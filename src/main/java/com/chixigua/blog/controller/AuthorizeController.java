package com.chixigua.blog.controller;

        import com.chixigua.blog.dto.AccessTokenDTO;
        import com.chixigua.blog.dto.GithubUser;
        import com.chixigua.blog.provider.GithubProvider;
        import com.chixigua.blog.provider.GithubProvider1;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider1 gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private  String clientSecret;
    @Value("{github.redirect.url}")
    private String redirectUrl;
    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state){
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
        GithubUser user = gitHubProvider.getUser(accessToken);
        System.out.println(user);
        return "index";
    }
}
