/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.waitxy.com
 */
package com.waitxy.web.api;

import com.waitxy.common.core.domain.AjaxResult;
import com.waitxy.mall.config.CommonConstants;
import com.waitxy.mall.entity.GoodsCategory;
import com.waitxy.mall.service.GoodsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品类目
 *
 * @author www.waitxy.com
 * @date 2019-08-12 11:46:28
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/goodscategory")
@Api(value = "goodscategory", tags = "商品类目API")
public class GoodsCategoryApi {

    private final GoodsCategoryService goodsCategoryService;

    /**
     * 返回树形集合
     */
    @ApiOperation(value = "返回树形集合")
    @GetMapping("/tree")
    public AjaxResult goodsCategoryTree(GoodsCategory goodsCategory) {
        goodsCategory.setEnable(CommonConstants.YES);
        return AjaxResult.success(goodsCategoryService.selectTree(goodsCategory));
    }
}
