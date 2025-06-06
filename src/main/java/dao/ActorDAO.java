package dao;


import db.DatabaseConnection;
import model.Actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {
    public List<Actor> getActorsByMovieId(int movieId) throws SQLException {
        String sql = """
            SELECT a.* FROM actors a
            JOIN movie_actors ma ON a.id = ma.actor_id
            WHERE ma.movie_id = ?
            """;
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, movieId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("id"));
                actor.setName(rs.getString("name"));
                actor.setBirthDate(rs.getDate("birth_date").toLocalDate());
                actors.add(actor);
            }
        }
        return actors;
    }

    public List<Actor> getActorsWithMinMovies(int minMovies) throws SQLException {
        String sql = """
            SELECT a.* FROM actors a
            JOIN movie_actors ma ON a.id = ma.actor_id
            GROUP BY a.id HAVING COUNT(ma.movie_id) >= ?
            """;
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, minMovies);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("id"));
                actor.setName(rs.getString("name"));
                actor.setBirthDate(rs.getDate("birth_date").toLocalDate());
                actors.add(actor);
            }
        }
        return actors;
    }

    public List<Actor> getActorsWhoAreDirectors() throws SQLException {
        String sql = """
            SELECT a.* FROM actors a
            JOIN directors d ON a.id = d.actor_id
            """;
        List<Actor> actors = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt("id"));
                actor.setName(rs.getString("name"));
                actor.setBirthDate(rs.getDate("birth_date").toLocalDate());
                actors.add(actor);
            }
        }
        return actors;
    }
}
