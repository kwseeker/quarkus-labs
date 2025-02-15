package top.kwseeker.quarkus;

import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace
public interface BookMapper {

    @Insert("insert into books (id, title, author_id) values (#{id}, #{title}, #{author_id})")
    Integer createBook(@Param("id") Integer id, @Param("title") String title, @Param("author_id") Integer authorId);

    @Select("SELECT id, author_id, title from books where id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author", column = "author_id", javaType = User.class, one = @One(select = "getUser"))
    })
    Book getBook(Integer id);

    @Select("select * from users where id = #{id}")
    User getUser(Integer id);

    Book findById(Integer id);
}
