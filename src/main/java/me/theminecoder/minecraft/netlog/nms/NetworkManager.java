package me.theminecoder.minecraft.netlog.nms;

import io.netty.channel.Channel;
import me.theminecoder.minecraft.nmsproxy.NMSProxy;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSClass;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSField;

@NMSClass(type = NMSClass.Type.NMS, className = "NetworkManager")
public interface NetworkManager extends NMSProxy {

    @NMSField(type = NMSField.Type.GETTER)
    Channel channel();

}
