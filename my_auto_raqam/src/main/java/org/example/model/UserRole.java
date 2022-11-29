package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRole extends Base{
    private int userId;
    private int roleId;

    public UserRole(ResultSet resultSet) {
        this.get(resultSet);
    }


    @Override
    protected void get(ResultSet resultSet) {
        try {
            this.userId = resultSet.getInt("user_id");
            this.roleId = resultSet.getInt("role_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
