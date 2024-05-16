package model;


public class Cadastro {
    private int id;
    private String nome;
    private String dataNascimento;
    private String endereco;
    private String documento;
    private String email;

    // Construtor padrão
    public Cadastro() {
    }

    // Construtor com parâmetros
    public Cadastro(int id, String nome, String dataNascimento, String endereco,  String documento, String email) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.documento = documento;
        this.email = email;
    }

    // Getters and Setters

    public int getId(){
        return id;
    }

    public void setId(int Id){
        this.id = Id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Cadastro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", endereco='" + endereco + '\'' +
                ", documento='" + documento + '\'' +
                ", email='" + email + '\'' +
                '}';
}

}
    
