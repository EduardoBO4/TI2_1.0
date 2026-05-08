package FilterDTO;

public class PostFilterDTO {
    
    private Integer idprestador;
    private String titulo;

    // Construtor vazio
    public PostFilterDTO() {}

    // Getters e Setters
    public Integer getIdprestador() {
        return idprestador;
    }

    public void setIdprestador(Integer idprestador) {
        this.idprestador = idprestador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}