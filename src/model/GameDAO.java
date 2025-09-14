package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO para operações CRUD no banco de dados.
 */
public class GameDAO {
    private static final String DB_URL = "jdbc:sqlite:games.db";

    /**
     * Inicializa o banco de dados, criando a tabela se não existir.
     */
    public GameDAO() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS games (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "genre TEXT," +
                    "platform TEXT," +
                    "release_year INTEGER," +
                    "status TEXT," +
                    "rating INTEGER," +
                    "UNIQUE(title, platform)" +
                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um novo jogo ao banco.
     * @param game Jogo a ser adicionado.
     * @throws SQLException Se houver erro no banco.
     */
    public void addGame(Game game) throws SQLException {
        String sql = "INSERT INTO games (title, genre, platform, release_year, status, rating) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getGenre());
            pstmt.setString(3, game.getPlatform());
            pstmt.setInt(4, game.getReleaseYear());
            pstmt.setString(5, game.getStatus());
            pstmt.setInt(6, game.getRating());
            pstmt.executeUpdate();
        }
    }

    /**
     * Lista todos os jogos, com filtro opcional.
     * @param filterType Tipo de filtro (genre, platform, status).
     * @param filterValue Valor do filtro.
     * @return Lista de jogos.
     * @throws SQLException Se houver erro no banco.
     */
    public List<Game> getGames(String filterType, String filterValue) throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games";
        if (filterType != null && filterValue != null) {
            sql += " WHERE " + filterType + " = ?";
        }
        sql += " ORDER BY title"; // Ordenação por título por padrão

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (filterType != null && filterValue != null) {
                pstmt.setString(1, filterValue);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("platform"),
                        rs.getInt("release_year"),
                        rs.getString("status"),
                        rs.getInt("rating")
                ));
            }
        }
        return games;
    }

    /**
     * Atualiza um jogo no banco.
     * @param game Jogo atualizado.
     * @throws SQLException Se houver erro no banco.
     */
    public void updateGame(Game game) throws SQLException {
        String sql = "UPDATE games SET title = ?, genre = ?, platform = ?, release_year = ?, status = ?, rating = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getGenre());
            pstmt.setString(3, game.getPlatform());
            pstmt.setInt(4, game.getReleaseYear());
            pstmt.setString(5, game.getStatus());
            pstmt.setInt(6, game.getRating());
            pstmt.setInt(7, game.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Remove um jogo pelo ID.
     * @param id ID do jogo.
     * @throws SQLException Se houver erro no banco.
     */
    public void deleteGame(int id) throws SQLException {
        String sql = "DELETE FROM games WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Gera relatório de jogos concluídos por plataforma ou gênero.
     * @param groupBy "platform" ou "genre".
     * @return Mapa com contagens.
     * @throws SQLException Se houver erro no banco.
     */
    public Map<String, Integer> getCompletedGamesReport(String groupBy) throws SQLException {
        Map<String, Integer> report = new HashMap<>();
        String sql = "SELECT " + groupBy + ", COUNT(*) as count FROM games WHERE status = 'Concluído' GROUP BY " + groupBy;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                report.put(rs.getString(groupBy), rs.getInt("count"));
            }
        }
        return report;
    }
}