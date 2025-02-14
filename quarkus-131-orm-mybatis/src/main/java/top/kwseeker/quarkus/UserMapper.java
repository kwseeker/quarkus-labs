package top.kwseeker.quarkus;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cursor.Cursor;

@Mapper
@CacheNamespace(readWrite = false)
public interface UserMapper {

    @Select("select * from users where id = #{id}")
    User getUser(Integer id);

    @Insert("insert into users (id, name, externalId) values (#{id}, #{name}, #{externalId})")
    Integer createUser(@Param("id") Integer id, @Param("name") String name, @Param("externalId") String externalId);

    @Delete("delete from users where id = #{id}")
    Integer removeUser(Integer id);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    User selectOne(Integer id);

    @Select("select count(*) from users")
    int getUserCount();

    @Select("select name from users")
    Cursor<String> selectCursor();

    User findById(Integer id);
}
