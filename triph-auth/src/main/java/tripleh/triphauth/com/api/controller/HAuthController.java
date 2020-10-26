package tripleh.triphauth.com.api.controller;


import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;
import tripleh.triphauth.com.api.request.HAuthParam;
import tripleh.triphauth.com.api.response.ResponseResult;
import tripleh.triphauth.com.service.IHAuthService;
import tripleh.triphcommon.com.config.CustomerUserDetails;

import java.util.HashMap;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@Api(tags = "权限api")
public class HAuthController {

    @Autowired
    private IHAuthService ihAuthService;

    @Autowired
    ConsumerTokenServices tokenServices;

    /**
     * 根据登录信息获取权菜单列表
     * @return
     */
    @GetMapping("/getAuthoritiesByPrincipal")
    @ApiOperation(value = "根据登录信息获取菜单列表", httpMethod = "GET")
    public ResponseResult getAuthoritiesByPrincipal(Authentication authentication) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        return ResponseResult.SUCCESS(ihAuthService.getAuthoritiesByUser(principal.gethUser()));
    }

    /**
     * 获取所有菜单列表
     * @return
     */
    @GetMapping("/getAllAuthorities")
    @ApiOperation(value = "获取所有菜单列表", httpMethod = "GET")
    public ResponseResult getAllAuthorities(Authentication authentication) {
        return ResponseResult.SUCCESS(ihAuthService.getAllAuthorities());
    }

    /**
     * 添加系统菜单
     * @return
     */
    @PostMapping("/addSystemMenu")
    @ApiOperation(value = "添加系统菜单", httpMethod = "POST")
    public ResponseResult addSystemMenu(Authentication authentication, String menuName) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihAuthService.addSystemMenu(menuName, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 添加菜单
     * @return
     */
    @PostMapping("/addMenu")
    @ApiOperation(value = "添加菜单", httpMethod = "POST")
    public ResponseResult addMenu(Authentication authentication, @RequestBody HAuthParam hAuthParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        return ResponseResult.SUCCESS(ihAuthService.addMenu(hAuthParam, principal.gethUser()));
    }

    /**
     * 编辑菜单
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "编辑菜单", httpMethod = "POST")
    public ResponseResult edit(Authentication authentication, @RequestBody HAuthParam hAuthParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihAuthService.edit(hAuthParam, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 删除菜单
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除菜单", httpMethod = "GET")
    public ResponseResult delete(Authentication authentication, Long id) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihAuthService.delete(id, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 根据角色id获取对应权限
     * @return
     */
    @GetMapping("/getAuth/{roleId}")
    @ApiOperation(value = "删除菜单", httpMethod = "GET")
    public ResponseResult getAuth(Authentication authentication, @PathVariable Long roleId) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("roleAuth", ihAuthService.getAuthByRoleId(roleId));
        map.put("allAuth", ihAuthService.getAllAuthorities());
        return ResponseResult.SUCCESS(map);
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @GetMapping("/logOut/{token}")
    @ApiOperation(value = "删除菜单", httpMethod = "GET")
    public ResponseResult revokeToken(@PathVariable String token) {
        log.info("revokeToken and token:{}", token);
        tokenServices.revokeToken(token);
        return ResponseResult.SUCCESS();
    }
}

