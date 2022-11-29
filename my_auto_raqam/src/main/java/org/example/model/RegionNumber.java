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
public class RegionNumber extends Base{
    private int id;
    private String preNumber;
    private int regionId;


    public RegionNumber(ResultSet resultSet){
        this.get(resultSet);
    }

    @Override
    protected void get(ResultSet resultSet) {
        try {
            this.preNumber = resultSet.getString("pre_number");
            this.regionId = resultSet.getInt("region_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
