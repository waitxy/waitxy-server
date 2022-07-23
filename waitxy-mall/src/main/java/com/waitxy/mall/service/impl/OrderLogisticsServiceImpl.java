/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.waitxy.com
 * 注意：
 * 本软件为www.waitxy.com开发研制，项目使用请保留此说明
 */
package com.waitxy.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.waitxy.mall.entity.OrderLogistics;
import com.waitxy.mall.mapper.OrderLogisticsMapper;
import com.waitxy.mall.service.OrderLogisticsService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 订单物流
 *
 * @author www.waitxy.com
 * @date 2019-09-16 09:53:17
 */
@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements OrderLogisticsService {

    @Override
    public OrderLogistics getById(Serializable id) {
        return baseMapper.selectById2(id);
    }
}
