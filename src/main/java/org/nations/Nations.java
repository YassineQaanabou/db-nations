package org.nations;

import java.sql.*;
import java.util.Scanner;

public class Nations {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "Root6912";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Che nazione vuole cercare?");
            String search = scan.nextLine();


            String sql = "select c.name as nation , c.country_id , r.name as region ,c2.name as continent\n" +
                    "from countries c \n" +
                    "join regions r  on r.region_id =c.region_id \n" +
                    "join continents c2 on c2.continent_id =r.continent_id\n" +
                    "where c.name like '%" + search + "%'" +
                    "order by c.name asc ;";
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