package tripleh.triphauth.com.api.controller;


import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tripleh.triphauth.com.api.request.EditPassParam;
import tripleh.triphauth.com.api.request.HUserParams;
import tripleh.triphauth.com.api.request.UserParam;
import tripleh.triphauth.com.api.response.ResponseResult;
import tripleh.triphauth.com.service.IHUserService;
import tripleh.triphcommon.com.config.CustomerUserDetails;
import tripleh.triphcommon.com.enums.StatusEnum;

import java.util.HashMap;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户api")
public class HUserController {

    @Autowired
    private IHUserService ihUserService;

    /**
     * 用户列表下拉菜单
     * @return
     */
    @GetMapping("/option/init")
    @ApiOperation(value = "用户列表下拉菜单", httpMethod = "GET")
    public ResponseResult initOption() {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("statusOption", StatusEnum.getAll());
        return ResponseResult.SUCCESS(map);
    }

    /**
     * 用户列表
     * @return
     */
    @PostMapping("/data/get")
    @ApiOperation(value = "用户列表", httpMethod = "POST")
    public Object getData(@RequestBody HUserParams hUserParams) {
        log.info("getData and HUserParams:{}", hUserParams);
        return ihUserService.listByParams(hUserParams);
    }

    /**
     * 根据用户id查询所属组织
     * @return
     */
    @GetMapping("/getOrgByUserId")
    @ApiOperation(value = "根据用户id查询所属组织", httpMethod = "GET")
    public ResponseResult getOrgByUserId(Long userId) {
        log.info("getOrgByUserId and userId:{}", userId);
        return ResponseResult.SUCCESS(ihUserService.getOrgByUserId(userId));
    }

    /**
     * 根据用户id查询所有权限
     * @return
     */
    @GetMapping("/getAuthByUserId")
    @ApiOperation(value = "根据用户id查询所有权限", httpMethod = "GET")
    public ResponseResult getAuthByUserId(Long userId) {
        log.info("getAuthByUserId and userId:{}", userId);
        return ResponseResult.SUCCESS(ihUserService.getAuthByUserId(userId));
    }

    /**
     * 给用户附组织
     * @return
     */
    @GetMapping("/attachOrg")
    @ApiOperation(value = "给用户附组织", httpMethod = "GET")
    public ResponseResult attachOrg(Long userId, String orgIds) {
        log.info("attachOrg and userId:{}, orgIds:{}", userId, orgIds);
        ihUserService.attachOrg(userId, orgIds);
        return ResponseResult.SUCCESS();
    }

    /**
     * 给用户附权限
     * @return
     */
    @GetMapping("/attachAuth")
    @ApiOperation(value = "给用户附权限", httpMethod = "GET")
    public ResponseResult attachAuth(Long userId, String authIds) {
        log.info("attachAuth and userId:{}, authIds:{}", userId, authIds);
        ihUserService.attachAuth(userId, authIds);
        return ResponseResult.SUCCESS();
    }

    /**
     * 添加用户
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加用户", httpMethod = "POST")
    public ResponseResult add(Authentication authentication, @RequestBody UserParam userParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihUserService.add(userParam, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping("/editPass")
    @ApiOperation(value = "修改密码", httpMethod = "POST")
    public ResponseResult editPass(Authentication authentication, @RequestBody EditPassParam editPassParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihUserService.editPass(editPassParam, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

}

