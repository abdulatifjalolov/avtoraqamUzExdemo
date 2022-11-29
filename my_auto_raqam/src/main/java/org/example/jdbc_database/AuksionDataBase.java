package org.example.jdbc_database;

import org.example.model.Auksion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuksionDataBase extends BaseDatabase<Auksion> {
    public boolean addAuksion(
            Date startDate,
            Date endDate,
            int state,
            double initialPrice,
            double currentPrice,
            long avtoNumberId
    ) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select add_auksion(" + startDate + ", " + endDate + ", " + state + ", " + initialPrice + ", " + currentPrice + ", " + avtoNumberId + ")"
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


    public List<Auksion> searchAuksion(String preNumber,String postNumber1,String postNumber2,String postNumber3,String postNumber4,String postNumber5,String postNumber6){
        Connection connection=null;
        PreparedStatement statement=null;
        List<Auksion> auksionList=new ArrayList<>();
        try {
            connection=getConnection();
            statement=connection.prepareStatement("select * from get_auksion_list(?,?,?,?,?,?,?)");
            statement.setString(1,preNumber);
            statement.setString(2,postNumber1);
            statement.setString(3,postNumber2);
            statement.setString(4,postNumber3);
            statement.setString(5,postNumber4);
            statement.setString(6,postNumber5);
            statement.setString(7,postNumber6);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                auksionList.add(new Auksion(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
        }
        return auksionList;
    }
    public Auksion getAuksion(String preNumber,String postNumber){
        Connection connection=null;
        Statement statement=null;
        Auksion auksion=null;
        try {
            connection=getConnection();
            statement=connection.createStatement();
            ResultSet resultSet=
                    statement.executeQuery(
                            "select * from get_auksion_info(" + forSql(preNumber) + ", " + forSql(postNumber) + ")"
                    );
            resultSet.next();
            auksion=new Auksion(resultSet);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
            closeConnections(connection,statement);
        }
        return auksion;
    }

    public boolean sale(Long userId,Integer auksionId,Double newPrice){
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            connection=getConnection();
            statement=connection.prepareStatement("select * from sale(?,?,?)");
            statement.setLong(1,userId);
            statement.setInt(2,auksionId);
            statement.setDouble(3,newPrice);
            ResultSet resultSet=statement.executeQuery();
            return resultSet.getBoolean(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
        }
    }


    @Override
    public List<Auksion> getList() {
        Connection connection = null;
        Statement statement = null;
        List<Auksion> auksionList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select * from auksion");

            while (resultSet.next()){
                auksionList.add(new Auksion(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
        }
        return auksionList;
    }


    @Override
    public List<Auksion> getPaginationList(int page, int length) {
        return null;
    }
}
