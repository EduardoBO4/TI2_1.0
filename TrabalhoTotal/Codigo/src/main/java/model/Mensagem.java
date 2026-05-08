package model;

public class Mensagem {

    private int prestadorId;
    private int usuarioId;
    private String texto;

    public Mensagem(int prestadorId, int usuarioId, String texto) {
        this.prestadorId = prestadorId;
        this.usuarioId = usuarioId;
        this.texto = texto;
    }

    public int getPrestadorId() {
        return prestadorId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getTexto() {
        return texto;
    }

    public void setPrestadorId(int prestadorId) {
        this.prestadorId = prestadorId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}