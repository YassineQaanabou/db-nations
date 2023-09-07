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

                        System.out.println("ID:" + id + " - Country: " + nation + " - Region: " + region + " - Continent: " + continent);

                    }
                }
            }

            System.out.println("Choose a country ID:");
            String id = scan.nextLine();

            String countryName = "select c.name \n" +
                    "from countries c \n" +
                    "where c.country_id =" + id + ";";

            String searchLanguage = "select distinct l.`language`\n" +
                    "from languages l \n" +
                    "join country_languages cl on l.language_id = cl.language_id  \n" +
                    "join countries c on c.country_id =cl.country_id \n" +
                    "join country_stats cs on cs.country_id =c.country_id \n" +
                    "where c.country_id =" + id + ";\n";

            String searchStats = "select distinct cs.population ,cs.gdp ,cs.`year` \n" +
                    "from languages l \n" +
                    "join country_languages cl on l.language_id = cl.language_id  \n" +
                    "join countries c on c.country_id =cl.country_id \n" +
                    "join country_stats cs on cs.country_id =c.country_id \n" +
                    "where c.country_id = " + id + "  and cs.`year` = (select max(year) from country_stats cs2);";

            try (PreparedStatement ps = conn.prepareStatement(countryName)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        System.out.println("Details for country: " + name);

                    }
                }
            }

            System.out.print("Languages: ");

            try (PreparedStatement ps = conn.prepareStatement(searchLanguage)) {
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