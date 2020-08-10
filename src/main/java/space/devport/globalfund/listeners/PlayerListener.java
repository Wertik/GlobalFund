package space.devport.globalfund.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import space.devport.globalfund.GlobalFundPlugin;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        GlobalFundPlugin.getInstance().getRecordManager().unloadRecord(event.getPlayer().getUniqueId());
    }
}
