import dao.ActorDAO;
import dao.MovieDAO;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import model.Actor;
import model.Movie;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MovieDAO movieDAO = new MovieDAO();
        ActorDAO actorDAO = new ActorDAO();
        try {
            List<Movie> movies = movieDAO.findRecentMovies();
            System.out.println("Фильмы вышедшие на экран в текущем и прошлом году");
            System.out.println(movieTable(movies).render());

            List<Actor> actors = actorDAO.getActorsByMovieId(movies.get(0).getId());
            System.out.println("Актеры снимавшиеся в фильме с ID = " + movies.get(0).getId());
            System.out.println(actorTable(actors).render());

            List<Actor> actors1 = actorDAO.getActorsWhoAreDirectors();
            System.out.println("Актеры, которые были режиссерами хотя бы одного фильма");
            System.out.println(actorTable(actors1).render());

            List<Actor> actors2 = actorDAO.getActorsWithMinMovies(2);
            System.out.println("Актеры, которые снялись минимум в 2-х фильмах");
            System.out.println(actorTable(actors2).render());

            int rows = movieDAO.deleteMoviesOlderThan(2000);
            System.out.println("Удаление фильмов вышедших до 2022 года");
            System.out.println("Удалено строк: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static AsciiTable movieTable(List<Movie> movies) {
        AsciiTable movieTable = new AsciiTable();
        movieTable.addRule();
        movieTable.addRow("ID", "Название", "Год", "Страна", "Режиссер");
        movieTable.addRule();
        for (Movie movie : movies) {
            movieTable.addRow(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getCountry(), movie.getDirector().getName());
        }
        movieTable.addRule();
        movieTable.setTextAlignment(TextAlignment.CENTER);
        return movieTable;
    }

    private static AsciiTable actorTable(List<Actor> actors) {
        AsciiTable actorTable = new AsciiTable();
        actorTable.addRule();
        actorTable.addRow("ID", "Имя", "Дата рождения");
        actorTable.addRule();
        for (Actor actor : actors) {
            actorTable.addRow(actor.getId(), actor.getName(), actor.getBirthDate());
        }
        actorTable.addRule();
        actorTable.setTextAlignment(TextAlignment.CENTER);
        return actorTable;
    }
}