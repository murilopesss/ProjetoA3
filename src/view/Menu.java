
package view;

import model.Usuario;
import controller.ControlaCadastro;
import controller.ControlaCadastro;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


import connection.ConexaoMySQL;

public class Menu {

    private static ArrayList<Usuario> cadastros = new ArrayList<>();
    private static Scanner leia = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("Menu de Opções:");
            System.out.println("1. Adicionar um cadastro");
            System.out.println("2. Atualizar um cadastro");
            System.out.println("3. Listar todos");
            System.out.println("4. Listar por parte do nome");
            System.out.println("5. Remover um usuário");
            System.out.println("6. Fechar / Sair do programa");
            System.out.print("Escolha uma opção: ");
            option = leia.nextInt();
            leia.nextLine(); 

            switch (option) {
                case 1:
                    adicionarCadastro();
                    break;
                case 2:
                    atualizarCadastro();
                    break;
                case 3:
                    listarTodos();
                    break;
                case 4:
                    listarPorNome();
                    break;
                case 5:
                    removerUsuario();
                    break;
                case 6:
                    System.out.println("Fechando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option != 6);
    }

    private static void adicionarCadastro() {
        System.out.print("ID: ");
        int id = leia.nextInt();
        leia.nextLine(); 

        System.out.print("Nome: ");
        String nome = leia.nextLine();

        System.out.print("Data de Nascimento (yyyy-mm-dd): ");
        String dataNascimento = leia.nextLine();

        System.out.print("Endereço: ");
        String endereco = leia.nextLine();

        System.out.print("Documento: ");
        String documento = leia.nextLine();

        System.out.print("Email: ");
        String email = leia.nextLine();

        Usuario cadastro = new Usuario(id, nome, dataNascimento, endereco, documento, email);
        cadastros.add(cadastro);
        
        ControlaCadastro.adicionaCadastro(cadastro);
        
        System.out.println("Contato adicionado com sucesso!");
    }

    private static void atualizarCadastro() {
        System.out.print("Informe o ID do contato que deseja atualizar: ");
        int id = leia.nextInt();
        leia.nextLine(); 

        for (Usuario cadastro : cadastros) {
            if (cadastro.getId() == id) {
                System.out.print("Novo nome: ");
                cadastro.setNome(leia.nextLine());

                System.out.print("Nova data de nascimento (yyyy-mm-dd): ");
                cadastro.setDataNascimento(leia.nextLine());

                System.out.print("Novo endereço: ");
                cadastro.setEndereco(leia.nextLine());

                System.out.print("Novo documento: ");
                cadastro.setDocumento(leia.nextLine());

                System.out.print("Novo email: ");
                cadastro.setEmail(leia.nextLine());
                
                ControlaCadastro.atualizaCadastro(cadastro);

                System.out.println("Contato atualizado com sucesso!");
                return;
            }
        }
        System.out.println("Contato não encontrado.");
    }

    private static void listarTodos() {
        if (cadastros.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
        } else {
            for (Usuario cadastro : cadastros) {
                System.out.println(cadastro);
            }
        }
    }

    private static void listarPorNome() {
        System.out.print("Informe parte do nome que deseja buscar: ");
        String nome = leia.nextLine();

        for (Usuario cadastro : cadastros) {
            if (cadastro.getNome().toLowerCase().contains(nome.toLowerCase())) {
                System.out.println(cadastro);
            }
        }
    }

    private static void removerUsuario() {
    	 System.out.print("Informe o ID do contato que deseja remover: ");
         int id = leia.nextInt();
         leia.nextLine(); 

         // Remover o cadastro do banco de dados
         ControlaCadastro.removeCadastro(id);

         for (Usuario cadastro : cadastros) {
             if (cadastro.getId() == id) {
                 cadastros.remove(cadastro);
                 System.out.println("Contato removido com sucesso!");
                 return;
             }
         }
                 
    }
        
    
}
