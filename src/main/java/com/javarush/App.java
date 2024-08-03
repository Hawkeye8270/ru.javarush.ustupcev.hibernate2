package com.javarush;

import com.javarush.dao.*;
import com.javarush.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class App {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FeatureDAO featureDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RatingDAO ratingDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;

    public App() {
        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "password");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.HBM2DDL_AUTO, "validate");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Feature.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rating.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        featureDAO = new FeatureDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        ratingDAO = new RatingDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }

    public static void main(String[] args) {

        App app = new App();

        Customer customer = app.createCustomer();
        app.customerReturnInventory();
        app.customerRentSomeFilm(customer);
        app.newFilm();
    }

    private Customer createCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Store store = storeDAO.getItems(0, 1).get(0);

            City city = cityDAO.getByName("Salala");

            Address address = new Address();
            address.setAddress("1121 Loja Avenue");
            address.setPhone("322-33-22");
            address.setCity(city);
            address.setDistrict("California");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setStore(store);
            customer.setFirstName("LAURA");
            customer.setLastName("MILLER");
            customer.setAddress(address);
            customer.setActive(true);
            customer.setEmail("SHARON.ROBINSON@sakilacustomer.org");
            customerDAO.save(customer);

            session.getTransaction().commit();
            return customer;
        }
    }

    private void newFilm() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().get();
            List<Category> categories = categoryDAO.getItems(0, 5);
            List<Actor> actors = actorDAO.getItems(0, 15);

            Film film = new Film();
            film.setActors(new HashSet<>(actors));
            film.setCategories(new HashSet<>(categories));
            film.setDescription("TTTTTTT");
            film.setLanguage(language);
            film.setOriginalLanguage(language);
            film.setLength((short) 125);
            film.setRentalDuration((byte) 60);
            film.setRating(Rating.NC17);
            film.setRentalRate(BigDecimal.ZERO);
            film.setReplacementCost(BigDecimal.TEN);
            film.setSpecialFeatures(Set.of(Feature.TRAILERS, Feature.DELETED_SCENES, Feature.COMMENTARIES));
            film.setTitle("MyFirstFilm");
            film.setYear(Year.of(2025));
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setFilm(film);
            filmText.setId((short) 3);
            filmText.setDescription("MyFirstFilm");
            filmText.setTitle("MyFirstFilm2");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }

        private void customerRentSomeFilm (Customer customer){
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Film film = filmDAO.getSomeAvailableFilmForRent();
                Store store = storeDAO.getItems(0, 1).get(0);

                Inventory inventory = new Inventory();
                inventory.setFilm(film);
                inventory.setStore(store);
                inventoryDAO.save(inventory);

                Staff staff = store.getStaff();

                Rental rental = new Rental();
                rental.setRentalDate(LocalDateTime.now());
                rental.setCustomer(customer);
                rental.setInventory(inventory);
                rental.setStaff(staff);
                rentalDAO.save(rental);

                Payment payment = new Payment();
                payment.setCustomer(customer);
                payment.setAmount(BigDecimal.valueOf(55.45));
                payment.setStaff(staff);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setRental(rental);
                paymentDAO.save(payment);

                session.getTransaction().commit();
            }
        }

        private void customerReturnInventory () {
            try (Session session = sessionFactory.getCurrentSession()) {
                session.beginTransaction();

                Rental rental = rentalDAO.getAnyUnreturnedInventory();
                rental.setReturnDate(LocalDateTime.now());

                rentalDAO.save(rental);

                session.getTransaction().commit();
            }
        }
    }

