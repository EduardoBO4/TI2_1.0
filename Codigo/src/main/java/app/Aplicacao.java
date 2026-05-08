package app;

import service.UsuarioService;
import service.PostService;
import service.PrestadorService;


import static spark.Spark.*;

public class Aplicacao {
    
    private static UsuarioService usuarioService = new UsuarioService();
    private static PostService postService = new PostService();
    private static PrestadorService prestadorService = new PrestadorService();
    
    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/public");
        
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        });
        
        options("/*", (req, res) -> { res.status(200); return "OK"; });

        // --- USUÁRIO ---
        post("/login", (request, response) -> usuarioService.login(request, response));
        post("/cadastro", (request, response) -> usuarioService.cadastro(request, response));
        get("/usuarios",  (request, response) -> usuarioService.listarComFiltro(request, response));
        get("/usuario/:id", (request, response) -> usuarioService.buscarPorId(request, response));
        put("/usuario/:id", (request, response) -> usuarioService.atualizar(request, response));
        delete("/usuario/:id",(request, response) -> usuarioService.deletar(request, response));
        
        // --- PRESTADOR ---
        post("/cadastro-prestador", (req, res) -> prestadorService.cadastrar(req, res));
        get("/areas-atuacao", (req, res) -> prestadorService.listarAreas(req, res));
        get("/tipos", (req, res) -> prestadorService.listarTipos(req, res));

        // --- POSTS ---
        post("/post", (request, response) -> postService.criarPost(request, response));
        get("/posts", (request, response) -> postService.listarPosts(request, response));
        get("/posts/prestador/:id", (request, response) -> postService.listarPostsPorPrestador(request, response));
        delete("/post/:id", (request, response) -> postService.deletarPost(request, response));
    }
}