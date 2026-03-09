package user.demo;

public class Principal {

	public static void main(String[] args) {

		UsuarioDAO usuarioDAO = new UsuarioDAO();

		// criar usuario
		Usuario u1 = new Usuario(1, "eduardo", "123");

		// INSERT
		if(usuarioDAO.insert(u1)) {
			System.out.println("Usuario inserido com sucesso!");
		}

		// GET
		Usuario usuario = usuarioDAO.get(1);
		if(usuario != null) {
			System.out.println("Usuario encontrado:");
			System.out.println("ID: " + usuario.getId());
			System.out.println("Nome: " + usuario.getNome());
			System.out.println("Senha: " + usuario.getSenha());
		}

		// UPDATE
		u1.setNome("eduardo atualizado");
		u1.setSenha("456");

		if(usuarioDAO.update(u1)) {
			System.out.println("Usuario atualizado com sucesso!");
		}

		// AUTENTICAR
		boolean login = usuarioDAO.autenticar("eduardo atualizado", "456");

		if(login) {
			System.out.println("Login realizado com sucesso!");
		} else {
			System.out.println("Erro no login.");
		}

		// DELETE
		if(usuarioDAO.delete(1)) {
			System.out.println("Usuario deletado com sucesso!");
		}
	}
}