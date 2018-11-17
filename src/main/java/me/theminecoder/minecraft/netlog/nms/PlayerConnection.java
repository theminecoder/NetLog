package me.theminecoder.minecraft.netlog.nms;

import me.theminecoder.minecraft.nmsproxy.NMSProxy;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSClass;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSField;

@NMSClass(type = NMSClass.Type.NMS, className = "PlayerConnection")
public interface PlayerConnection extends NMSProxy {

    @NMSField(type = NMSField.Type.GETTER)
    NetworkManager networkManager();

}
