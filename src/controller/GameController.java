package controller;

import model.Game;
import model.GameDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Controller que gerencia a lógica de negócio e validações.
 */
public class GameController {
    private GameDAO dao;

    public GameController() {
        this.dao = new GameDAO();
    }

    /**
     * Adiciona um jogo, validando regras.
     * @param game Jogo a adicionar.
     */
    public void addGame(Game game) {
        try {
            validateGame(game);
            dao.addGame(game);
            JOptionPane.showMessageDialog(null, "Jogo adicionado com sucesso!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(null, "Já existe um jogo com o mesmo título e plataforma!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (e.getMessage().contains("no such table") || e.getMessage().contains("database")) {
                JOptionPane.showMessageDialog(null, "Falha na conexão com o banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao adicionar jogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lista jogos com filtro.
     * @param filterType Tipo de filtro.
     * @param filterValue Valor.
     * @return Lista de jogos.
     */
    public java.util.List<Game> getGames(String filterType, String filterValue) {
        try {
            return dao.getGames(filterType, filterValue);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Falha na conexão com o banco de dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    /**
     * Atualiza jogo, validando.
     * @param game Jogo atualizado.
     */
    public void updateGame(Game game) {
        try {
            validateGame(game);
            dao.updateGame(game);
            JOptionPane.showMessageDialog(null, "Jogo atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(null, "Já existe um jogo com o mesmo título e plataforma!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar jogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Remove jogo.
     * @param id ID do jogo.
     */
    public void deleteGame(int id) {
        try {
            dao.deleteGame(id);
            JOptionPane.showMessageDialog(null, "Jogo removido com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover jogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gera relatório.
     * @param groupBy Agrupamento.
     * @return Mapa do relatório.
     */
    public Map<String, Integer> getCompletedGamesReport(String groupBy) {
        try {
            return dao.getCompletedGamesReport(groupBy);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return new java.util.HashMap<>();
        }
    }

    /**
     * Valida os dados do jogo.
     * @param game Jogo a validar.
     * @throws IllegalArgumentException Se inválido.
     */
    private void validateGame(Game game) {
        if (game.getTitle() == null || game.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório!");
        }
        if (game.getReleaseYear() > 2025) { // Ano atual como exemplo; ajuste para data real
            throw new IllegalArgumentException("Ano de lançamento não pode ser no futuro!");
        }
        if (game.getRating() < 0 || game.getRating() > 10) {
            throw new IllegalArgumentException("Nota deve ser entre 0 e 10!");
        }
    }
}