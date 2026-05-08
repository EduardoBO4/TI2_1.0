package service;

import dao.ConversaDAO;
import model.Conversa;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import spark.Request;
import spark.Response;
import java.util.List;

public class ConversaService {

    private final ConversaDAO conversaDAO = new ConversaDAO();

    private final JsonMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    private Object erro(String mensagem, int status, Response response) {
        response.status(status);
        return "{\"sucesso\": false, \"mensagem\": \"" + mensagem + "\"}";
    }

    public Object inserir(Request request, Response response) {
        response.type("application/json");

        Conversa conversa;
        try {
            conversa = mapper.readValue(request.body(), new TypeReference<Conversa>() {});
        } catch (JsonMappingException e) {
            return erro("Formato JSON inválido", 400, response);
        } catch (JsonProcessingException e) {
            return erro("Erro ao processar JSON", 400, response);
        }

        if (conversa.getTexto() == null || conversa.getTexto().trim().isEmpty()) {
            return erro("O texto da mensagem é obrigatório", 400, response);
        }

        boolean sucesso = conversaDAO.inserir(conversa);

        if (sucesso) {
            response.status(201);
            return "{\"sucesso\": true, \"mensagem\": \"Mensagem enviada com sucesso\"}";
        }
        return erro("Erro ao enviar a mensagem", 500, response);
    }

    public Object listar(Request request, Response response) {
        response.type("application/json");

        try {
            List<Conversa> lista = conversaDAO.listar();
            return mapper.writeValueAsString(lista);
        } catch (JsonProcessingException e) {
            return erro("Erro ao serializar resposta", 500, response);
        }
    }

    public Object atualizar(Request request, Response response) {
        response.type("application/json");

        try {
            int id = Integer.parseInt(request.params(":id"));
            Conversa conversa = mapper.readValue(request.body(), new TypeReference<Conversa>() {});
            conversa.setIdMensagem(id);

            boolean sucesso = conversaDAO.atualizar(conversa);

            if (sucesso) {
                return "{\"sucesso\": true, \"mensagem\": \"Mensagem atualizada com sucesso\"}";
            }
            return erro("Mensagem não encontrada", 404, response);
        } catch (NumberFormatException e) {
            return erro("ID inválido", 400, response);
        } catch (JsonProcessingException e) {
            return erro("Formato JSON inválido", 400, response);
        }
    }

    public Object deletar(Request request, Response response) {
        response.type("application/json");

        try {
            int id = Integer.parseInt(request.params(":id"));
            boolean sucesso = conversaDAO.deletar(id);

            if (sucesso) {
                return "{\"sucesso\": true, \"mensagem\": \"Mensagem deletada com sucesso\"}";
            }
            return erro("Mensagem não encontrada", 404, response);
        } catch (NumberFormatException e) {
            return erro("ID inválido", 400, response);
        }
    }
}