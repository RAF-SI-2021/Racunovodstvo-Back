package raf.si.racunovodstvo.user.model;

public enum PermissionType {
    ADMIN("admin"),
    FINANSIJSKA_OPERATIVA("finop"),
    FINANSIJSKO_KNJIGOVODSTVO("finknj"),
    OBRACUN_ZARADE("obrza"),
    PRODAJA("prodaja");

    public final String label;

    PermissionType(String label) {
        this.label = label;
    }

    public static PermissionType valueOfLabel(String label) {
        for (PermissionType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        return null;
    }
}
