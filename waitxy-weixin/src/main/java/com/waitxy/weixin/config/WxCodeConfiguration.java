package com.waitxy.weixin.config;

import com.waitxy.common.core.redis.RedisCache;
import com.waitxy.weixin.entity.WxErrorCode;
import com.waitxy.weixin.service.WxErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class WxCodeConfiguration {
    @Autowired
    RedisCache redisUtil;
    @Autowired
    WxErrorCodeService wxErrorCodeService;

    @Bean
    public void cache() {
        List<WxErrorCode> wxErrorCodeList = wxErrorCodeService.list();
        for (WxErrorCode wxErrorCode : wxErrorCodeList) {
            String code = wxErrorCode.getErrCode();
            String redisKey = "wx:code:" + code;
            redisUtil.setCacheObject(redisKey, wxErrorCode.getErrMsg());
        }
        log.info("WxCodeConfiguration 缓存微信错误码 共{}条", wxErrorCodeList.size());
    }

}
