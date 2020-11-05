package com.sunnsoft.rabbitmq.order.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunnsoft.rabbitmq.order.data.model.entity.Torder;
import com.sunnsoft.rabbitmq.order.data.model.entity.TorderUser;

import java.sql.SQLException;

/**
 * <p>
 * 订单-外卖小哥表 服务类
 * </p>
 *
 * @author hkm
 * @since 2020-10-30
 */
public interface ITorderUserService extends IService<TorderUser> {

    /**
     * 外卖小哥接单
     * @param torderUser
     * @return
     */
    int insert(TorderUser torderUser) throws SQLException;

}
