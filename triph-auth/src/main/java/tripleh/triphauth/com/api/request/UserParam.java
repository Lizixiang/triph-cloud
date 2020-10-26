package tripleh.triphauth.com.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: zixli
 * Date: 2020/10/16 9:10
 * FileName: UserParam
 * Description: 用户请求参数
 */
@Data
public class UserParam {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
