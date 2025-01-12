package gamestore.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerQueries {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/gamestore_fx_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection;
    private PreparedStatement selectCustomerByUserName;
    private PreparedStatement selectCustomerByPassword;
    private PreparedStatement insertNewCustomer;

    public CustomerQueries() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

            selectCustomerByUserName = connection.prepareStatement("SELECT * FROM customers WHERE UserName = ?");
            selectCustomerByPassword = connection.prepareStatement("SELECT * FROM customers WHERE Password = ?");
            insertNewCustomer = connection.prepareStatement("INSERT INTO customers (UserName, Password, Email, CCNumber) VALUES (?, ?, ?, ?)");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }

    public List<Customer> getCustomerByUserName(String userName) {
        List<Customer> results = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            selectCustomerByUserName.setString(1, userName);
            resultSet = selectCustomerByUserName.executeQuery();

            while (resultSet.next()) {
                results.add(new Customer(
                        (int) resultSet.getLong("customerID"), // Ensure this matches your database schema
                        resultSet.getString("UserName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getString("CCNumber"),
                        resultSet.getDouble("Balance"))); // Ensure column name and type are correct
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                close();
            }
        }

        return results;
    }

    public List<Customer> getCustomerByPassword(String password) {
        List<Customer> results = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            selectCustomerByPassword.setString(1, password);
            resultSet = selectCustomerByPassword.executeQuery();

            while (resultSet.next()) {
                results.add(new Customer(
                        (int) resultSet.getLong("customerID"), // Ensure this matches your database schema
                        resultSet.getString("UserName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getString("CCNumber"),
                        resultSet.getDouble("Balance"))); // Ensure column name and type are correct
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                close();
            }
        }

        return results;
    }

    public int addCustomer(String userName, String password, String email, String ccNumber) {
        int result = 0;

        try {
            insertNewCustomer.setString(1, userName);
            insertNewCustomer.setString(2, password);
            insertNewCustomer.setString(3, email);
            insertNewCustomer.setString(4, ccNumber);

            result = insertNewCustomer.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            close();
        }

        return result;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
