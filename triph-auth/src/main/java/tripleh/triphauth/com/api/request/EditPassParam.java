package tripleh.triphauth.com.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: zixli
 * Date: 2020/10/16 14:31
 * FileName: EditPassParam
 * Description: 修改密码请求参数
 */
@Data
public class EditPassParam {

    @ApiModelProperty("原始密码")
    private String orgPass;

    @ApiModelProperty("新密码")
    private String newPass;

}
