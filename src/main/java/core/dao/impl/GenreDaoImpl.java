package core.dao.impl;

import core.dao.GenreDao;
import core.dao.util.HibernateUtil;
import core.entity.Genre;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GenreDaoImpl implements GenreDao {
    @Override
    public void add(Genre entity) {
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
    public Genre findById(Long id) {
        Genre genre;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            genre = session.get(Genre.class, id);
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return genre;
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Genre";
            Query query = session.createQuery(hql);
            genres = query.list();
            session.close();;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return genres;
    }

    @Override
    public void deleteById(Long id) {
        Genre genre = findById(id);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(genre);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Genre entity) {
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
