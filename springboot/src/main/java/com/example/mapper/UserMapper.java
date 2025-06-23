package com.example.mapper;


import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作user相关数据接口
*/
public interface UserMapper {


    void insert(User user);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

    List<User> selectAll(User user);

    void updateById(User user);

    void deleteById(Integer id);

    User selectById(Integer id);
}
