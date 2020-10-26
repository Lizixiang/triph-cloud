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
 * 组织表
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
public class HPartment extends Model<HPartment> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 组织名称
     */
    private String partmentName;
    /**
     * 组织编码
     */
    private String partmentCode;
    /**
     * 状态 0:正常 1:禁用 默认是0
     */
    private String status;
    /**
     * 上级组织id
     */
    private Long parentId;
    /**
     * 组织类型
     */
    private Integer type;
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

    public static final String PARTMENT_NAME = "partment_name";

    public static final String PARTMENT_CODE = "partment_code";

    public static final String STATUS = "status";

    public static final String PARENT_ID = "parent_id";

    public static final String TYPE = "type";

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
