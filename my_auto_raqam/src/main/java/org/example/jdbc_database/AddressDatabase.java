package org.example.jdbc_database;

import org.example.model.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressDatabase extends BaseDatabase<Address>{

    public boolean addAddress(
            String name,
            long cityId
    ) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_address(" + name +", "+cityId+ ")"
                    );

            return resultSet.getBoolean(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null && statement != null){
                try {
                    connection.close();
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public List<Address> getList() {
        Connection connection = null;
        Statement statement = null;
        List<Address> addressList = new ArrayList<>();
        try {
            connection = getConnection();
            statement  = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from address");
            while (resultSet.next()){
                addressList.add(new Address(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null && statement != null){
                try {
                    connection.close();
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return addressList;
    }

    @Override
    public List<Address> getPaginationList(int page, int length) {
        return null;
    }
}
