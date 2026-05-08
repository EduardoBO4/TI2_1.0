package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class MensagemDAO {

    public void inserir(Mensagem m) throws Exception {
        Connection conn = Conexao.conectar();

        String sql = "INSERT INTO conversa (idprestador, idusuario, mensagem, dataenvio) VALUES (?, ?, ?, ?)";

        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, m.getPrestadorId());
        st.setInt(2, m.getUsuarioId());
        st.setString(3, m.getTexto());
        st.setObject(4, LocalDateTime.now());

        st.execute();
        conn.close();
    }

    public void listar() throws Exception {
        Connection conn = Conexao.conectar();

        String sql = "SELECT * FROM conversa";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println("-------------------");
            System.out.println("Prestador: " + rs.getInt("idprestador"));
            System.out.println("Usuário: " + rs.getInt("idusuario"));
            System.out.println("Mensagem: " + rs.getString("mensagem"));
            System.out.println("Data: " + rs.getObject("dataenvio"));
        }

        conn.close();
    }

    public void atualizar(String mensagemAntiga, String mensagemNova) throws Exception {
        Connection conn = Conexao.conectar();

        String sql = "UPDATE conversa SET mensagem = ? WHERE mensagem = ?";

        PreparedStatement st = conn.prepareStatement(sql);

        st.setString(1, mensagemNova);
        st.setString(2, mensagemAntiga);

        st.executeUpdate();

        conn.close();
    }
    
    public void deletar(String mensagem) throws Exception {
        Connection conn = Conexao.conectar();

        String sql = "DELETE FROM conversa WHERE mensagem = ?";

        PreparedStatement st = conn.prepareStatement(sql);

        st.setString(1, mensagem);

        st.executeUpdate();

        conn.close();
    }
}