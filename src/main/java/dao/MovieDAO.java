package dao;

import db.DatabaseConnection;
import model.Director;
import model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public List<Movie> findRecentMovies() throws SQLException {
        String sql = """ 
                SELECT m.*, d.id AS d_id, d.name AS d_name, d.birth_date AS d_birth_date\s
                FROM movies m
                JOIN directors d ON m.director_id = d.id
                WHERE release_year >= ?
                """;
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int currentYear = Year.now().getValue();
            statement.setInt(1, currentYear - 1);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Director director = new Director();
                director.setId(resultSet.getInt("d_id"));
                director.setName(resultSet.getString("d_name"));
                director.setBirthDate(resultSet.getDate("d_birth_date").toLocalDate());

                Movie movie = new Movie();
                movie.setId(resultSet.getInt("id"));
                movie.setTitle(resultSet.getString("title"));
                movie.setReleaseYear(Year.of(resultSet.getInt("release_year")));
                movie.setCountry(resultSet.getString("country"));
                movie.setDirector(director);
                movies.add(movie);
            }
        }
        return movies;
    }

    public int deleteMoviesOlderThan(int years) throws SQLException {
        String sql = """
                DELETE FROM movies WHERE release_year < ?
                """;
        int rows;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, years);
            rows = statement.executeUpdate();
        }

        return rows;
    }
}
