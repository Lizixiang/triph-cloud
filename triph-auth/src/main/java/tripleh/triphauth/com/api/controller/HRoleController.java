package tripleh.triphauth.com.api.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tripleh.triphauth.com.api.request.EditRoleParam;
import tripleh.triphauth.com.api.request.HRoleParam;
import tripleh.triphauth.com.api.response.ResponseResult;
import tripleh.triphauth.com.service.IHRoleService;
import tripleh.triphcommon.com.config.CustomerUserDetails;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/role")
@Slf4j
@Api(tags = "角色api")
public class HRoleController {

    @Autowired
    private IHRoleService ihRoleService;

    /**
     * 获取角色列表
     * @param hRoleParam
     * @return
     */
    @PostMapping("/getRoles")
    @ApiOperation(value = "获取角色列表", httpMethod = "POST")
    public Object getRoles(@RequestBody HRoleParam hRoleParam) {
        Page page = ihRoleService.page(hRoleParam.getIPage(), hRoleParam.getWrapper());
        return page;
    }

    /**
     * 新增/编辑角色权限
     * @param editRoleParam
     * @return
     */
    @PostMapping("/editRole")
    @ApiOperation(value = "新增/编辑角色权限", httpMethod = "POST")
    public ResponseResult editRole(Authentication authentication, @RequestBody @Valid EditRoleParam editRoleParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihRoleService.editRole(editRoleParam, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 删除角色
     * @param authentication
     * @param ids 角色id集合
     * @return
     */
    @GetMapping("/del/{ids}")
    @ApiOperation(value = "删除角色", httpMethod = "GET")
    public ResponseResult del(Authentication authentication, @PathVariable String ids) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ArrayList<Long> idList = Lists.newArrayList();
        for (String s : ids.split(",")) {
            idList.add(Long.parseLong(s));
        }
        ihRoleService.deleteRoles(idList, principal.gethUser());
        return ResponseResult.SUCCESS();
    }
}

