package com.waitxy.web.controller.weixin;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.waitxy.common.core.controller.BaseController;
import com.waitxy.common.core.domain.AjaxResult;
import com.waitxy.common.core.redis.RedisCache;
import com.waitxy.weixin.entity.WxUser;
import com.waitxy.weixin.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;


/**
 * @author ：admin
 * @date ：Created in 2019/6/27 20:20
 * @Time: 20:20
 * @description：微信登录的实现类
 * @modified By：
 * @version: 1.0$
 */
@Slf4j
@RestController
@RequestMapping("wechat")
@Api(tags = "微信授权")
public class WXUserInfoController extends BaseController {

    @Autowired
    WxUserService wxUserService;
    @Autowired
    RedisCache redisUtil;
    private String redisKey = "wx:code:";

    private String APPID = "wxac701ad414ce114a";

    private String APPSECRET = "8e453486a311b658c1493882ee61627e";

    private String backUrl = "https://www.waitxy.com";//授权域名

    //    private String backApi = "/prod-api/wechat/callBack";//回调接口

    private String backApi = "/authorization/authorize.html";//回调接口

    /**
     * 公众号微信登录授权
     *
     * @return
     * @throws ParseException
     */
    @ApiOperation("微信授权信息")
    @GetMapping("/authorization")
    public ModelAndView authorization(String url) {
        // 第一步：用户同意授权，获取code
        String getCodeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + APPID + "&redirect_uri="
                + URLUtil.encode(backUrl + backApi + "?surveyId=" + url) + "&response_type=code" + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";
        url = "redirect:" + getCodeUrl;
        log.info("-----------------------重定向到微信callBack方法---------------------" + url);
        return new ModelAndView(url);
    }

    /**
     * 公众号微信登录授权回调函数
     *
     * @return
     * @throws ParseException
     */
    @ApiOperation("微信授权信息")
    @PostMapping("/callBack")
    public AjaxResult callBack(@RequestBody String requestJson) {
        String code = JSONObject.parseObject(requestJson).getString("code");
        // 第二步：通过code换取网页授权access_token
        String getTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret="
                + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
        String getTokenResult = HttpUtil.get(getTokenUrl);
//        log.info("通过code换取网页授权access_token:{}  getTokenResult:{}", getTokenUrl, getTokenResult);
        JSONObject getTokenJson = JSONObject.parseObject(getTokenResult);
        log.info("通过code换取网页授权access_token:{}", getTokenJson);
        String errcode = getTokenJson.getString("errcode");
        if (ObjectUtil.isNotEmpty(errcode) && ObjectUtil.notEqual(errcode, "0")) {
            String cacheObject = redisUtil.getCacheObject(redisKey + errcode);
            cacheObject = ObjectUtil.isNotEmpty(cacheObject) ? cacheObject : errcode;
            return AjaxResult.error(cacheObject);
        }
        String openid = getTokenJson.getString("openid");
        String access_token = getTokenJson.getString("access_token");
        String refresh_token = getTokenJson.getString("refresh_token");

        // 第五步验证access_token是否失效；展示都不需要
        String vlidTokenUrl = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;

        String vlidTokenResult = HttpUtil.get(vlidTokenUrl);
//        log.info("第五步验证access_token是否失效:{}  vlidTokenResult:{}", vlidTokenUrl, vlidTokenResult);
        JSONObject validTokenJson = JSONObject.parseObject(vlidTokenResult);
        if (!"0".equals(validTokenJson.getString("errcode"))) {
            // 第三步：刷新access_token（如果需要）-----暂时没有使用,参考文档https://mp.weixin.qq.com/wiki，
            String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + openid
                    + "&grant_type=refresh_token&refresh_token=" + refresh_token;
            String refreshTokenResult = HttpUtil.get(refreshTokenUrl);
//            log.info("刷新access_token:{}  vlidTokenResult:{}", refreshTokenUrl, refreshTokenResult);
            JSONObject refreshTokenJson = JSONObject.parseObject(refreshTokenResult);
//            log.info("刷新access_token:{}", refreshTokenJson);
            errcode = getTokenJson.getString("errcode");
            if (ObjectUtil.isNotEmpty(errcode) && ObjectUtil.notEqual(errcode, "0")) {
                String cacheObject = redisUtil.getCacheObject(redisKey + errcode);
                cacheObject = ObjectUtil.isNotEmpty(cacheObject) ? cacheObject : errcode;
                return AjaxResult.error(cacheObject);
            }
            access_token = refreshTokenJson.getString("access_token");
        }
        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        String getUserInfoResult = HttpUtil.get(getUserInfoUrl);
//        log.info("拉取用户信息:{}", getUserInfoResult);
        errcode = getTokenJson.getString("errcode");
        if (ObjectUtil.isNotEmpty(errcode) && ObjectUtil.notEqual(errcode, "0")) {
            String cacheObject = redisUtil.getCacheObject(redisKey + errcode);
            cacheObject = ObjectUtil.isNotEmpty(cacheObject) ? cacheObject : errcode;
            return AjaxResult.error(cacheObject);
        }
//        log.info("拉取用户信息:{}  vlidTokenResult:{}", getUserInfoUrl, getUserInfoResult);
        JSONObject getUserInfoJson = JSONObject.parseObject(getUserInfoResult);

        //可以根据获取的微信信息查询数据库是否存在数据，存在直接登录，不存在自动注册
//        response.sendRedirect(url + "&token=ToKen");//重定向到前端
        redirect(backUrl);
        WxUser wxUser = JSON.parseObject(getUserInfoJson.toJSONString(), WxUser.class);
        saveUserInfo(wxUser);
        return AjaxResult.success("授权成功", getUserInfoJson);
    }

    @PostMapping("/saveWxUserInfo")
    public void saveUserInfo(@RequestBody WxUser wxUser) {
        log.info("更新用户信息:{}", JSON.toJSONString(wxUser));
        wxUserService.update(wxUser, new LambdaQueryWrapper<WxUser>().eq(WxUser::getOpenId, wxUser.getOpenId()));
    }
}
