package core.service;

import core.dao.impl.MemberDaoImpl;
import core.entity.Member;

import java.util.List;

public class MemberService implements GenericService<Member> {

    MemberDaoImpl memberDao = new MemberDaoImpl();

    @Override
    public void add(Member entity) {
        memberDao.add(entity);
    }

    @Override
    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        memberDao.deleteById(id);
    }

    @Override
    public void update(Member entity) {
        memberDao.update(entity);
    }
}
