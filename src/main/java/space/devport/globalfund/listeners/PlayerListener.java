package space.devport.globalfund.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import space.devport.globalfund.GlobalFundPlugin;

public class PlayerListener implements Listener {

    private final GlobalFundPlugin plugin;

    public PlayerListener(GlobalFundPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.plugin.getRecordManager().unloadRecord(event.getPlayer().getUniqueId());
    }
}
