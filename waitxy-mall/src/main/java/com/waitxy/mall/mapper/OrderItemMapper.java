/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.waitxy.com
 * 注意：
 * 本软件为www.waitxy.com开发研制，项目使用请保留此说明
 */
package com.waitxy.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.waitxy.mall.entity.OrderItem;

import java.util.List;

/**
 * 商城订单详情
 *
 * @author www.waitxy.com
 * @date 2019-09-10 15:31:40
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

	List<OrderItem> selectList2(OrderItem orderItem);

}
