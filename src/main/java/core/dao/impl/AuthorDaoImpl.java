package core.dao.impl;

import core.dao.AuthorDao;
import core.dao.util.HibernateUtil;
import core.entity.Author;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {
    @Override
    public void add(Author entity) {
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
    public Author findById(Long id) {
        Author author;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            author = session.get(Author.class, id);
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return author;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Author";
            Query query = session.createQuery(hql);
            authors = query.list();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return authors;
    }

    @Override
    public void deleteById(Long id) {
        Author author = findById(id);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(author);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Author entity) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
