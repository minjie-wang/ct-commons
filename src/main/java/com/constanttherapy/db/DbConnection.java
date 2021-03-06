package com.constanttherapy.db;

import com.constanttherapy.util.CTLogger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by madvani on 7/25/16.
 */
public abstract class DbConnection
{
    protected Connection sql = null;
    protected String dbInstance;

    protected void init(String dbName) throws SQLException
    {
        if (this.sql == null)
            this.sql = SQLUtil.getDatabaseConnection(dbName);
    }

    public boolean isValid() throws SQLException
    {
        return (this.sql != null && !this.sql.isClosed());
    }

    public void close()
    {
        SQLUtil.closeQuietly(this.sql);
    }

    public SqlPreparedStatement prepareStatement(String query) throws SQLException
    {
        try
        {
            return new SqlPreparedStatement(this.sql.prepareStatement(query), this.dbInstance);
        }
        catch (SQLException ex)
        {
            CTLogger.debug(String.format("[%s] (ERROR): %s", this.dbInstance, query));
            throw ex;
        }
    }

    public SqlPreparedStatement prepareStatement(String query, int autoGeneratedKeys)
            throws SQLException
    {
        try
        {
            return new SqlPreparedStatement(this.sql.prepareStatement(query, autoGeneratedKeys), this.dbInstance);
        }
        catch (SQLException ex)
        {
            CTLogger.debug(String.format("[%s] (ERROR): %s", this.dbInstance, query));
            throw ex;
        }
    }

    public boolean isClosed() throws SQLException
    {
        return this.sql.isClosed();
    }

    /**
     * Checks to see whether the connection is read-write. If not, then creates and returns a read-write connection
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ReadWriteDbConnection getReadWriteDbConnection(DbConnection sql) throws SQLException
    {
        if (sql instanceof ReadWriteDbConnection)
            return (ReadWriteDbConnection) sql;
        else
            return new ReadWriteDbConnection();
    }

    /**
     * Checks to whether the connection is readonly. If not, creates and returns a readonly connection.
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ReadOnlyDbConnection getReadOnlyDbConnection(DbConnection sql) throws SQLException
    {
        if (sql instanceof ReadOnlyDbConnection)
            return (ReadOnlyDbConnection) sql;
        else
            return new ReadOnlyDbConnection();
    }

    public CallableStatement prepareCall(String s) throws SQLException
    {
        return this.sql.prepareCall(s);
    }
}
