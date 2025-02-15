package top.kwseeker.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.*;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.Test;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

@QuarkusTest
class MyBatisResourceTest {

    @Inject
    UserMapper userMapper;
    @Inject
    BookMapper bookMapper;
    @Inject
    MybatisService mybatisService;
    @Inject
    TransactionManager txManager;

    @Test
    void testGetBooks() {
        int bookId = 1;
        Book book = bookMapper.getBook(bookId);
        System.out.println(book);
    }

    @Test
    void testTransaction() {
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

    /**
     * 编程式事务
     */
    @Test
    void testProgrammaticTransaction() {
        try {
            txManager.begin();

            Book book = new Book();
            book.setId(2);
            book.setTitle("Title2");
            User author = new User();
            author.setId(4);
            author.setName("Author2");
            author.setExternalId(UUID.randomUUID().toString());
            book.setAuthor(author);
            mybatisService.insertBook2(book);

            txManager.commit();
            System.out.println("committed");
        } catch (Exception e) {
            try {
                txManager.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("rollbacked");
        }
    }

    @Test
    void testDuplicatedUniqueKeyInsert() {
        try {
            txManager.begin();

            Book book = new Book();
            book.setId(1);
            book.setTitle("Title2");
            User author = new User();
            author.setId(4);
            author.setName("Author2");
            author.setExternalId(UUID.randomUUID().toString());
            book.setAuthor(author);
            mybatisService.insertBook2(book);

            txManager.commit();
            System.out.println("committed");
        } catch (PersistenceException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                System.out.println("唯一索引冲突， e=" + e);
            } else {
                System.out.println("出现异常，e=" + e);
            }
            try {
                txManager.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            System.out.println("事务执行失败，e=" + e.getMessage());
        }
    }
}