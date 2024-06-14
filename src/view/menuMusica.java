package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import connection.ConexaoMySQL;

public class menuMusica {

    public static void mostrar() {
        Scanner leia = new Scanner(System.in);
        int opcao;
        
        System.out.print("Digite seu nome: ");
        String nomeUsuario = leia.nextLine();

        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> idsUsuarios = new ArrayList<>();
        List<String> nomesUsuarios = new ArrayList<>();
        int idUsuarioSelecionado = -1;

        try {
            conexao = ConexaoMySQL.getInstance();

            String sql = "SELECT id, nome FROM dados_usuarios WHERE LOWER(nome) = LOWER(?)";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nomeUsuario);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                idsUsuarios.add(id);
                nomesUsuarios.add(nome);
            }

            if (idsUsuarios.size() == 0) {
                System.out.println("Nenhum usuário encontrado com o nome fornecido.");
                return;
            } else if (idsUsuarios.size() == 1) {
                idUsuarioSelecionado = idsUsuarios.get(0);
            } else {
                System.out.println("Usuários encontrados:");
                for (int i = 0; i < idsUsuarios.size(); i++) {
                    System.out.println((i + 1) + ". " + nomesUsuarios.get(i));
                }
                
                int escolha;
                do {
                    System.out.print("Selecione o número correspondente ao seu usuário: ");
                    escolha = leia.nextInt();
                } while (escolha < 1 || escolha > idsUsuarios.size());

                idUsuarioSelecionado = idsUsuarios.get(escolha - 1);
            }

            System.out.println("Usuário selecionado: " + nomesUsuarios.get(idsUsuarios.indexOf(idUsuarioSelecionado)));
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

        if (idUsuarioSelecionado == -1) {
            return;
        }
        do {
            System.out.println("Submenu");
            System.out.println("1. Avaliar Músicas");
            System.out.println("2. Montar Playlist");
            System.out.println("3. Minhas Playlists");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = leia.nextInt();

            switch (opcao) {
                case 1:
                	criarPlaylist(idUsuarioSelecionado);
                    break;
                case 2:
                	do {
                		System.out.println("Montar uma playlist de 10, 20 ou 30 músicas?: ");                  
                        System.out.print("Escreva sua opção(0 para sair): ");
                        int q;
                        opcao = leia.nextInt();
                        switch (opcao) {
							case 10: 
								q = 10;
								break;
							case 20: 
								q = 20;
								break;
							case 30: 
								q = 30;
								break;						
						default:
							System.out.println("Desculpe, escreva apenas 10, 20 ou 30: ");
						}
					} while (opcao !=0);
                    imprimePlaylist(idUsuarioSelecionado);
                    break;
                case 3:
                    playlstBanco();
                    break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
    public static void playlstBanco() {
    	
    }
    public static void imprimePlaylist(int idUsuarioSelecionado) {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> idsMusicas = new ArrayList<>();
        List<Integer> notas = new ArrayList<>();

        try {
            conexao = ConexaoMySQL.getInstance();
            String sql = "SELECT idmusica, nota FROM avaliacao WHERE idusuario = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, idUsuarioSelecionado);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idMusica = rs.getInt("idmusica");
                int nota = rs.getInt("nota");
                idsMusicas.add(idMusica);
                notas.add(nota);
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

        Map<String, List<Integer>> generoNotasMap = new HashMap<>();
        generoNotasMap.put("pop", new ArrayList<>());
        generoNotasMap.put("rock", new ArrayList<>());
        generoNotasMap.put("world/traditional", new ArrayList<>());
        generoNotasMap.put("country", new ArrayList<>());
        generoNotasMap.put("folk/acoustic", new ArrayList<>());
        generoNotasMap.put("hip hop", new ArrayList<>());
        generoNotasMap.put("set", new ArrayList<>());
        generoNotasMap.put("metal", new ArrayList<>());
        generoNotasMap.put("latin", new ArrayList<>());
        generoNotasMap.put("dance/electronic", new ArrayList<>());
        generoNotasMap.put("r&b", new ArrayList<>());

        for (int i = 0; i < idsMusicas.size(); i++) {
            int idMusica = idsMusicas.get(i);
            int nota = notas.get(i);

            try {
                conexao = ConexaoMySQL.getInstance();
                String sql = "SELECT subgenero FROM musica WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    String generoMusica = rs.getString("subgenero").toLowerCase();
                    if (generoNotasMap.containsKey(generoMusica)) {
                        generoNotasMap.get(generoMusica).add(nota);
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

        Map<String, Double> generoMediaMap = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : generoNotasMap.entrySet()) {
            String genero = entry.getKey();
            List<Integer> notasGenero = entry.getValue();
            double media = notasGenero.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            generoMediaMap.put(genero, media);
        }

        generoMediaMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> {
                System.out.println("Gênero: " + entry.getKey() + " - Nota Média: " + entry.getValue());
            });
    }


   
    public static void criarPlaylist(int idUsuarioSelecionado) {
        Scanner leia = new Scanner(System.in);
        Random random = new Random();
        List<Integer> notas = new ArrayList<>();
        List<String> generos = new ArrayList<>();
        List<Integer> idsMusicas = new ArrayList<>();
        List<String> nomesMusicas = new ArrayList<>();
        List<String> artistasMusicas = new ArrayList<>();

        while (true) {
            int idMusica = random.nextInt(1000) + 1;

            try {
                Connection conexao = ConexaoMySQL.getInstance();
                String sql = "SELECT artist, song, subgenero FROM musica WHERE id = ?";
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String artista = rs.getString("artist");
                    String nomeMusica = rs.getString("song");
                    String generoMusica = rs.getString("subgenero");
                    System.out.println("Música encontrada: " + nomeMusica + " de " + artista);

                    int nota;
                    do {
                        System.out.print("Dê uma nota para a música (1 a 5, 0 para sair): ");
                        nota = leia.nextInt();
                    } while (nota < 0 || nota > 5);

                    if (nota == 0) {
                        break;
                    }

                    notas.add(nota);
                    generos.add(generoMusica);
                    idsMusicas.add(idMusica);
                    nomesMusicas.add(nomeMusica);
                    artistasMusicas.add(artista);

                    try {
                        String insertSql = "INSERT INTO avaliacao (idusuario, idmusica, nota) VALUES (?, ?, ?)";
                        PreparedStatement insertPstmt = conexao.prepareStatement(insertSql);
                        insertPstmt.setInt(1, idUsuarioSelecionado);
                        insertPstmt.setInt(2, idMusica);
                        insertPstmt.setInt(3, nota);
                        insertPstmt.executeUpdate();
                        insertPstmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Música com ID " + idMusica + " não encontrada.");
                }

                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
		mostrar();
	}
    
   
}

