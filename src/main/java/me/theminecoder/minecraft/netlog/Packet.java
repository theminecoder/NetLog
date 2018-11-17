package me.theminecoder.minecraft.netlog;

import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

public class Packet {

    public enum Direction {
        CLIENTBOUND,
        SERVERBOUND
    }

    private String type;
    private Date date = new Date();
    private Direction direction;
    private Map<String, Map<String, PacketField>> fields = Maps.newHashMap();

    public Packet(String name, Direction direction) {
        this.type = name;
        this.direction = direction;
    }

    public String getName() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public Direction getDirection() {
        return direction;
    }

    public Map<String, PacketField> getFields(Class clazz) {
        return fields.computeIfAbsent(clazz.getCanonicalName(), key -> Maps.newHashMap());
    }
}
