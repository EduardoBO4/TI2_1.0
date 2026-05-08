package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Post; // Certifique-se de que a classe Post (ou PostDTO) existe

public class PostDAO extends DAO {

    public PostDAO() {
        super();
        conectar();
    }

    public boolean inserir(Post post) {
        // Usa o idprestador, titulo, legenda. dataenvio é gerada automaticamente pelo banco
        String sql = "INSERT INTO posts (idprestador, titulo, legenda, dataenvio) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, post.getIdprestador());
            st.setString(2, post.getTitulo());
            st.setString(3, post.getLegenda());

            st.executeUpdate();
            st.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir post: " + e.getMessage());
            return false;
        }
    }

    public List<Post> listarTodos() {
        List<Post> lista = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY dataenvio DESC";

        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Post p = new Post();
                p.setIdpost(rs.getInt("idpost"));
                p.setIdprestador(rs.getInt("idprestador"));
                p.setTitulo(rs.getString("titulo"));
                p.setLegenda(rs.getString("legenda"));
                p.setDataenvio(rs.getTimestamp("dataenvio"));
                lista.add(p);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao listar posts: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean deletar(int idpost) {
        String sql = "DELETE FROM posts WHERE idpost = ?";
        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, idpost);
            int linhasAfetadas = st.executeUpdate();
            st.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar post: " + e.getMessage());
            return false;
        }
    }
}