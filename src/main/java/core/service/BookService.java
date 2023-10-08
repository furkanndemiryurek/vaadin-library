package core.service;

import core.dao.impl.BookDaoImpl;
import core.entity.Book;

import java.util.List;

public class BookService implements GenericService<Book> {

    BookDaoImpl bookDao = new BookDaoImpl();

    @Override
    public void add(Book entity) {
        bookDao.add(entity);
    }

    @Override
    public Book findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        bookDao.deleteById(id);
    }

    @Override
    public void update(Book entity) {
        bookDao.update(entity);
    }
}
