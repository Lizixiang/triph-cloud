package tripleh.triphauth.com.api.request;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripleh.triphauth.com.entity.HRole;
import tripleh.triphcommon.com.entity.PageFactory;

import java.util.Objects;

/**
 * Author: zixli
 * Date: 2020/10/10 17:56
 * FileName: HRoleParam
 * Description: 角色请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HRoleParam extends PageFactory {

    @ApiModelProperty("角色名称")
    private String roleName;

    public QueryWrapper getWrapper() {
        QueryWrapper<HRole> query = Wrappers.query();
        if (StringUtils.isNotBlank(roleName)) {
            query.like("role_name", roleName);
        }
        if ("asc".equalsIgnoreCase(getOrderType())) {
            query.orderByAsc("create_time");
        } else {
            query.orderByDesc("create_time");
        }
        query.eq("del_flag", "0");
        return query;
    }
}
