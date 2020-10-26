package tripleh.triphauth.com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 组织角色关系表
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HDepRoleRel extends Model<HDepRoleRel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组织id
     */
    private Long deptId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 创建时间
     */
    private Date createTime;


    public static final String ID = "id";

    public static final String DEPT_ID = "dept_id";

    public static final String ROLE_ID = "role_id";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
