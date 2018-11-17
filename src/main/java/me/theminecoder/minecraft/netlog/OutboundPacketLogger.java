package me.theminecoder.minecraft.netlog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class OutboundPacketLogger extends MessageToMessageEncoder<Object> {

    private PacketSession session;

    public OutboundPacketLogger(PacketSession session) {
        this.session = session;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        session.handlePacket(msg, Packet.Direction.CLIENTBOUND);
        out.add(msg);
    }
}
