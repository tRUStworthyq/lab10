import dao.ActorDAO;
import db.DatabaseConnection;
import model.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorDAOTest {

    private Connection connectionMock;
    private PreparedStatement statementMock;
    private ResultSet resultSetMock;

    @BeforeEach
    public void init() {
        connectionMock = mock(Connection.class);
        statementMock = mock(PreparedStatement.class);
        resultSetMock = mock(ResultSet.class);
    }

    @Test
    void getActorsByMovieId_ReturnsActors() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getInt("id")).thenReturn(100);
            when(resultSetMock.getString("name")).thenReturn("Leonardo DiCaprio");
            when(resultSetMock.getDate("birth_date")).thenReturn(Date.valueOf(LocalDate.of(1974, 11, 11)));


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsByMovieId(1);


            assertEquals(1, actors.size());
            Actor actor = actors.get(0);
            assertEquals(100, actor.getId());
            assertEquals("Leonardo DiCaprio", actor.getName());
            assertEquals(LocalDate.of(1974, 11, 11), actor.getBirthDate());

            verify(statementMock).setInt(1, 1);
        }
    }

    @Test
    void getActorsWithMinMovies_ReturnsActors() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true, true, false);
            when(resultSetMock.getInt("id")).thenReturn(100, 101);
            when(resultSetMock.getString("name")).thenReturn("Actor1", "Actor2");
            when(resultSetMock.getDate("birth_date")).thenReturn(
                    Date.valueOf(LocalDate.of(1980, 1, 1)),
                    Date.valueOf(LocalDate.of(1985, 2, 2))
            );


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsWithMinMovies(2);


            assertEquals(2, actors.size());
            verify(statementMock).setInt(1, 2);
        }
    }

    @Test
    void getActorsWhoAreDirectors_ReturnsActors() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true, false);
            when(resultSetMock.getInt("id")).thenReturn(200);
            when(resultSetMock.getString("name")).thenReturn("Clint Eastwood");
            when(resultSetMock.getDate("birth_date")).thenReturn(Date.valueOf(LocalDate.of(1930, 5, 31)));


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsWhoAreDirectors();


            assertEquals(1, actors.size());
            Actor actor = actors.get(0);
            assertEquals(200, actor.getId());
            assertEquals("Clint Eastwood", actor.getName());
            assertEquals(LocalDate.of(1930, 5, 31), actor.getBirthDate());
        }
    }


    @Test
    void getActorsByMovieId_WhenNoActors_ReturnsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false);


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsByMovieId(1);


            assertTrue(actors.isEmpty());
        }
    }

    @Test
    void getActorsWithMinMovies_WhenInvalidMinMovies_ReturnsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {

            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false);


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsWithMinMovies(0);


            assertTrue(actors.isEmpty());
        }
    }


    @Test
    void getActorsWhoAreDirectors_WhenNoResults_ReturnsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mocked = mockStatic(DatabaseConnection.class)) {


            mocked.when(DatabaseConnection::getConnection).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false);


            ActorDAO dao = new ActorDAO();
            List<Actor> actors = dao.getActorsWhoAreDirectors();


            assertTrue(actors.isEmpty());
        }
    }
}
