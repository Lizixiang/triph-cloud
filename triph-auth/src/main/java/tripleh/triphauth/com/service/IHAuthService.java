package tripleh.triphauth.com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tripleh.triphauth.com.api.request.HAuthParam;
import tripleh.triphauth.com.api.response.MenuBean;
import tripleh.triphauth.com.entity.HAuth;
import tripleh.triphcommon.com.entity.HUser;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
public interface IHAuthService extends IService<HAuth> {

    /**
     * 根据登录用户获取权限
     * @param hUser
     * @return
     */
    List<MenuBean> getAuthoritiesByUser(HUser hUser);

    /**
     * 获取所有菜单列表
     * @return
     */
    List<MenuBean> getAllAuthorities();

    /**
     * 添加系统菜单
     * @param menuName  菜单名称
     * @return
     */
    int addSystemMenu(String menuName, HUser hUser);

    /**
     * 添加菜单
     * @param hAuthParam
     * @param hUser
     * @return
     */
    Long addMenu(HAuthParam hAuthParam, HUser hUser);

    /**
     * 编辑菜单
     * @param hAuthParam  菜单权限请求参数
     * @return
     */
    int edit(HAuthParam hAuthParam, HUser hUser);

    /**
     * 删除菜单
     * @param id 权限id
     * @param hUser
     * @return
     */
    int delete(Long id, HUser hUser);

    /**
     *  根据角色id获取对应权限
     * @param roleId 角色id
     * @return
     */
    List<Long> getAuthByRoleId(Long roleId);

    /**
     * 根据id查询权限
     * @param ids 权限id集合
     * @return
     */
    List<HAuth> getAuthByIds(List<Long> ids);
}
