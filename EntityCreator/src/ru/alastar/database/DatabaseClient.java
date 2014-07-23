package ru.alastar.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.alastar.main.MainClass;

public class DatabaseClient
{
    public static Connection conn = null;

    public static boolean Start() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException
    { 
        MainClass.Log("[Validator]","Establishing MySQL connection...");
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost/"
                    + MainClass.dbName + "?" + "user="
                    + MainClass.dbUser + "&password="
                    + MainClass.dbPass + "");
            return true;
        } catch (SQLException ex)
        {
            MainClass.Log("[DATABASE]", ex.getMessage());
            MainClass.Log("[ERROR]", "SQLException: " + ex.getMessage());
            MainClass.Log("[ERROR]", "SQLState: " + ex.getSQLState());
            MainClass.Log("[ERROR]", "VendorError: " + ex.getErrorCode());
            return false;
        }
    }

    public static ResultSet commandExecute(String cmd)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = conn.prepareStatement(cmd);

            if (stmt.execute(cmd))
            {
                rs = stmt.getResultSet();
            }

            return rs;
        } catch (SQLException ex)
        {
            MainClass.Log("[ERROR]", "SQLException: " + ex.getMessage());
            MainClass.Log("[ERROR]", "SQLState: " + ex.getSQLState());
            MainClass.Log("[ERROR]", "VendorError: " + ex.getErrorCode());
            return null;
        }
    }

}
