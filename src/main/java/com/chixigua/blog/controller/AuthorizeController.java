package com.chixigua.blog.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.chixigua.blog.dto.AccessTokenDTO;
import com.chixigua.blog.dto.GithubUser;
import com.chixigua.blog.mapper.UserMapper;
import com.chixigua.blog.model.User;
import com.chixigua.blog.provider.GithubProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeController.class);
    @Autowired
    private GithubProvider gitHubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private  String clientSecret;
    @Value("{github.redirect.url}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request){
        //拿着向github请求的code，去申请token
        //先把参数封装为一个对象，方便管理
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_url(redirectUrl);
        accessTokenDTO.setState(state);
        //发送请求，获取token
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        //带着token，向github请求用户信息
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        if(githubUser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString().replace("-",""));
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.addUser(user);
            log.info("[登录成功]，设置session");
            request.getSession().setAttribute("user",user);
            return "redirect:/";
        }
        log.info("[登录失败]，重定向回index页面");
        return "redirect:/";
    }
}
