package model;

public class Musica {
    private int id;
    private String artist;
    private String song;
    private int durationMs;
    private int year;
    private int popularity;
    private String genre;
    private String subgenero;
    private String subgenero2;
    private String subgenero3;
    private String subgenero4;

    public Musica() {
    }

    public Musica(int id, String artist, String song, int durationMs, int year, int popularity, String genre, String subgenero, String subgenero2, String subgenero3, String subgenero4) {
        this.id = id;
        this.artist = artist;
        this.song = song;
        this.durationMs = durationMs;
        this.year = year;
        this.popularity = popularity;
        this.genre = genre;
        this.subgenero = subgenero;
        this.subgenero2 = subgenero2;
        this.subgenero3 = subgenero3;
        this.subgenero4 = subgenero4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSubgenero() {
        return subgenero;
    }

    public void setSubgenero(String subgenero) {
        this.subgenero = subgenero;
    }

    public String getSubgenero2() {
        return subgenero2;
    }

    public void setSubgenero2(String subgenero2) {
        this.subgenero2 = subgenero2;
    }

    public String getSubgenero3() {
        return subgenero3;
    }

    public void setSubgenero3(String subgenero3) {
        this.subgenero3 = subgenero3;
    }

    public String getSubgenero4() {
        return subgenero4;
    }

    public void setSubgenero4(String subgenero4) {
        this.subgenero4 = subgenero4;
    }

    @Override
    public String toString() {
        return "Musica{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", song='" + song + '\'' +
                ", durationMs=" + durationMs +
                ", year=" + year +
                ", popularity=" + popularity +
                ", genre='" + genre + '\'' +
                ", subgenero='" + subgenero + '\'' +
                ", subgenero2='" + subgenero2 + '\'' +
                ", subgenero3='" + subgenero3 + '\'' +
                ", subgenero4='" + subgenero4 + '\'' +
                '}';
    }
}
