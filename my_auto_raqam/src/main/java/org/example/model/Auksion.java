package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Auksion extends Base {
    private long id;
    private Date startDate;
    private Date endDate;
    private int state;
    private double initialPrice;
    private double currentPrice;
    private double suggestedPrice;
    private String preNumber;
    private String postNumber;
    private long userId;
    private String name;

    public Auksion(ResultSet resultSet) {
        this.get(resultSet);
    }

    @Override
    protected void get(ResultSet resultSet) {
        try {
            this.startDate = resultSet.getDate("start_date");
            this.suggestedPrice = resultSet.getDouble("suggested_price");
            this.preNumber = resultSet.getString("pre_number");
            this.postNumber = resultSet.getString("post_number");
            this.id = resultSet.getLong("id");
            this.endDate = resultSet.getDate("end_date");
            this.state = resultSet.getInt("state");
            this.initialPrice = resultSet.getDouble("initial_price");
            this.currentPrice = resultSet.getDouble("current_price");
            this.userId = resultSet.getLong("user_id");
            this.name = resultSet.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
