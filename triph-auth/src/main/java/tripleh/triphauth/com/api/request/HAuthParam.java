package tripleh.triphauth.com.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Author: zixli
 * Date: 2020/10/9 15:10
 * FileName: HAuthParam
 * Description: 菜单权限请求参数
 */
@Data
public class HAuthParam implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单路径")
    private String menuUrl;

    @ApiModelProperty("父id")
    private Long parentId;

}
