package org.example.jdbc_database;

import org.example.model.RegionNumber;
import org.example.model.User;

import javax.swing.plaf.synth.Region;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegionNumberDatabase extends BaseDatabase<RegionNumber>{

    public boolean add_region_number(
            String preNumber,
            int regionId
    ){

        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_region_number(" + preNumber + ", " + regionId + ")"
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
    public List<RegionNumber> getList() {
        Connection connection = null;
        Statement statement = null;
        List<RegionNumber> region_numberList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select * from region_number");

            while (resultSet.next()){
                region_numberList.add(new RegionNumber(resultSet));
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
        return region_numberList;
    }

    @Override
    public List<RegionNumber> getPaginationList(int page, int length) {
        return null;
    }
}
