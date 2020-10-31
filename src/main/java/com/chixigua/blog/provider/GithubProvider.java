package com.chixigua.blog.provider;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.chixigua.blog.dto.AccessTokenDTO;
import com.chixigua.blog.dto.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {
    private static final Logger log = LoggerFactory.getLogger(GithubProvider.class);

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //将对象解析为json对象，作为请求体
        String message = JSON.toJSONString(accessTokenDTO);
        String tokenStr = HttpRequest.post("https://github.com/login/oauth/access_token").body(message).execute().body();
        String token = tokenStr.split("&")[0].split("=")[1];
        log.info("token为:{}",token);
        return token;
    }

    public GithubUser getUser(String accessToken) {
        String body = HttpRequest.post("https://api.github.com/user").header("Authorization","token "+accessToken).execute().body();
        GithubUser githubUser = JSON.parseObject(body, GithubUser.class);
        log.info("用户信息为,githubUser:{}",githubUser);
        return githubUser;
    }
}
