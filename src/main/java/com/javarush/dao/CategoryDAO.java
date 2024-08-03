package com.javarush.dao;

import com.javarush.domain.Category;
import org.hibernate.SessionFactory;

public class CategoryDAO extends GenereticDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
