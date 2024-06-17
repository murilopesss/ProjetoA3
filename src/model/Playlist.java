package model;

public class Playlist {
    private int idPlaylist;
    private int idMusica;
    private int idUsuario;

    public Playlist() {
    }

    public Playlist(int idPlaylist, int idMusica, int idUsuario) {
        this.idPlaylist = idPlaylist;
        this.idMusica = idMusica;
        this.idUsuario = idUsuario;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public int getIdMusica() {
        return idMusica;
    }

    public void setIdMusica(int idMusica) {
        this.idMusica = idMusica;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist=" + idPlaylist +
                ", idMusica=" + idMusica +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
