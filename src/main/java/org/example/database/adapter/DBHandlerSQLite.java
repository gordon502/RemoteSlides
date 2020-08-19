package org.example.database.adapter;
import org.example.database.DBHandler;
import org.example.model.NewsTicker;
import org.example.model.Slide;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for connecting with DB.
 */
public class DBHandlerSQLite implements DBHandler {

    private String url;

    public DBHandlerSQLite(String url) {
        this.url = url;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public List<Slide> readSlides() {
        List<Slide> slides = new ArrayList<>();

        try {
            Connection connection = this.connect();
            String query = "select * from slides";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                slides.add(new Slide(rs.getInt(1),
                                     rs.getString(2),
                                     rs.getBoolean(3),
                                     rs.getInt(4)));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return slides;
    }

    public List<NewsTicker> readNewsTickers() {
        List<NewsTicker> newsTickers = new ArrayList<>();

        try {
            Connection connection = this.connect();
            String query = "select * from news_tickers";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                newsTickers.add(new NewsTicker(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3)));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return newsTickers;
    }
}
