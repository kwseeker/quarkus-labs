package top.kwseeker.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MybatisService {

    @Inject
    BookMapper bookMapper;
    @Inject
    UserMapper userMapper;

    @Transactional
    public void insertBook(Book book) {
        User author = book.getAuthor();
        userMapper.createUser(author.getId(), author.getName(), author.getExternalId());
        bookMapper.createBook(book.getId(), book.getTitle(), author.getId());
        throw new RuntimeException("mock exception");
    }

    public void insertBook2(Book book) {
        User author = book.getAuthor();
        userMapper.createUser(author.getId(), author.getName(), author.getExternalId());
        bookMapper.createBook(book.getId(), book.getTitle(), author.getId());
        throw new RuntimeException("mock exception");
    }
}
