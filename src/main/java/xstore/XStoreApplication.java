package xstore;


import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import xstore.dao.GenericDAO;
import xstore.daoImpl.GenericDAOImpl;
import xstore.model.Product;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */

public class XStoreApplication {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("xstore");
        GenericDAO<Product> dao = new GenericDAOImpl<>(Product.class);
        Product p = (Product) dao.findByID("1");
            System.out.println(p.toString());
        emf.close();
    }
}
