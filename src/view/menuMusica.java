package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import connection.ConexaoMySQL;

public class menuMusica {

    public static void mostrar() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Submenu");
            System.out.println("1. Criar Playlist");
            System.out.println("2. Listar Playlist");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    criarPlaylist();
                    break;
                case 2:
                    System.out.println("Opção 2 do Submenu escolhida.");
                    break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
    
    
    public static void criarPlaylist() {
    	Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // Arrays dinâmicos para armazenar notas e gêneros
        List<Integer> notas = new ArrayList<>();
        List<String> generos = new ArrayList<>();

        while (true) {
            int idMusica = random.nextInt(1000) + 1;

            Connection conexao = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            try {
                // Obter conexão com o banco de dados
                conexao = ConexaoMySQL.getInstance();

                // Preparar a consulta SQL
                String sql = "SELECT artist, song, genre FROM musicas WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);

                // Executar a consulta
                rs = pstmt.executeQuery();

                // Processar o resultado
                if (rs.next()) {
                	String artista = rs.getString("artist");
                    String nomeMusica = rs.getString("song");
                    String generoMusica = rs.getString("genre");
                    System.out.println("Música aleatória encontrada: " + nomeMusica + " de " + artista);

                    // Pedir ao usuário para avaliar a música
                    int nota;
                    do {
                        System.out.print("Dê uma nota para a música (0 a 5, 0 para sair): ");
                        nota = scanner.nextInt();
                    } while (nota < 0 || nota > 5);

                    // Verificar se o usuário quer sair
                    if (nota == 0) {
                        break;
                    }

                    // Salvar a nota e o gênero nos arrays
                    notas.add(nota);
                    generos.add(generoMusica);
                } else {
                    System.out.println("Música com ID " + idMusica + " não encontrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Fechar recursos
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conexao != null) conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Exibir as notas e gêneros armazenados
        System.out.println("Notas dadas pelo usuário:");
        for (int nota : notas) {
            System.out.print(nota + " - ");
        }
        System.out.println();

        System.out.println("Gêneros das músicas:");
        for (String genero : generos) {
            System.out.print(genero + " - ");
        }
        System.out.println();
        
        
    }

    public static void main(String[] args) {
        mostrar();
    }
}

