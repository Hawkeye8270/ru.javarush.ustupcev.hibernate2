package com.javarush.dao;

import com.javarush.domain.Rating;
import org.hibernate.SessionFactory;

public class RatingDAO extends GenereticDAO<Rating> {
    public RatingDAO(SessionFactory sessionFactory) {
        super(Rating.class, sessionFactory);
    }
}
