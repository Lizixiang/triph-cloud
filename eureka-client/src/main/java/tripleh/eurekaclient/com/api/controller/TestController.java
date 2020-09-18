package tripleh.eurekaclient.com.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: zixli
 * Date: 2020/9/15 15:26
 * FileName: TestController
 * Description: 测试类
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

}
