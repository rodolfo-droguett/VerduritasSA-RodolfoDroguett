package rodolfo.droguett.verduritassa.model;

public class Usuario {
    private String nombre;
    private String correo;
    private String pais;
    private String genero;

    public Usuario() {}

    public Usuario(String nombre, String correo, String pais, String genero) {
        this.nombre = nombre;
        this.correo = correo;
        this.pais = pais;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
