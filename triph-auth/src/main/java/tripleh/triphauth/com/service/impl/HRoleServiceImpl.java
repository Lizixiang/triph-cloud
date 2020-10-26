package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tripleh.triphauth.com.api.request.EditRoleParam;
import tripleh.triphauth.com.entity.HAuth;
import tripleh.triphauth.com.entity.HRole;
import tripleh.triphauth.com.entity.HRoleAuthRel;
import tripleh.triphauth.com.handler.ServiceException;
import tripleh.triphauth.com.mapper.HRoleDao;
import tripleh.triphauth.com.service.IHAuthService;
import tripleh.triphauth.com.service.IHRoleAuthRelService;
import tripleh.triphauth.com.service.IHRoleService;
import tripleh.triphcommon.com.entity.HUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@Service
@Slf4j
public class HRoleServiceImpl extends ServiceImpl<HRoleDao, HRole> implements IHRoleService {

    @Autowired
    private IHRoleAuthRelService ihRoleAuthRelService;

    @Autowired
    private IHAuthService ihAuthService;

    @Autowired
    private IHRoleService ihRoleService;

    @Transactional
    @Override
    public int editRole(EditRoleParam editRoleParam, HUser hUser) {
        log.info("editRole and editRoleParam:{}, hUser:{}", editRoleParam, hUser);
        // 新增
        if (editRoleParam.getId().compareTo(0L) == 0) {
            // 新增角色
            HRole hRole = HRole.builder()
                    .roleName(editRoleParam.getRoleName())
                    .createTime(new Date())
                    .createName(hUser.getUsername())
                    .delFlag("0")
                    .build();
            boolean bol = ihRoleService.save(hRole);
            // 赋权
            List<HAuth> hAuths = ihAuthService.getAuthByIds(editRoleParam.getAuthIds());
            List<Long> authIds = hAuths.stream().filter(x -> "2".equals(x.getGrade())).map(item -> item.getId()).collect(Collectors.toList());
            ArrayList<HRoleAuthRel> objects = Lists.newArrayList();
            for (Long authId : authIds) {
                objects.add(HRoleAuthRel.builder()
                        .roleId(hRole.getId())
                        .authId(authId)
                        .createTime(new Date())
                        .build());
            }
            return ihRoleAuthRelService.saveBatch(objects) ? 1 : 0;
        } else {
            HRole hRole = baseMapper.selectById(editRoleParam.getId());
            if (ObjectUtils.isEmpty(hRole)) {
                log.error("roleName:{} not exists...", editRoleParam.getRoleName());
                throw new ServiceException("该角色不存在");
            }
            // 清空该角色下的所有权限
            int i = ihRoleAuthRelService.deleteAuthByRoleId(editRoleParam.getId());
            log.info("deleteAuthByRoleId and result:{}", i);
            // 修改角色名称
            hRole.setRoleName(editRoleParam.getRoleName());
            hRole.setUpdateTime(new Date());
            hRole.setUpdateName(hUser.getUsername());
            int i1 = baseMapper.updateById(hRole);
            log.info("updateById and result:{}", i1);
            if (ObjectUtils.isEmpty(editRoleParam.getAuthIds())) {
                log.warn("editRoleParam.getAuthIds() is empty...");
                return i1;
            }
            // 重新赋权
            List<HAuth> hAuths = ihAuthService.getAuthByIds(editRoleParam.getAuthIds());
            List<Long> authIds = hAuths.stream().filter(x -> "2".equals(x.getGrade())).map(item -> item.getId()).collect(Collectors.toList());
            ArrayList<HRoleAuthRel> objects = Lists.newArrayList();
            for (Long authId : authIds) {
                objects.add(HRoleAuthRel.builder()
                        .roleId(editRoleParam.getId())
                        .authId(authId)
                        .createTime(new Date())
                        .build());
            }
            return ihRoleAuthRelService.saveBatch(objects) ? 1 : 0;
        }
    }

    @Transactional
    @Override
    public void deleteRoles(List<Long> ids, HUser hUser) {
        log.info("deleteRoles and ids:{}, hUser:{}", ids, hUser);
        if (ObjectUtils.isEmpty(ids)) {
            log.error("ids is empty...");
            throw new ServiceException("参数不能为空");
        }
        List<HRole> hRoles = baseMapper.selectBatchIds(ids);
        if (!ObjectUtils.isEmpty(hRoles)) {
            for (HRole hRole : hRoles) {
                hRole.setUpdateTime(new Date());
                hRole.setUpdateName(hUser.getUsername());
                hRole.setDelFlag("1");
                int i = baseMapper.updateById(hRole);
                // 清空该角色下的所有权限
                ihRoleAuthRelService.deleteAuthByRoleId(hRole.getId());
            }
        }
    }
}
