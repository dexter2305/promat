package com.poople.promat.persistence.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.poople.promat.models.Candidate;
import com.poople.promat.persistence.IStore;
import com.poople.promat.resources.Filter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by innsh on 13-06-2016.
 */
public abstract class AbstractORMLiteStore implements IStore {

    private volatile boolean storeInitialized;
    protected abstract ConnectionSource getConnectionSource() throws StoreException;
    private Dao<Candidate, Long> candidateDao;
    private static Log logger  = LogFactory.getLog(AbstractORMLiteStore.class);

    @Override
    public void setup() throws StoreException {
        try {
            int tableIfNotExists = TableUtils.createTableIfNotExists(getConnectionSource(), Candidate.class);
            logger.info("DDL completed.");
            candidateDao = DaoManager.createDao(getConnectionSource(), Candidate.class);
            logger.info("Dao prepared " + candidateDao.toString());

            storeInitialized = true;
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public void cleanUp() throws StoreException {
        try {
            getConnectionSource().close();
        } catch (SQLException e) {
            logger.error("Error while closing connection source.");
        }
    }

    @Override
    public void create(Candidate candidate) throws StoreException {
        try {
            candidateDao.create(candidate);
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    @Override
    public void update(Candidate candidate) throws StoreException {

    }

    @Override
    public void delete(long candidateId) throws StoreException {

    }

    @Override
    public Candidate read(long candidateId) throws StoreException {
        return null;
    }

    @Override
    public Collection<Candidate> readAll() throws StoreException {
        return null;
    }

    @Override
    public Collection<Candidate> apply(Filter filter) throws StoreException {
        return null;
    }
}
