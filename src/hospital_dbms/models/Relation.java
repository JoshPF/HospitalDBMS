package hospital_dbms.models;

import java.sql.Connection;
import java.sql.SQLException;

import static hospital_dbms.HospitalDBMS.getConnection;

/**
 * Base class for a table in the database.
 * Defines operations that all relations will support
 * and stores the connection instance
 */
public abstract class Relation {

    /** SQL Connection object */
    protected static Connection connection;

    /** Base constructor. Obtains connection */
    public Relation() {
        this.connection = getConnection();
    }

    /**
     * Inserts the object into the database by running an INSERT SQL statement
     * @throws SQLException if any SQL error occurs
     */
    abstract void insert() throws SQLException;

    /**
     * Updates the database to reflect the object's state by running an UPDATE.
     * @throws SQLException if any SQL error occurs
     */
    abstract void update() throws SQLException;
}
