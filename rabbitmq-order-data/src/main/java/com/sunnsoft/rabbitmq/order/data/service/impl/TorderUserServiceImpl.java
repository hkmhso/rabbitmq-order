package com.sunnsoft.rabbitmq.order.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunnsoft.rabbitmq.order.data.mapper.TorderMapper;
import com.sunnsoft.rabbitmq.order.data.mapper.TorderUserMapper;
import com.sunnsoft.rabbitmq.order.data.model.entity.TorderUser;
import com.sunnsoft.rabbitmq.order.data.service.ITorderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * <p>
 * 订单-外卖小哥表 服务实现类
 * </p>
 *
 * @author hkm
 * @since 2020-10-30
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class TorderUserServiceImpl extends ServiceImpl<TorderUserMapper, TorderUser> implements ITorderUserService {

    @Resource
    private TorderUserMapper torderUserMapper;

    @Override
    public int insert(TorderUser torderUser) throws SQLException {
        return torderUserMapper.insert(torderUser);
    }
}
