package org.example.jdbc_database;



import org.example.model.AutoNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AvtoNumberDataBase extends BaseDatabase<AutoNumber> {

    public boolean addAutoNumber(
            String post_number,
            int regionNumber,
            int state
    ) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_avto_number(" + post_number + ", " + regionNumber + ", " + state +")"
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
    public List<AutoNumber> getAutoNumberListByRegionName(String name){
        Connection connection=null;
        PreparedStatement statement=null;
        List<AutoNumber> autoNumberList=new ArrayList<>();
        try {
            connection=getConnection();
            statement=connection.prepareStatement("select * from get_auto_number_list_by_region(?)");
            statement.setString(1,name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                autoNumberList.add(new AutoNumber(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
        }
        return autoNumberList;
    }

    @Override
    public List<AutoNumber> getList() {
        Connection connection = null;
        PreparedStatement statement = null;
        List<AutoNumber> avtoNumberList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement("select * from get_auto_number()");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                avtoNumberList.add(new AutoNumber(resultSet));
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
        return avtoNumberList;
    }

    public List<AutoNumber> getPaginationList(int page, int length ) {
        Connection connection = null;
        PreparedStatement statement = null;
        List<AutoNumber> avtoNumberList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement("select * from get_pagination_number_list(?,?)");
            statement.setInt(1,page);
            statement.setInt(2,length);
            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()){
                avtoNumberList.add(new AutoNumber(resultSet));
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
        return avtoNumberList;
    }
}
