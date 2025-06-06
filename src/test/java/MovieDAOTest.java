import dao.MovieDAO;
import db.DatabaseConnection;
import model.Director;
import model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieDAOTest {

    @Test
    void findRecentMovies_ReturnsMovies() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {
            Connection connectionMock = mock(Connection.class);
            PreparedStatement statementMock = mock(PreparedStatement.class);
            ResultSet resultSetMock = mock(ResultSet.class);

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            int currentYear = Year.now().getValue();
            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getInt("id")).thenReturn(1);
            when(resultSetMock.getString("title")).thenReturn("Inception");
            when(resultSetMock.getInt("release_year")).thenReturn(currentYear - 1);
            when(resultSetMock.getString("country")).thenReturn("USA");
            when(resultSetMock.getInt("d_id")).thenReturn(10);
            when(resultSetMock.getString("d_name")).thenReturn("Christopher Nolan");
            when(resultSetMock.getDate("d_birth_date")).thenReturn(Date.valueOf(LocalDate.of(1970, 7, 30)));


            MovieDAO dao = new MovieDAO();
            List<Movie> movies = dao.findRecentMovies();

            assertEquals(1, movies.size());
            Movie movie = movies.get(0);
            assertEquals(1, movie.getId());
            assertEquals("Inception", movie.getTitle());
            assertEquals(currentYear - 1, movie.getReleaseYear().getValue());
            assertEquals("USA", movie.getCountry());

            Director director = movie.getDirector();
            assertEquals(10, director.getId());
            assertEquals("Christopher Nolan", director.getName());
            assertEquals(LocalDate.of(1970, 7, 30), director.getBirthDate());

            verify(statementMock).setInt(1, currentYear - 1);
        }
    }

    @Test
    void deleteMoviesOlderThan_ExecutesDelete() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            Connection connectionMock = mock(Connection.class);
            PreparedStatement statementMock = mock(PreparedStatement.class);

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeUpdate()).thenReturn(5);


            MovieDAO dao = new MovieDAO();
            int deletedRows = dao.deleteMoviesOlderThan(2000);


            assertEquals(5, deletedRows);
            verify(statementMock).setInt(1, 2000);
        }
    }

    @Test
    void findRecentMovies_WhenNoResults_ReturnsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            Connection connectionMock = mock(Connection.class);
            PreparedStatement statementMock = mock(PreparedStatement.class);
            ResultSet resultSetMock = mock(ResultSet.class);

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false);


            MovieDAO dao = new MovieDAO();
            List<Movie> movies = dao.findRecentMovies();


            assertTrue(movies.isEmpty());
        }
    }


    @Test
    void deleteMoviesOlderThan_WhenNoRowsAffected_ReturnsZero() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            Connection connectionMock = mock(Connection.class);
            PreparedStatement statementMock = mock(PreparedStatement.class);

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeUpdate()).thenReturn(0);


            MovieDAO dao = new MovieDAO();
            int rows = dao.deleteMoviesOlderThan(2000);


            assertEquals(0, rows);
        }
    }
}
