package com.javarush.dao;

import com.javarush.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class RentalDAO extends GenereticDAO<Rental> {
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public Rental getAnyUnreturnedInventory() {
        Query<Rental> query = getCurrentSession().createQuery("SELECT r FROM Rental r WHERE r.returnDate IS NULL", Rental.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
