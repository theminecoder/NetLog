package me.theminecoder.minecraft.netlog.nms;

import me.theminecoder.minecraft.nmsproxy.NMSProxy;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSClass;
import me.theminecoder.minecraft.nmsproxy.annotations.NMSMethod;

@NMSClass(type = NMSClass.Type.CRAFTBUKKIT, className = "entity.CraftPlayer")
public interface CraftPlayer extends NMSProxy {

    @NMSMethod
    EntityPlayer getHandle();

}
