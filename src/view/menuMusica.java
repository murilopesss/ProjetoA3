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

        do {
            System.out.println("Submenu");
            System.out.println("1. Criar Playlist");
            System.out.println("2. Listar Playlist");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = leia.nextInt();

            switch (opcao) {
                case 1:
                    criarPlaylist();
                    break;
                case 2:
                    imprimePlaylist();
                    break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
    }
    
    public static void imprimePlaylist() {
        Scanner leia = new Scanner(System.in);

        // Etapa 1: Seleção do usuário
        System.out.print("Digite seu nome: ");
        String nomeUsuario = leia.nextLine();

        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> idsUsuarios = new ArrayList<>();
        List<String> nomesUsuarios = new ArrayList<>();
        int idUsuarioSelecionado = -1;

        try {
            // Obter conexão com o banco de dados
            conexao = ConexaoMySQL.getInstance();

            // Preparar a consulta SQL
            String sql = "SELECT id, nome FROM dados_usuarios WHERE LOWER(nome) = LOWER(?)";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nomeUsuario);

            // Executar a consulta
            rs = pstmt.executeQuery();

            // Processar o resultado
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

        // Etapa 2: Recuperação das avaliações
        List<Integer> idsMusicas = new ArrayList<>();
        List<Integer> notas = new ArrayList<>();

        try {
            // Obter conexão com o banco de dados
            conexao = ConexaoMySQL.getInstance();

            // Preparar a consulta SQL
            String sql = "SELECT idmusica, nota FROM avaliacao WHERE idusuario = ?";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, idUsuarioSelecionado);

            // Executar a consulta
            rs = pstmt.executeQuery();

            // Processar o resultado
            while (rs.next()) {
                int idMusica = rs.getInt("idmusica");
                int nota = rs.getInt("nota");
                idsMusicas.add(idMusica);
                notas.add(nota);
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

        // Etapa 3: Recuperação das músicas e armazenamento das notas nos arrays correspondentes
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
                // Obter conexão com o banco de dados
                conexao = ConexaoMySQL.getInstance();

                // Preparar a consulta SQL
                String sql = "SELECT subgenero FROM musica WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);

                // Executar a consulta
                rs = pstmt.executeQuery();

                // Processar o resultado
                if (rs.next()) {
                    String generoMusica = rs.getString("subgenero").toLowerCase();

                    // Armazenar a nota no array correspondente ao gênero
                    if (generoNotasMap.containsKey(generoMusica)) {
                        generoNotasMap.get(generoMusica).add(nota);
                    }
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

        // Calcular a média das notas por gênero
        Map<String, Double> generoMediaMap = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : generoNotasMap.entrySet()) {
            String genero = entry.getKey();
            List<Integer> notasGenero = entry.getValue();

            if (!notasGenero.isEmpty()) {
                double soma = 0;
                for (int nota : notasGenero) {
                    soma += nota;
                }
                double media = soma / notasGenero.size();
                generoMediaMap.put(genero, media);
            }
        }

        // Ordenar os gêneros pela média das notas
        List<Map.Entry<String, Double>> listaOrdenada = new ArrayList<>(generoMediaMap.entrySet());
        listaOrdenada.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        // Imprimir o ranking dos gêneros
        System.out.println("Ranking dos gêneros por pontuação média:");
        for (Map.Entry<String, Double> entry : listaOrdenada) {
            System.out.printf("%s: %.2f%n", entry.getKey(), entry.getValue());
        }
    }



    

    
    
    public static void criarPlaylist() {
        Scanner leia = new Scanner(System.in);

        // Etapa 1: Seleção do usuário
        System.out.print("Digite seu nome: ");
        String nomeUsuario = leia.nextLine();

        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Integer> idsUsuarios = new ArrayList<>();
        List<String> nomesUsuarios = new ArrayList<>();
        int idUsuarioSelecionado = -1;

        try {
            // Obter conexão com o banco de dados
            conexao = ConexaoMySQL.getInstance();

            // Preparar a consulta SQL
            String sql = "SELECT id, nome FROM dados_usuarios WHERE LOWER(nome) = LOWER(?)";
            pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, nomeUsuario);

            // Executar a consulta
            rs = pstmt.executeQuery();

            // Processar o resultado
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

        // Etapa 2: Criação da playlist e armazenamento das avaliações
        Random random = new Random();
        List<Integer> notas = new ArrayList<>();
        List<String> generos = new ArrayList<>();
        List<Integer> idsMusicas = new ArrayList<>();
        List<String> nomesMusicas = new ArrayList<>();
        List<String> artistasMusicas = new ArrayList<>();

        while (true) {
            int idMusica = random.nextInt(1000) + 1;

            try {
                // Obter conexão com o banco de dados
                conexao = ConexaoMySQL.getInstance();

                // Preparar a consulta SQL
                String sql = "SELECT artist, song, subgenero FROM musica WHERE id = ?";
                pstmt = conexao.prepareStatement(sql);
                pstmt.setInt(1, idMusica);

                // Executar a consulta
                rs = pstmt.executeQuery();

                // Processar o resultado
                if (rs.next()) {
                    String artista = rs.getString("artist");
                    String nomeMusica = rs.getString("song");
                    String generoMusica = rs.getString("subgenero");
                    System.out.println("Música encontrada: " + nomeMusica + " de " + artista);

                    // Pedir ao usuário para avaliar a música
                    int nota;
                    do {
                        System.out.print("Dê uma nota para a música (1 a 5, 0 para sair): ");
                        nota = leia.nextInt();
                    } while (nota < 0 || nota > 5);

                    // Verificar se o usuário quer sair
                    if (nota == 0) {
                        break;
                    }

                    // Salvar a nota, o gênero, o ID da música, o nome da música e o artista nos arrays
                    notas.add(nota);
                    generos.add(generoMusica);
                    idsMusicas.add(idMusica);
                    nomesMusicas.add(nomeMusica);
                    artistasMusicas.add(artista);

                    // Inserir a avaliação na tabela "avaliacao"
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

        // Exibir as notas, gêneros e IDs armazenados
       /* System.out.println("Notas dadas pelo usuário:");
        for (int nota : notas) {
            System.out.print(nota + "  ");
        }
        System.out.println();

        System.out.println("Gêneros das músicas:");
        for (String genero : generos) {
            System.out.print(genero + "  ");
        }
        System.out.println();

        System.out.println("IDs das músicas:");
        for (int id : idsMusicas) {
            System.out.print(id + "  ");
        }
        System.out.println();*/
    }


	

    public static void main(String[] args) {
        mostrar();
    }
}

