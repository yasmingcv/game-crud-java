package model;

/**
 * Modelo representando um Jogo.
 */
public class Game {
    private int id;
    private String title;
    private String genre;
    private String platform;
    private int releaseYear;
    private String status;
    private int rating; // Nota de 0 a 10

    /**
     * Construtor completo.
     * @param id ID do jogo.
     * @param title Título do jogo.
     * @param genre Gênero do jogo.
     * @param platform Plataforma do jogo.
     * @param releaseYear Ano de lançamento.
     * @param status Status do jogo.
     * @param rating Nota do jogo.
     */
    public Game(int id, String title, String genre, String platform, int releaseYear, String status, int rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.status = status;
        this.rating = rating;
    }

    // Construtor sem ID para inserção
    public Game(String title, String genre, String platform, int releaseYear, String status, int rating) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.status = status;
        this.rating = rating;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}