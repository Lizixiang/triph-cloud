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
import tripleh.triphcommon.com.entity.HUser;
import tripleh.triphcommon.com.entity.PageFactory;

/**
 * Author: zixli
 * Date: 2020/10/14 23:04
 * FileName: HUserParams
 * Description: 用户列表请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HUserParams extends PageFactory {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("状态")
    private String status;

    public QueryWrapper getWrapper() {
        QueryWrapper<HUser> query = Wrappers.query();
        if (StringUtils.isNotBlank(username)) {
            query.like("username", username);
        }
        if (StringUtils.isNotBlank(status)) {
            query.eq("status", status);
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
