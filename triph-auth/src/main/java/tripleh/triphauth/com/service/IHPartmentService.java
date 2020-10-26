package tripleh.triphauth.com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tripleh.triphauth.com.api.request.OrgParam;
import tripleh.triphauth.com.api.response.OrganizationBean;
import tripleh.triphauth.com.entity.HPartment;
import tripleh.triphcommon.com.entity.HUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组织表 服务类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
public interface IHPartmentService extends IService<HPartment> {

    /**
     * 获取所有组织
     * @return
     */
    List<OrganizationBean> getData();

    /**
     * 获取所有组织
     * @return
     */
    Long add(OrgParam orgParam, HUser hUser);

    /**
     * 编辑组织
     * @return
     */
    int edit(OrgParam orgParam, HUser hUser);

    /**
     * 删除组织
     * @return
     */
    int delete(Long id, HUser hUser);

    /**
     * 根据组织id获取所属角色
     * @return
     */
    Map<String, Object> getRolesByOrgId(Long id);

    /**
     * 给组织附角色
     * @return
     */
    int attachRoles(Long orgId, String ids);

}
