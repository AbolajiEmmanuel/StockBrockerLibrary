/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizblock.library.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

/**
 *
 * @author Praise
 */
public final class DBConfiguration implements AutoCloseable
{
    public static EntityManagerFactory remoteEntityManagerFactory;

    private EntityManager entityManager;

    public DBConfiguration()
    {
        this.entityManager = createEntityManager();
    }

    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    public static EntityManager createEntityManager()
    {
        try
        {
            return remoteEntityManagerFactory.createEntityManager();
        }
        catch(PersistenceException xcp)
        {
            throw new RuntimeException(xcp);
        }
    }

    @Override
    public void close() throws Exception
    {
        this.entityManager.close();
    }
}
