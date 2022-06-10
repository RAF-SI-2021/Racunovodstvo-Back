package raf.si.racunovodstvo.user.model;

public enum PermissionType {
    PROFIL("profil"),
    EVIDENCIJE("evidencije"),
    NABAVKE("nabavke"),
    IZVEŠTAJI("izvestaji"),
    PRODAJA("prodaja"),
    FINANSIJSKA_OPERATIVA("finop"),
    FINANSIJSKO_KNJIGOVODSTVO("finknj"),
    NABAVKA("nabavka"),
    OBRACUN_ZARADE("obrza");

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
