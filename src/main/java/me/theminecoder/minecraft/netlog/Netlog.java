package me.theminecoder.minecraft.netlog;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.ChannelPipeline;
import me.theminecoder.minecraft.netlog.nms.CraftPlayer;
import me.theminecoder.minecraft.nmsproxy.proxy.NMSProxyProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class Netlog extends JavaPlugin implements Listener {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<Player, PacketSession> packetSessions = Maps.newHashMap();

    private static List<String> ignoredPackets;
    private NMSProxyProvider nmsProxyProvider;

    public static List<String> getIgnoredPackets() {
        return ignoredPackets;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        ignoredPackets = this.getConfig().getStringList("ignored-packets");
        this.nmsProxyProvider = NMSProxyProvider.get(this);
        this.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getOnlinePlayers().forEach(player -> this.onPlayerJoin(new PlayerJoinEvent(player, null)));
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> this.onPlayerQuit(new PlayerQuitEvent(player, null)));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PacketSession session = new PacketSession(new Date());
        nmsProxyProvider.getNMSObject(CraftPlayer.class, event.getPlayer())
                .getHandle()
                .playerConnection()
                .networkManager()
                .channel()
                .pipeline()
                .addBefore("packet_handler", "netlog_inbound_logger", new InboundPacketLogger(session))
                .addLast("netlog_outbound_logger", new OutboundPacketLogger(session));
        packetSessions.put(event.getPlayer(), session);
        this.getLogger().info("Started logging " + event.getPlayer().getName() + "('s) packets");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            ChannelPipeline pipeline = nmsProxyProvider.getNMSObject(CraftPlayer.class, event.getPlayer())
                    .getHandle()
                    .playerConnection()
                    .networkManager()
                    .channel()
                    .pipeline();
            pipeline.remove("debug_inbound_logger");
            pipeline.remove("debug_outbound_logger");
        } catch (NoSuchElementException ignored) {
        }

        this.getLogger().info("Stopped logging " + event.getPlayer().getName() + "('s) packets");

        PacketSession session = packetSessions.get(event.getPlayer());
        if (session != null) {
            session.setStop(new Date());

            File sessionFolder = new File(this.getDataFolder(), event.getPlayer().getUniqueId().toString());
            if (!sessionFolder.exists()) {
                sessionFolder.mkdirs();
            }

            int dateSessionNumder = 1;
            File sessionFile;
            do {
                sessionFile = new File(sessionFolder, DATE_FORMAT.format(session.getStart()) + "_" + (dateSessionNumder++) + ".json");
            } while (sessionFile.exists());

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(sessionFile), Charset.forName("UTF-8"))) {
                GSON.toJson(session, writer);
                this.getLogger().info("Wrote " + event.getPlayer().getName() + "('s) packets to " + sessionFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
