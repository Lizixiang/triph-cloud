package tripleh.triphauth.com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tripleh.triphauth.com.api.request.EditRoleParam;
import tripleh.triphauth.com.entity.HRole;
import tripleh.triphcommon.com.entity.HUser;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
public interface IHRoleService extends IService<HRole> {

    /**
     * 编辑角色权限
     * @param editRoleParam 编辑角色请求参数
     * @param hUser
     * @return
     */
    int editRole(EditRoleParam editRoleParam, HUser hUser);

    /**
     * 删除角色
     * @param ids 角色ids
     * @param hUser
     * @return
     */
    void deleteRoles(List<Long> ids, HUser hUser);
}
