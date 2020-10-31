package com.chixigua.blog.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {

    private String name; //登录名字
    private Long id;
    private String bio;//个人介绍
    private String avatarUrl;
}
