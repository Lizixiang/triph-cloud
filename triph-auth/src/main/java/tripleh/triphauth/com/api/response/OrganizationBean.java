package tripleh.triphauth.com.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zixli
 * Date: 2020/10/14 15:53
 * FileName: OrganizationBean
 * Description: 组织机构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationBean {

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("组织名称")
    private String partmentName;
    @ApiModelProperty("组织编码")
    private String partmentCode;
    @ApiModelProperty("上级组织id")
    private Long parentId;
    @ApiModelProperty("组织类型")
    private Integer type;
    @ApiModelProperty("子部门")
    private List<OrganizationBean> subOrg;

}
