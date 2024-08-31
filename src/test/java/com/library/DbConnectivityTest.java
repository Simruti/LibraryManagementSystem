package com.library;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DbConnectivityTest {
    @Test
    public void testConnection()
    {
        try{
            assertNotNull(DbConnectivity.getConnection(), "Connection should not be null");
        }catch(SQLException e)
        {
            assert false : "SQLException occurred: " + e.getMessage();
        }

    }
}
