package xstore.dao;

import java.util.List;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */

public interface  GenericDAO <T> {
    public T save(T entity);
    public T update(T  entity);
    public T delete(String id);
    public T findByID(String id);
    public List<T> findAll();
}
