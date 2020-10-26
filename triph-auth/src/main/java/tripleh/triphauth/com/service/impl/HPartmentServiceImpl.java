package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tripleh.triphauth.com.api.request.OrgParam;
import tripleh.triphauth.com.api.response.OrganizationBean;
import tripleh.triphauth.com.entity.HDepRoleRel;
import tripleh.triphauth.com.entity.HPartment;
import tripleh.triphauth.com.entity.HRole;
import tripleh.triphauth.com.handler.ServiceException;
import tripleh.triphauth.com.mapper.HPartmentDao;
import tripleh.triphauth.com.service.IHDepRoleRelService;
import tripleh.triphauth.com.service.IHPartmentService;
import tripleh.triphauth.com.service.IHRoleService;
import tripleh.triphcommon.com.entity.HUser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@Service
@Slf4j
public class HPartmentServiceImpl extends ServiceImpl<HPartmentDao, HPartment> implements IHPartmentService {

    @Autowired
    private IHRoleService ihRoleService;

    @Autowired
    private IHDepRoleRelService ihDepRoleRelService;

    @Override
    public List<OrganizationBean> getData() {
        ArrayList<OrganizationBean> objects = Lists.newArrayList();
        QueryWrapper<HPartment> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        List<HPartment> hPartments = baseMapper.selectList(wrapper);
        List<HPartment> parentOrgs = hPartments.stream().filter(x -> x.getParentId().compareTo(0L) == 0).collect(Collectors.toList());
        packageOrg(hPartments, parentOrgs, objects);
        return objects;
    }

    private void packageOrg(List<HPartment> allOrg, List<HPartment> targetOrg, List<OrganizationBean> objects) {
        for (HPartment hPartment : targetOrg) {
            OrganizationBean organizationBean = OrganizationBean.builder()
                    .id(hPartment.getId())
                    .parentId(hPartment.getParentId())
                    .partmentCode(hPartment.getPartmentCode())
                    .partmentName(hPartment.getPartmentName())
                    .type(hPartment.getType())
                    .subOrg(new ArrayList<>())
                    .build();
            objects.add(organizationBean);
            List<HPartment> childs = allOrg.stream().filter(x -> x.getParentId().compareTo(hPartment.getId()) == 0).collect(Collectors.toList());
            packageOrg(allOrg, childs, organizationBean.getSubOrg());
        }
    }

    @Override
    public Long add(OrgParam orgParam, HUser hUser) {
        log.info("add and orgParam:{}, hUser:{}", orgParam, hUser);
        QueryWrapper<HPartment> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        wrapper.eq("partment_name", orgParam.getOrgName());
        List<HPartment> hPartments = baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(hPartments)) {
            log.error("orgName already exists...");
            throw new ServiceException("组织名称已存在");
        }
        QueryWrapper<HPartment> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("del_flag", "0");
        wrapper1.orderByDesc("partment_code");
        List<HPartment> hPartments1 = baseMapper.selectList(wrapper1);
        HPartment build = HPartment.builder()
                .partmentName(orgParam.getOrgName())
                .partmentCode(Integer.parseInt(hPartments1.get(0).getPartmentCode()) + 1 + "")
                .status("0")
                .parentId(orgParam.getParentId())
                .type(1)
                .createTime(new Date())
                .createName(hUser.getUsername())
                .delFlag("0")
                .build();
        baseMapper.insert(build);
        return build.getId();
    }

    @Override
    public int edit(OrgParam orgParam, HUser hUser) {
        log.info("edit and :{}, hUser:{}", orgParam, hUser);
        QueryWrapper<HPartment> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        wrapper.eq("partment_name", orgParam.getOrgName());
        List<HPartment> hPartments = baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(hPartments)) {
            log.error("orgName already exists...");
            throw new ServiceException("组织名称已存在");
        }
        HPartment hPartment = baseMapper.selectById(orgParam.getId());
        hPartment.setPartmentName(orgParam.getOrgName());
        hPartment.setUpdateName(hUser.getUsername());
        hPartment.setUpdateTime(new Date());
        return baseMapper.updateById(hPartment);
    }

    @Transactional
    @Override
    public int delete(Long id, HUser hUser) {
        log.info("delete and id:{}, hUser:{}", id, hUser);
        HPartment hPartment = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(hPartment)) {
            log.error("hpartment is null...");
            throw new ServiceException("该组织不存在");
        }
        hPartment.setDelFlag("1");
        hPartment.setUpdateTime(new Date());
        hPartment.setUpdateName(hUser.getUsername());
        int i = baseMapper.updateById(hPartment);
        log.info("updateById and result:{}", i);
        // 清空组织的所有角色
        QueryWrapper<HDepRoleRel> wrapper = new QueryWrapper<>();
        wrapper.eq("dept_id", id);
        return ihDepRoleRelService.remove(wrapper) ? 1 : 0;
    }

    @Override
    public Map<String, Object> getRolesByOrgId(Long id) {
        HashMap<String, Object> map = Maps.newHashMap();
        // 查询所有角色
        QueryWrapper<HRole> wrapper = new QueryWrapper<>();
        wrapper.eq("del_flag", "0");
        List<HRole> list = ihRoleService.list(wrapper);
        // 查询组织对应的角色
        QueryWrapper<HDepRoleRel> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("dept_id", id);
        List<Long> list1 = ihDepRoleRelService.list(wrapper1).stream().map(x -> x.getRoleId()).collect(Collectors.toList());
        map.put("selected", list1);
        map.put("all", list);
        return map;
    }

    @Transactional
    @Override
    public int attachRoles(Long orgId, String ids) {
        log.info("attachRoles and orgId:{}, and ids:{}", orgId, ids);
        // 清空组织的所有角色
        QueryWrapper<HDepRoleRel> wrapper = new QueryWrapper<>();
        wrapper.eq("dept_id", orgId);
        boolean remove = ihDepRoleRelService.remove(wrapper);
        log.info("attachRoles remove and result:{}", remove);
        if (ObjectUtils.isEmpty(ids)) {
            log.warn("ids is empty...");
            return 0;
        }
        // 重新给组织附角色
        ArrayList<HDepRoleRel> objects = Lists.newArrayList();
        for (String s : ids.split(",")) {
            objects.add(HDepRoleRel.builder()
                    .deptId(orgId)
                    .roleId(Long.parseLong(s))
                    .createTime(new Date())
                    .build());
        }
        return ihDepRoleRelService.saveBatch(objects) ? 1 : 0;
    }
}
