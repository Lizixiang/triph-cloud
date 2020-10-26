package tripleh.triphauth.com.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import tripleh.triphauth.com.api.request.EditPassParam;
import tripleh.triphauth.com.api.request.HUserParams;
import tripleh.triphauth.com.api.request.UserParam;
import tripleh.triphcommon.com.entity.HUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zixli
 * @since 2020-08-26
 */
public interface IHUserService extends UserDetailsService {

    /**
     * 根据条件分页查询用户
     * @param hUserParams
     * @return
     */
    Object listByParams(HUserParams hUserParams);

    /**
     * 根据用户id查询所属组织
     * @param userId
     * @return
     */
    Map getOrgByUserId(Long userId);

    /**
     * 根据用户id查询所属组织
     * @param userId
     * @return
     */
    Map getAuthByUserId(Long userId);

    /**
     * 给用户附组织
     * @param userId
     * @param orgIds
     * @return
     */
    int attachOrg(Long userId, String orgIds);

    /**
     * 给用户附组织
     * @param userId
     * @param authIds
     * @return
     */
    int attachAuth(Long userId, String authIds);

    /**
     * 添加用户
     * @param userParam
     * @param hUser
     * @return
     */
    int add(UserParam userParam, HUser hUser);

    /**
     * 修改密码
     * @param editPassParam
     * @param hUser
     * @return
     */
    int editPass(EditPassParam editPassParam, HUser hUser);
}
