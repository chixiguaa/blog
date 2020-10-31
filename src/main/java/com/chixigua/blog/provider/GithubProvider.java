package com.chixigua.blog.provider;

import com.alibaba.fastjson.JSON;
import com.chixigua.blog.dto.AccessTokenDTO;
import com.chixigua.blog.dto.GithubUser;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    private static final Logger log = LoggerFactory.getLogger(GithubProvider.class);

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //以什么类型发送消息
        MediaType mediaType = MediaType.get("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        //将accessTokenDTO对象转化为json字符串，作为请求体
        String message = JSON.toJSONString(accessTokenDTO);
        RequestBody requestBody = RequestBody.create(message,mediaType);

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String body =  response.body().string();
            //body:access_token=00c43b98980f445406e3a780ba8850e0d8d9e2f6&scope=user&token_type=bearer
            String token = body.split("&")[0].split("=")[1];
            System.out.println("token为:"+token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println("user.string:"+string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
