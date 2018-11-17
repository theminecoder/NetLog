package me.theminecoder.minecraft.netlog;

public class PacketField {

    private String type;
    private String value;

    public PacketField(String type, Object value) {
        this.type = type;
        this.value = value+"";
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
