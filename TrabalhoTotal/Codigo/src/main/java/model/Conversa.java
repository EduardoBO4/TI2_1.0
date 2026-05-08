package model;

public class Conversa {

    private int idMensagem;
    private int idprestador;
    private int idusuario;
    private String texto;

    public Conversa(int idMensagem, int idprestador, int idusuario, String texto) {
        this.idMensagem = idMensagem;
        this.idprestador = idprestador;
        this.idusuario = idusuario;
        this.texto = texto;
    }

     public int getIdMensagem() {
        return idMensagem;
    }

    public int getIdPrestador() {
        return idprestador;
    }

    public int getIdUsuario() {
        return idusuario;
    }

    public String getTexto() {
        return texto;
    }

    public void  setIdMensagem(int idMensagem){
        this.idMensagem = idMensagem;
    }

    public void setIdPrestador(int idprestador) {
        this.idprestador = idprestador;
    }

    public void setIdUsuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}