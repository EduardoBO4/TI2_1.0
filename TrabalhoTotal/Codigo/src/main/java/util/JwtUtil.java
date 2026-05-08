package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "resolve-aqui-chave-secreta"; 
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final long EXPIRACAO_MS = 1000 * 60 * 60 * 8; // 8 Horas de validade

    public static String gerarToken(int idUsuario, String email, String tipoUsuario) {
        return JWT.create()
                .withSubject(String.valueOf(idUsuario))
                .withClaim("email", email)
                .withClaim("tipoUsuario", tipoUsuario)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRACAO_MS))
                .sign(ALGORITHM);
    }

    public static DecodedJWT validarToken(String token) throws JWTVerificationException {
        return JWT.require(ALGORITHM)
                .build()
                .verify(token);
    }

    public static String extrairTokenDoHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    //PEGA O ID DO USUARIO PELO TOKEN
    public static int getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = validarToken(token);
        if (decodedJWT != null) {
            try {
                return Integer.parseInt(decodedJWT.getSubject());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
    
    //PEGA O EMMAIL DO USUARIO
    public static String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = validarToken(token);
        if (decodedJWT != null) {
            return decodedJWT.getClaim("email").asString();
        }
        return null;
    }
    
    //PEGA O ID DO USUARIO LOGADO A PARTIR DA REQUEST
    public static int getUserIdFromRequest(spark.Request request) {
        String authHeader = request.headers("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return -1;
        }
        
        String token = authHeader.substring(7);
        return getUserIdFromToken(token);
    }
    
    //PEGA O ID DO USUARIO VERDE DE UM TOKEN EXTERNO
    public static Integer getIdUsuarioVerdeFromToken(String token) {
        try {
            // DECODIFICA O TOKEN
            DecodedJWT decodedJWT = JWT.decode(token);
            
            if (decodedJWT.getClaim("userId") != null && !decodedJWT.getClaim("userId").isNull()) {
                return decodedJWT.getClaim("userId").asInt();
            }
            
            System.err.println("Não foi possível extrair userId do token Verde");
            return null;
            
        } catch (Exception e) {
            System.err.println("Erro ao decodificar token Verde: " + e.getMessage());
            return null;
        }
    }
    
    //VERIFICA SE O TOKEN JA EXPIROU
    public static boolean isTokenExpired(String token) {
        DecodedJWT decodedJWT = validarToken(token);
        if (decodedJWT != null) {
            return decodedJWT.getExpiresAt().before(new Date());
        }
        return true;
    }
}