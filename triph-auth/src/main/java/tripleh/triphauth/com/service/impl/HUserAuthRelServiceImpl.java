package tripleh.triphauth.com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tripleh.triphauth.com.entity.HUserAuthRel;
import tripleh.triphauth.com.mapper.HUserAuthRelDao;
import tripleh.triphauth.com.service.IHUserAuthRelService;

/**
 * <p>
 * 用户权限关系表 服务实现类
 * </p>
 *
 * @author zixli
 * @since 2020-10-14
 */
@Service
@Slf4j
public class HUserAuthRelServiceImpl extends ServiceImpl<HUserAuthRelDao, HUserAuthRel> implements IHUserAuthRelService {

}
