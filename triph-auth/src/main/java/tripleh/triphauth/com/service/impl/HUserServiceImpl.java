package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tripleh.triphauth.com.api.request.EditPassParam;
import tripleh.triphauth.com.api.request.HUserParams;
import tripleh.triphauth.com.api.request.UserParam;
import tripleh.triphauth.com.entity.*;
import tripleh.triphauth.com.handler.ServiceException;
import tripleh.triphauth.com.service.*;
import tripleh.triphcommon.com.config.CustomerUserDetails;
import tripleh.triphcommon.com.entity.HUser;
import tripleh.triphcommon.com.mapper.HUserDao;
import tripleh.triphcommon.com.utils.MD5Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-08-26
 */
@Service("userService")
@Slf4j
public class HUserServiceImpl extends ServiceImpl<HUserDao, HUser> implements IHUserService {

    @Autowired
    private IHPartmentService ihPartmentService;

    @Autowired
    private IHUserPartmentRelService ihUserPartmentRelService;

    @Autowired
    private IHAuthService ihAuthService;

    @Autowired
    private IHUserAuthRelService ihUserAuthRelService;

    @Autowired
    private IHDepRoleRelService ihDepRoleRelService;

    @Autowired
    private IHRoleAuthRelService ihRoleAuthRelService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loadUserByUsername and username:{}", s);
        QueryWrapper<HUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", s);
        HUser hUser = baseMapper.selectOne(wrapper);
        log.info("user info:{}", hUser);
        if (ObjectUtils.isEmpty(hUser)) {
            log.error("user not exists...");
            throw new ServiceException("该用户不存在！");
        }
        if ("1".equals(hUser.getStatus())) {
            log.error("user is disabled...");
            throw new ServiceException("该用户已停用！");
        }
        return new CustomerUserDetails(hUser);
    }

    @Override
    public Object listByParams(HUserParams hUserParams) {
        return baseMapper.selectPage(hUserParams.getIPage(), hUserParams.getWrapper());
    }

    @Override
    public Map getOrgByUserId(Long userId) {
        log.info("getOrgByUserId and userId:{}", userId);
        HashMap<String, Object> map = Maps.newHashMap();
        QueryWrapper<HPartment> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        List<HPartment> hPartments = ihPartmentService.list(wrapper);
        map.put("all", hPartments);
        QueryWrapper<HUserPartmentRel> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", userId);
        List<Long> collect = ihUserPartmentRelService.list(wrapper1).stream().map(x -> x.getPartmentId()).collect(Collectors.toList());
        map.put("selected", collect);
        return map;
    }

    @Override
    public Map getAuthByUserId(Long userId) {
        log.info("getAuthByUserId and userId:{}", userId);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("all", ihAuthService.getAllAuthorities());
        QueryWrapper<HUserAuthRel> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Long> collect = ihUserAuthRelService.list(wrapper).stream().map(x -> x.getAuthId()).collect(Collectors.toList());
        map.put("selected", collect);
        return map;
    }

    @Transactional
    @Override
    public int attachOrg(Long userId, String orgIds) {
        // 清空用户的所有组织
        QueryWrapper<HUserPartmentRel> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        boolean remove = ihUserPartmentRelService.remove(wrapper);
        log.info("remove and result:{}", remove);
        // 清空用户的权限
        QueryWrapper<HUserAuthRel> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("user_id", userId);
        ihUserAuthRelService.remove(wrapper3);
        if (!ObjectUtils.isEmpty(orgIds)) {
            ArrayList<HUserPartmentRel> objects = Lists.newArrayList();
            for (String s : orgIds.split(",")) {
                objects.add(HUserPartmentRel.builder()
                        .userId(userId)
                        .partmentId(Long.parseLong(s))
                        .createTime(new Date())
                        .build());
            }
            // 重新给用户附组织
            ihUserPartmentRelService.saveBatch(objects);
            ArrayList<Long> deptIds = Lists.newArrayList();
            for (String s : orgIds.split(",")) {
                deptIds.add(Long.parseLong(s));
            }
            // 根据部门id获取对应所有角色id
            QueryWrapper<HDepRoleRel> wrapper1 = new QueryWrapper<>();
            wrapper1.in("dept_id", deptIds);
            List<HDepRoleRel> list1 = ihDepRoleRelService.list(wrapper1);
            // 根据角色id获取对应的权限id
            QueryWrapper<HRoleAuthRel> wrapper2 = new QueryWrapper<>();
            wrapper2.in("role_id", list1.stream().map(x -> x.getRoleId()).collect(Collectors.toList()));
            List<HRoleAuthRel> list2 = ihRoleAuthRelService.list(wrapper2);
            List<Long> authIds = list2.stream().map(x -> x.getAuthId()).distinct().collect(Collectors.toList());
            // 重新给用户附权限
            ArrayList<HUserAuthRel> objects1 = Lists.newArrayList();
            for (Long authId : authIds) {
                objects1.add(HUserAuthRel.builder()
                        .userId(userId)
                        .authId(authId)
                        .createTime(new Date())
                        .build());
            }
            ihUserAuthRelService.saveBatch(objects1);
        }
        return 0;
    }

    @Override
    public int attachAuth(Long userId, String authIds) {
        // 清空用户的权限
        QueryWrapper<HUserAuthRel> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("user_id", userId);
        ihUserAuthRelService.remove(wrapper3);
        // 重新给用户赋权限
        if (!ObjectUtils.isEmpty(authIds)) {
            ArrayList<Long> objects = Lists.newArrayList();
            for (String s : authIds.split(",")) {
                objects.add(Long.parseLong(s));
            }
            QueryWrapper<HAuth> wrapper = new QueryWrapper<>();
            wrapper.in("id", objects);
            wrapper.eq("grade", "2");
            List<Long> collect = ihAuthService.list(wrapper).stream().map(x -> x.getId()).collect(Collectors.toList());
            ArrayList<HUserAuthRel> objects1 = Lists.newArrayList();
            for (Long authId : collect) {
                objects1.add(HUserAuthRel.builder()
                        .userId(userId)
                        .authId(authId)
                        .createTime(new Date())
                        .build());
            }
            ihUserAuthRelService.saveBatch(objects1);
        }
        return 0;
    }

    @Override
    public int add(UserParam userParam, HUser hUser) {
        log.info("add and hUser:{}, userParam:{}", hUser, userParam);
        QueryWrapper<HUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", userParam.getUsername());
        List<HUser> hUsers = baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(hUsers)) {
            log.error("username:{} already exists...", userParam.getUsername());
            throw new ServiceException("该用户名已存在");
        }
        HUser build = HUser.builder()
                .username(userParam.getUsername())
                .password(MD5Utils.encrypt(userParam.getPassword()))
                .createTime(new Date())
                .status("0")
                .delFlag("0")
                .build();
        return baseMapper.insert(build);
    }

    @Override
    public int editPass(EditPassParam editPassParam, HUser hUser) {
        log.info("editPass and editPassParam:{}, hUser:{}", editPassParam, hUser);
        HUser hUser1 = baseMapper.selectById(hUser.getId());
        if (ObjectUtils.isEmpty(hUser1)) {
            log.error("user not exists...");
            throw new ServiceException("该用户不存在");
        }
        if (!MD5Utils.encrypt(editPassParam.getOrgPass()).equals(hUser1.getPassword())) {
            log.error("incorrect orgPass...");
            throw new ServiceException("原始密码不正确");
        }
        hUser1.setPassword(MD5Utils.encrypt(editPassParam.getNewPass()));
        return baseMapper.updateById(hUser1);
    }
}
