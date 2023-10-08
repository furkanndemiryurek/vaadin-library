package core.dao.impl;

import core.dao.MemberDao;
import core.dao.util.HibernateUtil;
import core.entity.Member;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDaoImpl implements MemberDao {
    @Override
    public void add(Member entity) {
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
    public Member findById(Long id) {
        Member member;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            member = session.get(Member.class, id);
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return member;
    }

    @Override
    public List<Member> findAll() {
        List<Member> members;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Member";
            Query query = session.createQuery(hql);
            members = query.list();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return members;
    }

    @Override
    public void deleteById(Long id) {
        Member member = findById(id);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(member);
            transaction.commit();
            session.close();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Member entity) {
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