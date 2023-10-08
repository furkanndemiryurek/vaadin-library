package core.service;

import core.dao.impl.GenreDaoImpl;
import core.entity.Genre;

import java.util.List;

public class GenreService implements GenericService<Genre> {

    GenreDaoImpl genreDao = new GenreDaoImpl();

    @Override
    public void add(Genre entity) {
        genreDao.add(entity);
    }

    @Override
    public Genre findById(Long id) {
        return genreDao.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        genreDao.deleteById(id);
    }

    @Override
    public void update(Genre entity) {
        genreDao.update(entity);
    }
}
