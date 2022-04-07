package raf.si.racunovodstvo.user.model;

public enum PermissionType {
    PROFIL("Profil"),
    EVIDENCIJE("Evidencije"),
    NABAVKE("Nabavke"),
    PRODAJA("Prodaja"),
    IZVESTAJI("Izveštaji"),
    FINANSIJSKA_OPERATIVA("Finansijska operativa"),
    FINANSIJSKO_KNJIGOVODSTVO("Finansijsko knjigovodstvo"),
    OBRACUN_ZARADE("Obračun zarade");

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
