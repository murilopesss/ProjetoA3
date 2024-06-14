
package view;

import model.Usuario;
import controller.ControlaCadastro;
import controller.ControlaCadastro;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            System.out.println("3. Listar todos usuários");
            System.out.println("4. Listar usuários por parte do nome");
            System.out.println("5. Remover um usuário");
            System.out.println("6. Menu de Playlist");
            System.out.println("7. Relatorio");
            System.out.println("0. Fechar / Sair do programa");
            System.out.print("Escolha uma opção: ");
            System.out.println();
            option = leia.nextInt();
            leia.nextLine(); 
            System.out.println();

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
                    menuMusica.mostrar();
                    break;
                case 7:
                	System.out.println("Menu Relaórios");
                    System.out.println("1. Músicas por generos");
                    System.out.println("2. Listar músicas por cantor");
                    System.out.println("3. Imprimir média de ano de lançamento");
                    System.out.println("0. Voltar para o Menu");
                    System.out.print("Escolha uma opção: ");
                    option = leia.nextInt();
                    leia.nextLine();
                    System.out.println();
                    switch (option) {
						case 1: 
							musicasPorGenero();
							 System.out.println();
							break;
						case 2: 
							MusicasPorArtista();
							 System.out.println();
							break;
						case 3: 
							mediaAnoLancamento();
							 System.out.println();
							break;
						case 0:
		                    System.out.println("Voltando ao menu...");
		                    break;
						default:
							 System.out.println("Opção inválida. Tente novamente.");
                    }
	                    break;
                case 0:
                    System.out.println("Fechando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option != 0);
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

        Usuario cadastro = ControlaCadastro.buscarPorId(id);
        if (cadastro != null) {
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
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private static void listarTodos() {
        List<Usuario> cadastros = ControlaCadastro.listarTodos();
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

        List<Usuario> cadastros = ControlaCadastro.listarPorNome(nome);
        if (cadastros.isEmpty()) {
            System.out.println("Nenhum contato encontrado com o nome informado.");
        } else {
            for (Usuario cadastro : cadastros) {
                System.out.println(cadastro);
            }
        }
    }

    private static void removerUsuario() {
    	 System.out.print("Informe o ID do contato que deseja remover: ");
         int id = leia.nextInt();
         leia.nextLine(); 

         ControlaCadastro.removeCadastro(id);

         for (Usuario cadastro : cadastros) {
             if (cadastro.getId() == id) {
                 cadastros.remove(cadastro);
                 System.out.println("Contato removido com sucesso!");
                 return;
             }
         }
                 
    }
    public static void musicasPorGenero() {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConexaoMySQL.getInstance();

            String sql = "SELECT subgenero, COUNT(*) AS quantidade FROM musica GROUP BY subgenero";
            pstmt = conexao.prepareStatement(sql);

            rs = pstmt.executeQuery();

            Map<String, Integer> contagemPorGenero = new HashMap<>();

            while (rs.next()) {
                String subgenero = rs.getString("subgenero");
                int quantidade = rs.getInt("quantidade");
                contagemPorGenero.put(subgenero, quantidade);
            }

            System.out.println("Contagem de músicas por subgênero:");
            for (Map.Entry<String, Integer> entry : contagemPorGenero.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void MusicasPorArtista() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do artista: ");
        String nomeArtista = scanner.nextLine();

        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConexaoMySQL.getInstance();


            String sql = "SELECT song FROM musica WHERE artist = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nomeArtista);

            rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhuma música encontrada para o artista: " + nomeArtista);
            } else {
                System.out.println("Músicas do artista " + nomeArtista + ":");

                while (rs.next()) {
                    String nomeMusica = rs.getString("song");
                    System.out.println(nomeMusica);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void mediaAnoLancamento() {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConexaoMySQL.getInstance();

            String sql = "SELECT year FROM musica";
            pstmt = conexao.prepareStatement(sql);

            rs = pstmt.executeQuery();

            int somaAnos = 0;
            int quantidadeMusicas = 0;

            while (rs.next()) {
                int anoLancamento = rs.getInt("year");
                somaAnos += anoLancamento;
                quantidadeMusicas++;
            }

            int mediaLancamento = (quantidadeMusicas > 0) ? (int) Math.round((double) somaAnos / quantidadeMusicas) : 0;

            System.out.println("A média do ano de lançamento das músicas é: " + mediaLancamento);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }  
}
