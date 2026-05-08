package app;

import service.ConversaService;
import service.UsuarioService;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

public class Aplicacao {
	
	private static UsuarioService usuarioService = new UsuarioService();
	private static ConversaService conversaService = new ConversaService();
	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/public"); 
		
		before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        });
		
		options("/*", (req, res) -> { res.status(200); return "OK"; });

		post("/login", (resquest, response) -> usuarioService.login(resquest, response));
		post("/cadastro", (resquest, response) -> usuarioService.cadastro(resquest, response));
	
		get("/usuarios",  (resquest, response) -> usuarioService.listarComFiltro(resquest, response));
		get("/usuario/:id", (resquest, response) -> usuarioService.buscarPorId(resquest, response));
		put("/usuario/:id", (resquest, response) -> usuarioService.atualizar(resquest, response));
		delete("/usuario/:id",(resquest, response) -> usuarioService.deletar(resquest, response));

		post("/conversa", (request, response) -> conversaService.inserir(request, response));
        get("/conversas", (request, response) -> conversaService.listar(request, response));
        put("/conversa/:id", (request, response) -> conversaService.atualizar(request, response));
        delete("/conversa/:id", (request, response) -> conversaService.deletar(request, response));
	}	
}
