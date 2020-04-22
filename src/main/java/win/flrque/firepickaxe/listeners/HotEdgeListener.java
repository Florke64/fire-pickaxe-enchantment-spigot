package win.flrque.firepickaxe.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import win.flrque.firepickaxe.Main;

public class HotEdgeListener implements Listener {

    private final Main plugin;

    public HotEdgeListener() {
        plugin = Main.getPlugin(Main.class);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player == null) return;

        player.sendMessage("Boop!");

    }

}
