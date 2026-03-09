package user.demo;

import java.sql.*;

public class UsuarioDAO extends DAO {

	public UsuarioDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}

	public boolean insert(Usuario usuario) {

		boolean status = false;

		try {

			String sql = "INSERT INTO usuario (id, nome, senha) VALUES (?, ?, ?)";

			PreparedStatement ps = conexao.prepareStatement(sql);

			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.setString(3, usuario.getSenha());

			ps.executeUpdate();
			ps.close();

			status = true;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return status;
	}
	
	public Usuario get(int id) {

		Usuario usuario = null;

		try {

			Statement st = conexao.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT * FROM usuario WHERE id=" + id;

			System.out.println(sql);

			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {

				usuario = new Usuario(
						rs.getInt("id"),
						rs.getString("nome"),
						rs.getString("senha"));
			}

			st.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return usuario;
	}

	public boolean update(Usuario usuario) {

		boolean status = false;

		try {

			Statement st = conexao.createStatement();

			String sql = "UPDATE usuario SET nome = '" 
			+ usuario.getNome() 
			+ "', senha = '" 
			+ usuario.getSenha() 
			+ "' WHERE id = " 
			+ usuario.getId();

			System.out.println(sql);

			st.executeUpdate(sql);
			st.close();

			status = true;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return status;
	}

	public boolean delete(int id) {

		boolean status = false;

		try {

			Statement st = conexao.createStatement();

			String sql = "DELETE FROM usuario WHERE id = " + id;

			System.out.println(sql);

			st.executeUpdate(sql);
			st.close();

			status = true;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return status;
	}

	public boolean autenticar(String nome, String senha) {

		boolean resp = false;

		try {

			Statement st = conexao.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String sql = "SELECT * FROM usuario WHERE nome = '" 
			+ nome 
			+ "' AND senha = '" 
			+ senha + "'";

			System.out.println(sql);

			ResultSet rs = st.executeQuery(sql);

			resp = rs.next();

			st.close();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return resp;
	}
}