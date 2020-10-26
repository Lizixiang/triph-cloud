package tripleh.triphauth.com.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: zixli
 * Date: 2020/9/21 16:58
 * FileName: MenuBean
 * Description: 菜单权限集合
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuBean {

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("权限名称")
    private String authName;
    @ApiModelProperty("路径")
    private String authUrl;
    @ApiModelProperty("上级权限id")
    private Long parentId;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("菜单级别 0:系统菜单 1:一级菜单 2:二级菜单 以此类推")
    private String grade;
//    @ApiModelProperty("图标")
//    private String logo;
    @ApiModelProperty("子菜单")
    private List<MenuBean> subMenu;

}
