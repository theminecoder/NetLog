package me.theminecoder.minecraft.netlog;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PacketSession {

    private Date start;
    private Date stop;

    private List<Packet> packets = Lists.newArrayList();

    public PacketSession(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public List<Packet> getPackets() {
        return packets;
    }

    void handlePacket(Object msg, Packet.Direction direction) {
        if (!Netlog.getIgnoredPackets().contains(msg.getClass().getSimpleName())) {
            Packet packet = new Packet(msg.getClass().getCanonicalName(), direction);
            Class packetClass = msg.getClass();
            do {
                Class finalPacketClass = packetClass;
                Arrays.stream(msg.getClass().getDeclaredFields()).forEach(field -> {
                    try {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        String type;
                        if (field.getType().isPrimitive()) {
                            type = field.getType().getName();
                        } else {
                            type = field.getType().getCanonicalName();
                        }
                        try {
                            Object value = field.get(msg);
                            if (field.getType().isArray() && value != null)
                                value = Arrays.toString((Object[]) value);
                            packet.getFields(finalPacketClass).put(field.getName(), new PacketField(type, value));
                        } catch (ReflectiveOperationException | ClassCastException e) {
                            packet.getFields(finalPacketClass).put(field.getName(), new PacketField(type, "?"));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        throw e;
                    }
                });
                packetClass = packetClass.getSuperclass();
                if (packetClass == Object.class) {
                    packetClass = null;
                }
            } while (packetClass != null);
            packets.add(packet);
        }
    }

}
