package core.service;

import core.dao.impl.PublisherDaoImpl;
import core.entity.Publisher;

import java.util.List;

public class PublisherService implements GenericService<Publisher> {

    PublisherDaoImpl publisherDao = new PublisherDaoImpl();

    @Override
    public void add(Publisher entity) {
        publisherDao.add(entity);
    }

    @Override
    public Publisher findById(Long id) {
        return publisherDao.findById(id);
    }

    @Override
    public List<Publisher> findAll() {
        return publisherDao.findAll();
    }

    @Override
    public void deleteById(Long id) {
        publisherDao.deleteById(id);
    }

    @Override
    public void update(Publisher entity) {
        publisherDao.update(entity);
    }
}
