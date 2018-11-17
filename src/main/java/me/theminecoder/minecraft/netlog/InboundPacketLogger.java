package me.theminecoder.minecraft.netlog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class InboundPacketLogger extends MessageToMessageDecoder<Object> {

    private PacketSession session;

    public InboundPacketLogger(PacketSession session) {
        this.session = session;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        session.handlePacket(msg, Packet.Direction.SERVERBOUND);
        out.add(msg);
    }
}
