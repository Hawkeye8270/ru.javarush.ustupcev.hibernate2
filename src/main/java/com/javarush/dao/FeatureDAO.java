package com.javarush.dao;

import com.javarush.domain.Feature;
import org.hibernate.SessionFactory;

public class FeatureDAO extends GenereticDAO<Feature> {
    public FeatureDAO(SessionFactory sessionFactory) {
        super(Feature.class, sessionFactory);
    }
}
