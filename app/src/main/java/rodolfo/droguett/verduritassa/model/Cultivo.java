package rodolfo.droguett.verduritassa.model;

public class Cultivo {
    private String uidUsuario;
    private String alias;
    private String planta;
    private String fechaSiembra;
    private String fechaCosecha;

    public Cultivo() {

    }

    public Cultivo(String uidUsuario, String alias, String planta, String fechaSiembra, String fechaCosecha) {
        this.uidUsuario = uidUsuario;
        this.alias = alias;
        this.planta = planta;
        this.fechaSiembra = fechaSiembra;
        this.fechaCosecha = fechaCosecha;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getFechaSiembra() {
        return fechaSiembra;
    }

    public void setFechaSiembra(String fechaSiembra) {
        this.fechaSiembra = fechaSiembra;
    }

    public String getFechaCosecha() {
        return fechaCosecha;
    }

    public void setFechaCosecha(String fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
    }
}
