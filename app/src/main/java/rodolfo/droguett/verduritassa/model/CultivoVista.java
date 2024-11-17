package rodolfo.droguett.verduritassa.model;

public class CultivoVista {
    private String alias;
    private String fechaCosecha;
    private String planta;
    private String fechaSiembra;
    private String uidUsuario;
    private String documentId;

    public CultivoVista() {
    }

    public CultivoVista(String alias, String fechaCosecha, String planta, String fechaSiembra, String uidUsuario) {
        this.alias = alias;
        this.fechaCosecha = fechaCosecha;
        this.planta = planta;
        this.fechaSiembra = fechaSiembra;
        this.uidUsuario = uidUsuario;

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFechaCosecha() {
        return fechaCosecha;
    }

    public void setFechaCosecha(String fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
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

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}