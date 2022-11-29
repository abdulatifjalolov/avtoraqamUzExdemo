package org.example.jdbc_database;

import org.example.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase extends BaseDatabase<User> {

//    private final static String ADD_USER = "select add_user()";

    public boolean addUser(
            long chatId,
            String fullName,
            String phoneNumber,
            String password,
            long addressId
    ) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                    "select add_user(" + forSql(fullName) + ", " + forSql(password) + "," + forSql(phoneNumber) + ", " + true + ", " + 1 + ", " + chatId + ")"
            );
            resultSet.next();
            return resultSet.getBoolean(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            closeConnections(connection,statement);
        }
    }
    public boolean checkUser(
            Long chatId
    ){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select checkuser(" + chatId + ")"
                    );
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally  {
            closeConnections(connection,statement);
        }
        return false;
    }



    @Override
    public List<User> getList() {
        Connection connection = null;
        Statement statement = null;
        List<User> userList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(
                            "select * from av_user");

            while (resultSet.next()){
                userList.add(new User(resultSet));
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
        return userList;
    }

    @Override
    public List<User> getPaginationList(int page, int length) {
        return null;
    }
}
