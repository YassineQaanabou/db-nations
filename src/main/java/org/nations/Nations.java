package org.nations;

import java.sql.*;

public class Nations {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "Root6912";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println(conn.getCatalog());

            String sql = """
                    select c.name as nation , c.country_id , r.name as region ,c2.name as continent
                    from countries c\s
                    join regions r  on r.region_id =c.region_id\s
                    join continents c2 on c2.continent_id =r.continent_id
                    order by c.name asc ;""";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("country_id");
                        String nation = rs.getString("nation");
                        String region = rs.getString("region");
                        String continent = rs.getString("continent");

                        System.out.println(nation + " - ID:" + id + " - " + region + " - " + continent);

                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}