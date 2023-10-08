package core.service;

import core.dao.impl.AuthorDaoImpl;
import core.entity.Author;

import java.util.List;

public class AuthorService implements GenericService<Author> {

    AuthorDaoImpl authorDao = new AuthorDaoImpl();

    @Override
    public void add(Author entity) {
        authorDao.add(entity);
    }

    @Override
    public Author findById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        authorDao.deleteById(id);
    }

    @Override
    public void update(Author entity) {
        authorDao.update(entity);
    }
}
