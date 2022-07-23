/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.waitxy.com
 * 注意：
 * 本软件为www.waitxy.com开发研制，项目使用请保留此说明
 */
package com.waitxy.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waitxy.mall.entity.OrderItem;
import com.waitxy.mall.mapper.OrderItemMapper;
import com.waitxy.mall.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * 商城订单详情
 *
 * @author www.waitxy.com
 * @date 2019-09-10 15:31:40
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
