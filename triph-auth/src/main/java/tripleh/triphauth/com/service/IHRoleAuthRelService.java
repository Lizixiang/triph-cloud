package tripleh.triphauth.com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tripleh.triphauth.com.entity.HRoleAuthRel;

/**
 * <p>
 * 角色权限关系表 服务类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
public interface IHRoleAuthRelService extends IService<HRoleAuthRel> {

    /**
     * 清空角色所有权限
     * @param roleId
     * @return
     */
    int deleteAuthByRoleId(Long roleId);

}
