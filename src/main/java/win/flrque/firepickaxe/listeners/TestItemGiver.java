package win.flrque.firepickaxe.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import win.flrque.firepickaxe.Main;

import java.util.ArrayList;
import java.util.List;

public class TestItemGiver implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage("Hello miner! Here is your pickaxe!");

        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta meta = pick.getItemMeta();

        List<String> lore = new ArrayList<String>();

        if(meta.hasLore())
            lore.addAll(meta.getLore());

        lore.add(ChatColor.GRAY + plugin.getCustomEnchantment("hot_edge").getName());
        meta.setLore(lore);

        pick.setItemMeta(meta);
        pick.addUnsafeEnchantment(plugin.getCustomEnchantment("hot_edge"), 1);

        player.getInventory().addItem(pick);

    }

}
