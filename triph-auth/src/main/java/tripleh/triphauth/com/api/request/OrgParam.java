package tripleh.triphauth.com.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Author: zixli
 * Date: 2020/10/14 17:23
 * FileName: OrgParam
 * Description: 组织请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgParam {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("组织名称")
    @NotBlank
    private String orgName;

}
