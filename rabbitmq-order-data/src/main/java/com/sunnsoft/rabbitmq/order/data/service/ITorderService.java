package com.sunnsoft.rabbitmq.order.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.model.entity.TorderUser;

import java.sql.SQLException;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author hkm
 * @since 2020-10-30
 */
public interface ITorderService extends IService<Torder> {

    /**
     * 用户下单
     * @param torder
     * @return
     */
    int insert(Torder torder) throws SQLException;

    /**
     * 根据订单id查询
     * @param orderId
     * @return
     */
    Torder selectByOrderId(Long orderId);

}
