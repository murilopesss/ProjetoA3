package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                	avaliaMusica(idUsuarioSelecionado);
                    break;
                case 2:
                	int q = 0;
                	
            		System.out.println("Montar uma playlist de 10, 20 ou 30 músicas?: ");                  
                    System.out.print("Escreva sua opção(0 para sair): ");
                    
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
					
                    imprimePlaylist(idUsuarioSelecionado, q);
                    break;
                case 3:
                    playlstBanco(idUsuarioSelecionado);
                    break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
    
    public static void playlstBanco(int idUsuarioSelecionado) {
        Scanner leia = new Scanner(System.in);
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> idsPlaylists = new ArrayList<>();
        List<Integer> idsMusicas = new ArrayList<>();

        try {
            conexao = ConexaoMySQL.getInstance();

            // Consulta para obter as playlists do usuário
            String sqlPlaylists = "SELECT DISTINCT id_playlist FROM playlist_musicas WHERE id_usuario = ?";
            pstmt = conexao.prepareStatement(sqlPlaylists);
            pstmt.setInt(1, idUsuarioSelecionado);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idPlaylist = rs.getInt("id_playlist");
                idsPlaylists.add(idPlaylist);
            }

            if (idsPlaylists.isEmpty()) {
                System.out.println("Nenhuma playlist encontrada para este usuário.");
                return;
            }

            int idPlaylistSelecionada;
            if (idsPlaylists.size() == 1) {
                idPlaylistSelecionada = idsPlaylists.get(0);
                System.out.println("Playlist encontrada automaticamente.");
            } else {
                // Se houver mais de uma playlist, perguntar ao usuário qual ele deseja selecionar
                System.out.println("Playlists encontradas:");
                for (int i = 0; i < idsPlaylists.size(); i++) {
                    System.out.println((i + 1) + ". Playlist ID: " + idsPlaylists.get(i));
                }

                int escolha;
                do {
                    System.out.print("Selecione o número correspondente à playlist desejada: ");
                    escolha = leia.nextInt();
                } while (escolha < 1 || escolha > idsPlaylists.size());

                idPlaylistSelecionada = idsPlaylists.get(escolha - 1);
            }

            // Consulta para obter as músicas da playlist selecionada
            String sqlMusicas = "SELECT id_musica FROM playlist_musicas WHERE id_usuario = ? AND id_playlist = ?";
            pstmt = conexao.prepareStatement(sqlMusicas);
            pstmt.setInt(1, idUsuarioSelecionado);
            pstmt.setInt(2, idPlaylistSelecionada);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idMusica = rs.getInt("id_musica");
                idsMusicas.add(idMusica);
            }

            // Exibição dos nomes das músicas
            if (!idsMusicas.isEmpty()) {
                System.out.println("Músicas na playlist:");

                for (int idMusica : idsMusicas) {
                    String nomeMusica = obterNomeMusica(conexao, idMusica);
                    System.out.println("- " + nomeMusica);
                }
            } else {
                System.out.println("A playlist selecionada não contém músicas.");
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

    private static String obterNomeMusica(Connection conexao, int idMusica) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String nomeMusica = "";

        try {
            String sql = "SELECT song FROM musica WHERE id = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, idMusica);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nomeMusica = rs.getString("song");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nomeMusica;
    }

    public static void imprimePlaylist(int idUsuarioSelecionado, int q) {
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
        List<Map.Entry<String, Double>> sortedGeneros = generoMediaMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .toList();
        int qtd1 = (int) (q * 0.4);
        int qtd2 = (int) (q * 0.3);
        int qtd3 = (int) (q * 0.2);
        int qtd4 = (int) (q * 0.1);
        Map<String, Integer> generoQtdMap = new HashMap<>();
        generoQtdMap.put(sortedGeneros.get(0).getKey(), qtd1);
        generoQtdMap.put(sortedGeneros.get(1).getKey(), qtd2);
        generoQtdMap.put(sortedGeneros.get(2).getKey(), qtd3);
        generoQtdMap.put(sortedGeneros.get(3).getKey(), qtd4);
        List<Integer> playlistIds = new ArrayList<>();
        Random random = new Random();
        Integer idPlaylist = null; // Variável para armazenar o id da playlist
        try {
            conexao = ConexaoMySQL.getInstance();
            // Inserir na tabela playlist e pegar o id_playlist gerado
            String sql = "INSERT INTO playlist_musicas (id_usuario) VALUES (?)";
            pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, idUsuarioSelecionado);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idPlaylist = rs.getInt(1);
                // Deletar a linha em branco criada
                String deleteSql = "DELETE FROM playlist_musicas WHERE id = ?";
                PreparedStatement deletePstmt = conexao.prepareStatement(deleteSql);
                deletePstmt.setInt(1, idPlaylist);
                deletePstmt.executeUpdate();
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
        
        for (Map.Entry<String, Integer> entry : generoQtdMap.entrySet()) {
            String genero = entry.getKey();
            int qtd = entry.getValue();
            try {
                conexao = ConexaoMySQL.getInstance();
                String sql = "SELECT id FROM musica WHERE subgenero = ? ORDER BY RAND() LIMIT ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setString(1, genero);
                pstmt.setInt(2, qtd);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    int idMusica = rs.getInt("id");
                    playlistIds.add(idMusica);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    // Não fechar a conexão aqui para reutilização
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Playlist:");
        for (int i = 0; i < playlistIds.size(); i++) {
            int idMusica = playlistIds.get(i);
            try {
                conexao = ConexaoMySQL.getInstance();
                String sql = "SELECT song FROM musica WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    String musica = rs.getString("song");
                    System.out.println(musica);
                }
                // Inserir cada música na tabela playlist_musicas com o id_playlist
                String insertSql = "INSERT INTO playlist_musicas (id_playlist, id_musica, id_usuario) VALUES (?, ?, ?)";
                pstmt = conexao.prepareStatement(insertSql);
                pstmt.setInt(1, idPlaylist);
                pstmt.setInt(2, idMusica);
                pstmt.setInt(3, idUsuarioSelecionado);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    // Não fechar a conexão aqui para reutilização
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static void avaliaMusica(int idUsuarioSelecionado) {
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
