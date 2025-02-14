package top.kwseeker.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
class MyBatisResourceTest {

    @Inject
    UserMapper userMapper;
    @Inject
    BookMapper bookMapper;
    @Inject
    MybatisService mybatisService;

    @Test
    void testGetBooks() {
        int bookId = 1;
        Book book = bookMapper.getBook(bookId);
        System.out.println(book);
    }

    @Test
    void testTransactional() {
        Book book = new Book();
        book.setId(2);
        book.setTitle("Title2");
        User author = new User();
        author.setId(4);
        author.setName("Author2");
        author.setExternalId(UUID.randomUUID().toString());
        book.setAuthor(author);
        mybatisService.insertBook(book);
    }
}