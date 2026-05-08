package model;

import java.sql.Timestamp;

public class Post {
    private int idpost;
    private int idprestador;
    private String titulo;
    private String legenda;
    private String imagemUrl; // <<< ADICIONE ESTA LINHA
    private Timestamp dataenvio;

    public Post() {}

    public int getIdpost() { return idpost; }
    public void setIdpost(int idpost) { this.idpost = idpost; }

    public int getIdprestador() { return idprestador; }
    public void setIdprestador(int idprestador) { this.idprestador = idprestador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getLegenda() { return legenda; }
    public void setLegenda(String legenda) { this.legenda = legenda; }

    // <<< ADICIONE ESTES DOIS MÉTODOS >>>
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public Timestamp getDataenvio() { return dataenvio; }
    public void setDataenvio(Timestamp dataenvio) { this.dataenvio = dataenvio; }
}