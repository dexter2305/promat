package com.poople.promat.persistence.ormlite;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

/**
 * Created by innsh on 13-06-2016.
 */
public class SQLiteStore extends AbstractORMLiteStore {
    /*
        Sample SQLite XERIAL driver URL to create a database named "sample-db.dat" in the current directory
        jdbc:sqlite:.\sample-db.dat
     */
    private static final String PROTOCOL = "jdbc:sqlite:";
    private static final String DB_NAME = "promat-db-v1.dat";
    private static String databaseURL = PROTOCOL + DB_NAME;
    private Log logger = LogFactory.getLog(SQLiteStore.class);

    @Override
    protected ConnectionSource getConnectionSource() throws StoreException {
        try {
            JdbcConnectionSource jdbcConnectionSource = new JdbcConnectionSource(databaseURL);
            logger.info("Prepared connection source as '" + jdbcConnectionSource.getUrl() + "'");
            return jdbcConnectionSource;
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }
}
