package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tripleh.triphauth.com.api.request.HAuthParam;
import tripleh.triphauth.com.api.response.MenuBean;
import tripleh.triphauth.com.entity.*;
import tripleh.triphauth.com.handler.ServiceException;
import tripleh.triphauth.com.mapper.HAuthDao;
import tripleh.triphauth.com.service.*;
import tripleh.triphcommon.com.entity.HUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@Service
@Slf4j
public class HAuthServiceImpl extends ServiceImpl<HAuthDao, HAuth> implements IHAuthService {

    @Autowired
    private IHUserPartmentRelService ihUserPartmentRelService;

    @Autowired
    private IHDepRoleRelService ihDepRoleRelService;

    @Autowired
    private IHRoleAuthRelService ihRoleAuthRelService;

    @Autowired
    private IHUserAuthRelService ihUserAuthRelService;

    @Override
    public List<MenuBean> getAuthoritiesByUser(HUser hUser) {
        log.info("getAuthoritiesByUser and hUser:{}", hUser);

        // 菜单权限
        ArrayList<MenuBean> menus = Lists.newArrayList();

        // 根据用户id获取所在部门id
        QueryWrapper<HUserPartmentRel> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", hUser.getId());
        List<HUserPartmentRel> list = ihUserPartmentRelService.list(wrapper);
        if (!"admin".equals(hUser.getUsername()) && CollectionUtils.isEmpty(list)) {
            log.warn("username:{} not belong to any partment...", hUser.getUsername());
            return menus;
        }
        // 根据部门id获取对应所有角色id
        QueryWrapper<HDepRoleRel> wrapper1 = new QueryWrapper<>();
        wrapper1.in("dept_id", list.stream().map(x -> x.getPartmentId()).collect(Collectors.toList()));
        List<HDepRoleRel> list1 = ihDepRoleRelService.list(wrapper1);
        // 如果是超级管理员，则拥有所有权限
        if ("admin".equals(hUser.getUsername()) || list1.stream().filter(x -> x.getRoleId().compareTo(1L) == 0).count() > 0) {
            QueryWrapper<HAuth> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("del_flag", "0");
            wrapper2.orderByAsc("sort");
            List<HAuth> hAuths = baseMapper.selectList(wrapper2);
            packageMenu(hAuths, menus, "0", 0L);
            return menus;
        }
//        // 根据角色id获取对应的权限id
//        QueryWrapper<HRoleAuthRel> wrapper2 = new QueryWrapper<>();
//        wrapper2.in("role_id", list1.stream().map(x -> x.getRoleId()).collect(Collectors.toList()));
//        List<HRoleAuthRel> list2 = ihRoleAuthRelService.list(wrapper2);
        // 根据用户id获取所有权限
        QueryWrapper<HUserAuthRel> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("user_id", hUser.getId());
        List<HUserAuthRel> list2 = ihUserAuthRelService.list(wrapper2);
        List<Long> authIds = list2.stream().map(x -> x.getAuthId()).distinct().collect(Collectors.toList());
        QueryWrapper<HAuth> wrapper3 = new QueryWrapper<>();
        wrapper3.in("id", authIds);
        wrapper3.eq("del_flag", "0");
        wrapper3.orderByAsc("sort");
        // 二级菜单
        List<HAuth> hAuths = baseMapper.selectList(wrapper3);
        // 一级菜单
        List<Long> list3 = hAuths.stream().map(x -> x.getParentId()).distinct().collect(Collectors.toList());
        QueryWrapper<HAuth> wrapper4 = new QueryWrapper<>();
        wrapper4.in("id", list3);
        wrapper4.eq("del_flag", "0");
        List<HAuth> hAuths1 = baseMapper.selectList(wrapper4);
        hAuths.addAll(hAuths1);
        // 系统菜单
        List<Long> list4 = hAuths1.stream().map(x -> x.getParentId()).distinct().collect(Collectors.toList());
        QueryWrapper<HAuth> wrapper5 = new QueryWrapper<>();
        wrapper5.in("id", list4);
        wrapper5.eq("del_flag", "0");
        List<HAuth> hAuths2 = baseMapper.selectList(wrapper5);
        hAuths.addAll(hAuths2);
        packageMenu(hAuths, menus, "0", 0L);
        return menus;
    }

    @Override
    public List<MenuBean> getAllAuthorities() {
        // 菜单权限
        ArrayList<MenuBean> menus = Lists.newArrayList();

        QueryWrapper<HAuth> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        wrapper.orderByAsc("sort");
        List<HAuth> hAuths = baseMapper.selectList(wrapper);
        packageMenu(hAuths, menus, "0", 0L);
        return menus;
    }

    /**
     * 组装菜单
     * @param auths 权限
     * @param child 子菜单
     * @param grade 级别
     * @param parentId 父id
     */
    private void packageMenu(List<HAuth> auths, List<MenuBean> child, String grade, Long parentId) {
        if (!CollectionUtils.isEmpty(auths)) {
            List<HAuth> collect = auths.stream().filter(x -> (grade.equals(x.getGrade()) && x.getParentId().compareTo(parentId) == 0)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                collect.stream().forEach(item -> {
                    ArrayList<MenuBean> childs = Lists.newArrayList();
                    child.add(MenuBean.builder()
                            .authName(item.getAuthName())
                            .authUrl(item.getAuthUrl())
                            .grade(item.getGrade())
                            .id(item.getId())
                            .parentId(item.getParentId())
                            .sort(item.getSort())
                            .subMenu(childs)
                            .build());
                    packageMenu(auths, childs, (Integer.parseInt(grade)+1)+"", item.getId());
                });
            }
        }
    }

