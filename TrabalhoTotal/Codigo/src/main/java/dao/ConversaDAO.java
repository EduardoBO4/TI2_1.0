package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Conversa;

public class ConversaDAO extends DAO {

    public ConversaDAO() {
        super();
        conectar();
    }

    public boolean inserir(Conversa c) {
        // Ajuste o nome das colunas caso estejam diferentes no seu banco de dados
        String sql = "INSERT INTO conversa (idprestador, idusuario, texto) VALUES (?, ?, ?)";

        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, c.getIdPrestador());
            st.setInt(2, c.getIdUsuario());
            st.setString(3, c.getTexto());

            int linhasAfetadas = st.executeUpdate();
            st.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir conversa: " + e.getMessage());
            return false;
        }
    }

    public List<Conversa> listar() {
        List<Conversa> conversas = new ArrayList<>();
        String sql = "SELECT * FROM conversa";

        try {
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Conversa c = new Conversa(
                    rs.getInt("idMensagem"), 
                    rs.getInt("idprestador"),
                    rs.getInt("idusuario"),
                    rs.getString("texto") // Ajuste se a coluna se chamar 'mensagem' no banco
                );
                conversas.add(c);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao listar conversas: " + e.getMessage());
        }
        return conversas;
    }

    public boolean atualizar(Conversa c) {
        String sql = "UPDATE conversa SET texto = ? WHERE idMensagem = ?";

        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, c.getTexto());
            st.setInt(2, c.getIdMensagem());

            int linhasAfetadas = st.executeUpdate();
            st.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar conversa: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int idMensagem) {
        String sql = "DELETE FROM conversa WHERE idMensagem = ?";

        try {
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, idMensagem);

            int linhasAfetadas = st.executeUpdate();
            st.close();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar conversa: " + e.getMessage());
            return false;
        }
    }
}