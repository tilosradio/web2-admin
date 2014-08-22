package hu.radio.tilos.model.type;

public enum MixCategory implements DescriptiveType {
    DJ("Tilos DJ"),
    GUEST_DJ("Vendég DJ"),
    SHOW("Beszélgetős műsor"),
    PARTY("Tilos party"),
    TALE("Tilos mese");

    private String description;

    MixCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
