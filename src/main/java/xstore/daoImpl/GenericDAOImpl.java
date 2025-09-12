package xstore.daoImpl;

import jakarta.persistence.EntityManager;
import xstore.dao.GenericDAO;
import xstore.util.JpaUtil;

import java.util.List;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */


public record GenericDAOImpl <T>(Class<T> entityClass) implements GenericDAO<T> {

    @Override
    public T save(T entity) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T update(T entity) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T delete(String id) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.remove(em.find(entityClass, id));
            em.getTransaction().commit();
            return em.find(entityClass, id);
        }  catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public T findByID(String id) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.find(entityClass, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findAll() {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            return em.createQuery("SELECT e FROM " + entityClass.getName() + " e", entityClass).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
