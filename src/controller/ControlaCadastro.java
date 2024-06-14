package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConexaoMySQL;
import model.Usuario;

public class ControlaCadastro {
    
    public static void adicionaCadastro(Usuario usuario) {
        Connection conn = ConexaoMySQL.getInstance();
        String sql = "INSERT INTO dados_usuarios (id, nome, datanascimento, endereco, documento, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getDataNascimento());
            stmt.setString(4, usuario.getEndereco());
            stmt.setString(5, usuario.getDocumento());
            stmt.setString(6, usuario.getEmail());
            stmt.executeUpdate();
            System.out.println("Cadastro adicionado ao banco de dados com sucesso!");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { 
                System.out.println("Erro: ID já existe no banco de dados. Cadastro não adicionado.");
            } else {
                System.out.println("Erro ao adicionar cadastro ao banco de dados: " + e.getMessage());
            }
        }
    }

    public static void atualizaCadastro(Usuario usuario) {
        Connection conn = ConexaoMySQL.getInstance();
        String sql = "UPDATE dados_usuarios SET nome = ?, datanascimento = ?, endereco = ?, documento = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getDataNascimento());
            stmt.setString(3, usuario.getEndereco());
            stmt.setString(4, usuario.getDocumento());
            stmt.setString(5, usuario.getEmail());
            stmt.setInt(6, usuario.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cadastro atualizado com sucesso!");
            } else {
                System.out.println("Nenhum cadastro encontrado com o ID informado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cadastro no banco de dados: " + e.getMessage());
        }
    }

    public static void removeCadastro(int id) {
        Connection conn = ConexaoMySQL.getInstance();
        String checkSql = "SELECT id FROM dados_usuarios WHERE id = ?";
        String deleteSql = "DELETE FROM dados_usuarios WHERE id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, id);
                    int rowsDeleted = deleteStmt.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Cadastro removido com sucesso!");
                    } else {
                        System.out.println("Nenhum cadastro encontrado com o ID informado.");
                    }
                }
            } else {
                System.out.println("ID não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover cadastro do banco de dados: " + e.getMessage());
        }
    }
    
    public static Usuario buscarPorId(int id) {
        Connection conn = ConexaoMySQL.getInstance();
        String sql = "SELECT * FROM dados_usuarios WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("datanascimento"),
                    rs.getString("endereco"),
                    rs.getString("documento"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cadastro no banco de dados: " + e.getMessage());
        }
        return null;
    }
    
    
    
    
    

    public static List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = ConexaoMySQL.getInstance();
        String sql = "SELECT * FROM dados_usuarios";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("datanascimento"),
                    rs.getString("endereco"),
                    rs.getString("documento"),
                    rs.getString("email")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar cadastros do banco de dados: " + e.getMessage());
        }
        return usuarios;
    }

    public static List<Usuario> listarPorNome(String nome) {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = ConexaoMySQL.getInstance();
        String sql = "SELECT * FROM dados_usuarios WHERE nome LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("datanascimento"),
                    rs.getString("endereco"),
                    rs.getString("documento"),
                    rs.getString("email")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar cadastros por nome no banco de dados: " + e.getMessage());
        }
        return usuarios;
    }
}
