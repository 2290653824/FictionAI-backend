package com.fiction.fictionai.dao;

import com.fiction.fictionai.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM \"User\" WHERE id = #{id}")
    User getUserById(@Param("id") int id);

    @Select("select * from \"User\" where email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("select * from \"User\"")
    List<User> getAll();

    @Insert("insert into \"User\" (id,email) values(#{id},#{email}))")
    int save(User user);
}