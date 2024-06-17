package model;

public class Avaliacao {
    private int id;
    private int idMusica;
    private int idUsuario;
    private int nota;

    public Avaliacao() {
    }

    public Avaliacao(int id, int idMusica, int idUsuario, int nota) {
        this.id = id;
        this.idMusica = idMusica;
        this.idUsuario = idUsuario;
        this.nota = nota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", idMusica=" + idMusica +
                ", idUsuario=" + idUsuario +
                ", nota=" + nota +
                '}';
    }
}

