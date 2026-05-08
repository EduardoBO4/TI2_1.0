package service;

import dao.PostDAO;
import dao.UsuarioDAO;
import model.Post;
import responseDTO.UsuarioDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import spark.Request;
import spark.Response;
import java.util.List;

public class PostService {
    
    private final PostDAO postDAO = new PostDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    // Usando o Jackson (JsonMapper) exatamente como no UsuarioService
    private final JsonMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    // Método padronizado de erro igual ao do UsuarioService
    private Object erro(String mensagem, int status, Response response) {
        response.status(status);
        return "{\"sucesso\": false, \"mensagem\": \"" + mensagem + "\"}";
    }

    public Object criarPost(Request request, Response response) {
        response.type("application/json");
        
        Post novoPost;
        try {
            // Pega o JSON do body da requisição e converte para o model Post usando Jackson
            novoPost = mapper.readValue(request.body(), new TypeReference<Post>() {});
        } catch (JsonProcessingException e) {
            return erro("Formato JSON inválido", 400, response);
        }
        
        // Busca o usuário pelo ID para conferir se ele existe e qual o tipo dele
        UsuarioDTO usuario = usuarioDAO.buscarPorId(novoPost.getIdprestador());
        
        // Se o usuário não existir no banco
        if (usuario == null) {
            return erro("Usuário não encontrado.", 404, response);
        }
        
        // REGRA: Se o tipoUsuario for diferente de "prestador", bloqueia!
        if (!"prestador".equalsIgnoreCase(usuario.getTipoUsuario())) {
            return erro("Apenas prestadores de serviço podem fazer posts.", 403, response);
        }
        
        // Se chegou aqui, ele é prestador. Vamos salvar no banco!
        boolean sucesso = postDAO.inserir(novoPost);
        
        if (sucesso) {
            response.status(201); // 201 significa "Criado"
            return "{\"sucesso\": true, \"mensagem\": \"Post criado com sucesso!\"}";
        }
        
        return erro("Erro ao salvar no banco de dados.", 500, response);
    }

    public Object listarPosts(Request request, Response response) {
        response.type("application/json");
        
        try {
            List<Post> lista = postDAO.listarTodos();
            return mapper.writeValueAsString(lista);
        } catch (JsonProcessingException e) {
            return erro("Erro ao serializar resposta", 500, response);
        }
    }
    
    public Object deletarPost(Request request, Response response) {
        response.type("application/json");
        
        try {
            int id = Integer.parseInt(request.params(":id"));
            boolean sucesso = postDAO.deletar(id);
            
            if (sucesso) {
                response.status(200);
                return "{\"sucesso\": true, \"mensagem\": \"Post deletado com sucesso.\"}";
            }
            return erro("Post não encontrado.", 404, response);
            
        } catch (NumberFormatException e) {
            return erro("ID inválido", 400, response);
        }
    }
}