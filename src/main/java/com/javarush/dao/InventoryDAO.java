package com.javarush.dao;

import com.javarush.domain.Inventory;
import org.hibernate.SessionFactory;

public class InventoryDAO extends GenereticDAO<Inventory> {
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }
}
