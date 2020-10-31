package com.chixigua.blog.mapper;

import com.chixigua.blog.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user(account_id,name,token,gmt_create,gmt_modified)" +
            "values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified})")
    public int addUser(User user);
}
