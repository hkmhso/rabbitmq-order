package com.sunnsoft.rabbitmq.order.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunnsoft.rabbitmq.order.data.mapper.TorderMapper;
import com.sunnsoft.rabbitmq.order.data.mapper.TorderUserMapper;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.service.ITorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author hkm
 * @since 2020-10-30
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class TorderServiceImpl extends ServiceImpl<TorderMapper, Torder> implements ITorderService {

    @Resource
    private TorderMapper torderMapper;

    @Override
    public int insert(Torder torder) throws SQLException {
        return torderMapper.insert(torder);
    }

    @Override
    public Torder selectByOrderId(Long orderId) {
        LambdaQueryWrapper<Torder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Torder::getOrderNum, orderId);
        return torderMapper.selectOne(lambdaQueryWrapper);
    }
}