    @Override
    public int addSystemMenu(String menuName, HUser hUser) {
        log.info("addSystemMenu and menuName:{}", menuName);
        QueryWrapper<HAuth> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("grade", 0);
        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("sort");
        List<HAuth> hAuths = baseMapper.selectList(wrapper);
        int sort = 1;
        if (!CollectionUtils.isEmpty(hAuths)) {
            if (hAuths.stream().filter(x -> menuName.equals(x.getAuthName())).count() > 0) {
                log.error("systemMenu:{} already exists...", menuName);
                throw new ServiceException("该名称已存在！");
            }
            sort = hAuths.get(0).getSort() + 1;
        }
        HAuth hAuth = HAuth.builder()
                .authName(menuName)
                .parentId(0L)
                .sort(sort)
                .grade("0")
                .createTime(new Date())
                .createName(hUser.getUsername())
                .delFlag("0")
                .build();
        return baseMapper.insert(hAuth);
    }

    @Override
    public Long addMenu(HAuthParam hAuthParam, HUser hUser) {
        log.info("addMenu and hAuthParam:{}, hUser", hAuthParam, hUser);
        HAuth hAuth1 = baseMapper.selectById(hAuthParam.getParentId());
        if (ObjectUtils.isEmpty(hAuth1)) {
            log.error("parent menu not exists...");
            throw new ServiceException("父菜单不存在，无法添加");
        }
        QueryWrapper<HAuth> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", hAuthParam.getParentId());
        wrapper.eq("del_flag", "0");
        wrapper.orderByDesc("sort");
        List<HAuth> hAuths = baseMapper.selectList(wrapper);
        int sort = 1;
        if (!CollectionUtils.isEmpty(hAuths)) {
            if (hAuths.stream().filter(x -> hAuthParam.getMenuName().equals(x.getAuthName())).count() > 0) {
                log.error("systemMenu:{} already exists...", hAuthParam.getMenuName());
                throw new ServiceException("该名称已存在！");
            }
            sort = hAuths.get(0).getSort() + 1;
        }
        HAuth hAuth = HAuth.builder()
                .authName(hAuthParam.getMenuName())
                .authUrl(ObjectUtils.isEmpty(hAuthParam.getMenuUrl()) ? null : hAuthParam.getMenuUrl())
                .parentId(hAuthParam.getParentId())
                .sort(sort)
                .grade(Integer.parseInt(hAuth1.getGrade()) + 1 + "")
                .createTime(new Date())
                .createName(hUser.getUsername())
                .delFlag("0")
                .build();
        baseMapper.insert(hAuth);
        return hAuth.getId();
    }

    @Override
    public int edit(HAuthParam hAuthParam, HUser hUser) {
        log.info("edit and hAuthParam:{}, hUser:{}", hAuthParam, hUser);
        if (ObjectUtils.isEmpty(hAuthParam.getId()) || ObjectUtils.isEmpty(hAuthParam.getMenuName())) {
            log.error("id or menuName is blank...");
            throw new ServiceException("id或者menuName不能为空");
        }
        HAuth hAuth = baseMapper.selectById(hAuthParam.getId());
        if (ObjectUtils.isEmpty(hAuth)) {
            log.error("hAuth is empty...");
            throw new ServiceException("该菜单权限不存在");
        }
        QueryWrapper<HAuth> wrapper = new QueryWrapper<>();
        wrapper.eq("auth_name", hAuthParam.getMenuName());
        wrapper.eq("del_flag", "0");
        wrapper.notIn("id", hAuthParam.getId());
        List<HAuth> hAuths = baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(hAuths)) {
            log.error("menuName:{} already exists...", hAuthParam.getMenuName());
            throw new ServiceException("菜单名称已存在!");
        }
        hAuth.setAuthName(hAuthParam.getMenuName());
        if ("2".equals(hAuth.getGrade())) {
            hAuth.setAuthUrl(hAuthParam.getMenuUrl());
        }
        hAuth.setUpdateName(hUser.getUsername());
        hAuth.setUpdateTime(new Date());
        return baseMapper.updateById(hAuth);
    }

    @Override
    public int delete(Long id, HUser hUser) {
        log.info("delete and id:{}, hUser:{}", id, hUser);
        HAuth hAuth = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(hAuth)) {
            log.error("hAuth not exists...");
            throw new ServiceException("该菜单权限不存在，无法删除");
        }
        hAuth.setUpdateTime(new Date());
        hAuth.setUpdateName(hUser.getUsername());
        hAuth.setDelFlag("1");
        return baseMapper.updateById(hAuth);
    }

    @Override
    public List<Long> getAuthByRoleId(Long roleId) {
        log.info("getAuthByRoleId and roleId:{}", roleId);
        // 根据角色id获取对应的权限id
        QueryWrapper<HRoleAuthRel> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        List<HRoleAuthRel> list = ihRoleAuthRelService.list(wrapper);
        List<Long> authIds = list.stream().map(x -> x.getAuthId()).distinct().collect(Collectors.toList());
        log.info("getAuthByRoleId and result:{}", authIds);
        return authIds;
    }

    @Override
    public List<HAuth> getAuthByIds(List<Long> ids) {
        log.info("getAuthByIds and ids:{}", ids);
        if (ObjectUtils.isEmpty(ids)) {
            log.warn("ids is empty...");
            return Lists.newArrayList();
        }
        return baseMapper.selectBatchIds(ids).stream().filter(x -> "0".equals(x.getDelFlag())).collect(Collectors.toList());
    }
}
