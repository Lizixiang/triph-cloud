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
 * 权限表
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
public class HAuth extends Model<HAuth> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 路径
     */
    private String authUrl;
    /**
     * 上级权限id
     */
    private Long parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 菜单级别 0:系统菜单 1:一级菜单 2:二级菜单 以此类推
     */
    private String grade;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createName;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateName;
    /**
     * 删除标记 0:未删除 1:删除 默认0 
     */
    private String delFlag;


    public static final String ID = "id";

    public static final String AUTH_NAME = "auth_name";

    public static final String AUTH_URL = "auth_url";

    public static final String PARENT_ID = "parent_id";

    public static final String SORT = "sort";

    public static final String GRADE = "grade";

    public static final String CREATE_TIME = "create_time";

    public static final String CREATE_NAME = "create_name";

    public static final String UPDATE_TIME = "update_time";

    public static final String UPDATE_NAME = "update_name";

    public static final String DEL_FLAG = "del_flag";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
