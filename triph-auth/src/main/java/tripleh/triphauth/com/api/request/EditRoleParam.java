package tripleh.triphauth.com.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: zixli
 * Date: 2020/10/14 10:18
 * FileName: EditRoleParam
 * Description: 编辑角色请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRoleParam {

    @ApiModelProperty("角色id")
    @NotNull
    private Long id;

    @ApiModelProperty("权限集合")
    private List<Long> authIds;

    @ApiModelProperty("角色名称")
    @NotBlank
    private String roleName;

}
