package rs.raf.demo.model.enums;

public enum RadnaPozicija {
    DIREKTOR("direktor"),RADNIK("radnik"),MENADZER("menadzer");

    public final String label;

    RadnaPozicija(String label) {
        this.label = label;
    }

    public static RadnaPozicija valueOfLabel(String label) {
        for (RadnaPozicija type : values()) {
            if (type.label.equals(label.toLowerCase())) {
                return type;
            }
        }
        return null;
    }
}
