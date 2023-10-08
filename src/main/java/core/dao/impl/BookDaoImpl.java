package core.dao.impl;

import core.dao.BookDao;
import core.dao.util.HibernateUtil;
import core.entity.Book;
import core.service.AuthorService;
import core.service.GenreService;
import core.service.PublisherService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {


    @Override
    public void add(Book entity) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Book findById(Long id) {
        Book book;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            book = session.get(Book.class, id);
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Book";
            Query query = session.createQuery(hql);
            books = query.list();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return books;
    }

    @Override
    public void deleteById(Long id) {
        Book book = findById(id);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(book);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Book entity) {

    }
}
