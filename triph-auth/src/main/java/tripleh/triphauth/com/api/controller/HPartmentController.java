package tripleh.triphauth.com.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tripleh.triphauth.com.api.request.OrgParam;
import tripleh.triphauth.com.api.response.ResponseResult;
import tripleh.triphauth.com.service.IHPartmentService;
import tripleh.triphcommon.com.config.CustomerUserDetails;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 * 组织表 前端控制器
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/org")
@Slf4j
@Api(tags = "组织api")
public class HPartmentController {

    @Autowired
    private IHPartmentService ihPartmentService;

    /**
     * 获取所有组织
     * @return
     */
    @GetMapping("/getData")
    @ApiOperation(value = "获取所有组织", httpMethod = "GET")
    public ResponseResult geData() {
        return ResponseResult.SUCCESS(ihPartmentService.getData());
    }

    /**
     * 添加组织
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加组织", httpMethod = "POST")
    public ResponseResult add(Authentication authentication, @RequestBody @Valid OrgParam orgParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        return ResponseResult.SUCCESS(ihPartmentService.add(orgParam, principal.gethUser()));
    }

    /**
     * 编辑组织
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "编辑组织", httpMethod = "POST")
    public ResponseResult edit(Authentication authentication, @RequestBody @Valid OrgParam orgParam) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihPartmentService.edit(orgParam, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 删除组织
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除组织", httpMethod = "GET")
    public ResponseResult delete(Authentication authentication, Long id) {
        CustomerUserDetails principal = (CustomerUserDetails) authentication.getPrincipal();
        ihPartmentService.delete(id, principal.gethUser());
        return ResponseResult.SUCCESS();
    }

    /**
     * 根据组织id获取所属角色
     * @return
     */
    @GetMapping("/getRolesByOrgId")
    @ApiOperation(value = "根据组织id获取所属角色", httpMethod = "GET")
    public ResponseResult getRolesByOrgId(Long id) {
        return ResponseResult.SUCCESS(ihPartmentService.getRolesByOrgId(id));
    }

    /**
     * 给组织附角色
     * @return
     */
    @GetMapping("/attachRoles")
    @ApiOperation(value = "给组织附角色", httpMethod = "GET")
    public ResponseResult attachRoles(Long orgId, String roleIds) {
        return ResponseResult.SUCCESS(ihPartmentService.attachRoles(orgId, roleIds));
    }

}

