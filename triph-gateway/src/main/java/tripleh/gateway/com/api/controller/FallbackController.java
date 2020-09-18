package tripleh.gateway.com.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tripleh.gateway.com.api.response.CommonResultCode;
import tripleh.gateway.com.api.response.ResponseResult;

/**
 * Author: zixli
 * Date: 2020/9/15 17:26
 * FileName: FallbackController
 * Description: Hystrix熔断降级地址
 */

@RestController
public class FallbackController {

    @GetMapping("/fallbackA")
    public ResponseResult fallbackA() {
        return ResponseResult.FAIL(CommonResultCode.SERVICE_UNAVAILABLE);
    }
}