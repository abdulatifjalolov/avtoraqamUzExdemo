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
public class City extends Base{
    private int id;
    private String name;
    private int regionId;

    public City(ResultSet resultSet){
        this.get(resultSet);
    }
    @Override
    protected void get(ResultSet resultSet) {
        try {
            this.id=resultSet.getInt("id");
            this.name=resultSet.getString("name");
            this.regionId=resultSet.getInt("region_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
