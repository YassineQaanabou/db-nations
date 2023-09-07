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
            System.out.println("Search:");
            String search = scan.nextLine();


            String sql = """
                    select c.name as nation , c.country_id , r.name as region ,c2.name as continent
                    from countries c\s
                    join regions r  on r.region_id =c.region_id\s
                    join continents c2 on c2.continent_id =r.continent_id
                    where c.name like ?order by c.name asc ;""";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1,"%"+search+"%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("country_id");
                        String nation = rs.getString("nation");
                        String region = rs.getString("region");
                        String continent = rs.getString("continent");

                        System.out.println("ID:" + id + " - Country: " + nation + " - Region: " + region + " - Continent: " + continent);

                    }
                }
            }

            System.out.println("Choose a country ID:");
            int id = Integer.parseInt(scan.nextLine());

            String countryName = """
                    select c.name\s
                    from countries c\s
                    where c.country_id =?;""";

            String searchLanguage = """
                    select distinct l.`language`
                    from languages l\s
                    join country_languages cl on l.language_id = cl.language_id \s
                    join countries c on c.country_id =cl.country_id\s
                    join country_stats cs on cs.country_id =c.country_id\s
                    where c.country_id =?;
                    """;

            String searchStats = """
                    select distinct cs.population ,cs.gdp ,cs.`year`\s
                    from languages l\s
                    join country_languages cl on l.language_id = cl.language_id \s
                    join countries c on c.country_id =cl.country_id\s
                    join country_stats cs on cs.country_id =c.country_id\s
                    where c.country_id = ?  and cs.`year` = (select max(year) from country_stats cs2);""";

            try (PreparedStatement ps = conn.prepareStatement(countryName)) {
                ps.setInt(1,id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        System.out.println("Details for country: " + name);

                    }
                }
            }

            System.out.print("Languages: ");

            try (PreparedStatement ps = conn.prepareStatement(searchLanguage)) {
                ps.setInt(1,id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String language = rs.getString("language");
                        System.out.print(language + ", ");

                    }
                }
            }
            System.out.println(" ");
            System.out.println("Most recent stats");

            try (PreparedStatement ps = conn.prepareStatement(searchStats)) {
                ps.setInt(1,id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int year = rs.getInt("year");
                        int population = rs.getInt("population");
                        long gdp = rs.getLong("gdp");


                        System.out.println("Year: " + year);
                        System.out.println("Population: " + population);
                        System.out.println("GDP: " + gdp);

                    }
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}