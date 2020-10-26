package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tripleh.triphauth.com.entity.HRoleAuthRel;
import tripleh.triphauth.com.mapper.HRoleAuthRelDao;
import tripleh.triphauth.com.service.IHRoleAuthRelService;

/**
 * <p>
 * 角色权限关系表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-09-21
 */
@Service
@Slf4j
public class HRoleAuthRelServiceImpl extends ServiceImpl<HRoleAuthRelDao, HRoleAuthRel> implements IHRoleAuthRelService {

    @Override
    public int deleteAuthByRoleId(Long roleId) {
        log.info("deleteAuthByRoleId and roleId:{}", roleId);
        QueryWrapper<HRoleAuthRel> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        return baseMapper.delete(wrapper);
    }

}
