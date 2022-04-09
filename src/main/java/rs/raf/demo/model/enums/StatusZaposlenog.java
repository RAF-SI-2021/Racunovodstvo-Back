package rs.raf.demo.model.enums;

public enum StatusZaposlenog {
    ZAPOSLEN("zaposlen"),NEZAPOSLEN("nezaposlen");

    public final String label;

    StatusZaposlenog(String zaposlen) {
        this.label = zaposlen;
    }

    public static StatusZaposlenog valueOfLabel(String label) {
        for (StatusZaposlenog type : values()) {
            if (type.label.equals(label.toLowerCase())) {
                return type;
            }
        }
        return null;
    }
}
