package view;

import controller.GameController;
import model.Game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * View principal usando Swing.
 */
public class MainView extends JFrame {
    private GameController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public MainView(GameController controller) {
        this.controller = controller;
        setTitle("Gerenciamento de Biblioteca de Jogos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem addItem = new JMenuItem("Cadastrar Novo Jogo");
        addItem.addActionListener(e -> showAddGameDialog());
        JMenuItem listItem = new JMenuItem("Listar Jogos");
        listItem.addActionListener(e -> refreshTable(null, null));
        JMenuItem reportPlatform = new JMenuItem("Relatório por Plataforma");
        reportPlatform.addActionListener(e -> showReport("platform"));
        JMenuItem reportGenre = new JMenuItem("Relatório por Gênero");
        reportGenre.addActionListener(e -> showReport("genre"));
        fileMenu.add(addItem);
        fileMenu.add(listItem);
        fileMenu.add(reportPlatform);
        fileMenu.add(reportGenre);
        menuBar.add(fileMenu);

        JMenu filterMenu = new JMenu("Filtros");
        JMenuItem filterGenre = new JMenuItem("Filtrar por Gênero");
        filterGenre.addActionListener(e -> showFilterDialog("genre"));
        JMenuItem filterPlatform = new JMenuItem("Filtrar por Plataforma");
        filterPlatform.addActionListener(e -> showFilterDialog("platform"));
        JMenuItem filterStatus = new JMenuItem("Filtrar por Status");
        filterStatus.addActionListener(e -> showFilterDialog("status"));
        filterMenu.add(filterGenre);
        filterMenu.add(filterPlatform);
        filterMenu.add(filterStatus);
        menuBar.add(filterMenu);

        setJMenuBar(menuBar);

        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Título", "Gênero", "Plataforma", "Ano", "Status", "Nota"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel();
        JButton updateButton = new JButton("Atualizar Jogo");
        updateButton.addActionListener(e -> showUpdateGameDialog());
        JButton deleteButton = new JButton("Remover Jogo");
        deleteButton.addActionListener(e -> deleteSelectedGame());
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable(null, null);
    }

    private void showAddGameDialog() {
        JTextField titleField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField platformField = new JTextField();
        JTextField yearField = new JTextField();
        String[] statuses = {"Jogando", "Concluído", "Wishlist"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        JTextField ratingField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Título:"));
        panel.add(titleField);
        panel.add(new JLabel("Gênero:"));
        panel.add(genreField);
        panel.add(new JLabel("Plataforma:"));
        panel.add(platformField);
        panel.add(new JLabel("Ano de Lançamento:"));
        panel.add(yearField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(new JLabel("Nota (0-10):"));
        panel.add(ratingField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Cadastrar Jogo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Game game = new Game(
                        titleField.getText(),
                        genreField.getText(),
                        platformField.getText(),
                        Integer.parseInt(yearField.getText()),
                        (String) statusCombo.getSelectedItem(),
                        Integer.parseInt(ratingField.getText())
                );
                controller.addGame(game);
                refreshTable(null, null);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateGameDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um jogo para atualizar!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        String genre = (String) tableModel.getValueAt(selectedRow, 2);
        String platform = (String) tableModel.getValueAt(selectedRow, 3);
        int year = (int) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        int rating = (int) tableModel.getValueAt(selectedRow, 6);

        JTextField titleField = new JTextField(title);
        JTextField genreField = new JTextField(genre);
        JTextField platformField = new JTextField(platform);
        JTextField yearField = new JTextField(String.valueOf(year));
        String[] statuses = {"Jogando", "Concluído", "Wishlist"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(status);
        JTextField ratingField = new JTextField(String.valueOf(rating));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Título:"));
        panel.add(titleField);
        panel.add(new JLabel("Gênero:"));
        panel.add(genreField);
        panel.add(new JLabel("Plataforma:"));
        panel.add(platformField);
        panel.add(new JLabel("Ano de Lançamento:"));
        panel.add(yearField);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(new JLabel("Nota (0-10):"));
        panel.add(ratingField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Atualizar Jogo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Game game = new Game(
                        id,
                        titleField.getText(),
                        genreField.getText(),
                        platformField.getText(),
                        Integer.parseInt(yearField.getText()),
                        (String) statusCombo.getSelectedItem(),
                        Integer.parseInt(ratingField.getText())
                );
                controller.updateGame(game);
                refreshTable(null, null);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedGame() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um jogo para remover!");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        controller.deleteGame(id);
        refreshTable(null, null);
    }

    private void refreshTable(String filterType, String filterValue) {
        tableModel.setRowCount(0);
        List<Game> games = controller.getGames(filterType, filterValue);
        for (Game game : games) {
            tableModel.addRow(new Object[]{
                    game.getId(),
                    game.getTitle(),
                    game.getGenre(),
                    game.getPlatform(),
                    game.getReleaseYear(),
                    game.getStatus(),
                    game.getRating()
            });
        }
    }

    private void showFilterDialog(String filterType) {
        String filterValue = JOptionPane.showInputDialog("Digite o valor para filtrar por " + filterType + ":");
        if (filterValue != null) {
            refreshTable(filterType, filterValue);
        }
    }

    private void showReport(String groupBy) {
        Map<String, Integer> report = controller.getCompletedGamesReport(groupBy);
        StringBuilder sb = new StringBuilder("Relatório de Jogos Concluídos por " + groupBy + ":\n");
        for (Map.Entry<String, Integer> entry : report.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}