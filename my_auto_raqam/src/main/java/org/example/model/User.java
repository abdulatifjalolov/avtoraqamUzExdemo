package org.example.model;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends Base{

    private long id;
    private String phoneNumber;
    private String fullName;
    private long chatId;
    private String password;
    private boolean isPhysical;

    public User(ResultSet resultSet){
        this.get(resultSet);
    }

    @Override
    protected void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong("id");
            this.chatId = resultSet.getLong("chat_id");
            this.fullName = resultSet.getString("name");
            this.phoneNumber = resultSet.getString("phone_num");
            this.password = resultSet.getString("password_s");
            this.isPhysical = resultSet.getBoolean("is_physical");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
