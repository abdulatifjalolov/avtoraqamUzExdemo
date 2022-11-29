package org.example.jdbc_database;

import org.example.model.City;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CityDatabase extends BaseDatabase<City> {

    public boolean addCity(String name, Long region_id) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_city(" + name + ", " + region_id + ")"
                    );

            return resultSet.getBoolean(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null && statement != null) {
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
    public List<City> getList() {
        Connection connection = null;
        Statement statement = null;
        List<City> cityList = new ArrayList<>();


        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from city");

            while (resultSet.next()) {
                cityList.add(new City(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null && statement != null) {
                try {
                    connection.close();
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return cityList;
    }

    @Override
    public List<City> getPaginationList(int page, int length) {
        return null;
    }

}
