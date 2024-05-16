package view;

import model.Cadastro;

public class testeCadastro {
    public static void main(String[] args) {
        System.out.println("O Cadastro se iniciou...");

        Cadastro c []= new Cadastro[2];
        c[0] = new Cadastro();
        c[0].setId(1);
        c[0].setNome("Murilo de Oliveira Lopes");
        c[0].setDataNascimento("18/11/2002");
        c[0].setEndereco("rua José Bonifácio, 205- Tabajaras - Uberlândia - MG");
        c[0].setDocumento("123.456.789-01");
        c[0].setEmail("projetoA3@muriloUNA.com");

        System.out.println("\nNome: \t"+ c[0].getNome());

        c[1] = new Cadastro(2,"Pedro Lopes","10/08/2005","rua fulando de tal","123.456.789-02","pedro@projetoA3.com");
        System.out.println("\nNome:\t" + c[1].getNome());
        
    }
}
