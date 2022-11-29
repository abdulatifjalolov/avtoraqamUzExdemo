package org.example.jdbc_database;

import org.example.model.Region;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegionDatabase extends BaseDatabase<Region>{

    public boolean addRegion(
            String name
    ) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_region(" + name +  ")"
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
    public List<Region> getList() {
        Connection connection = null;
        Statement statement = null;
        List<Region> regionList = new ArrayList<>();
        try {
            connection = getConnection();
            statement  = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from region");
            while (resultSet.next()){
                regionList.add(new Region(resultSet));
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
        return regionList;
    }

    @Override
    public List<Region> getPaginationList(int page, int length) {
        return null;
    }
}
