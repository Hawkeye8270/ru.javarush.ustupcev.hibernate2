package com.javarush.dao;

import com.javarush.domain.Film;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class FilmDAO extends GenereticDAO<Film> {
    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }

    public Film getSomeAvailableFilmForRent() {
        Query<Film> query = getCurrentSession().createQuery("SELECT f FROM Film f " +
                "WHERE f.id NOT IN (SELECT DISTINCT film.id FROM Inventory)", Film.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
