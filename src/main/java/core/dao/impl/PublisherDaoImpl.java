package core.dao.impl;

import core.dao.PublisherDao;
import core.dao.util.HibernateUtil;
import core.entity.Publisher;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PublisherDaoImpl implements PublisherDao {
    @Override
    public void add(Publisher entity) {
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
    public Publisher findById(Long id) {
        Publisher book;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            book = session.get(Publisher.class, id);
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return book;
    }

    @Override
    public List<Publisher> findAll() {
        List<Publisher> books;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Publisher";
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
        Publisher book = findById(id);
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
    public void update(Publisher entity) {
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
