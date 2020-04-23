package win.flrque.firepickaxe.listeners;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import win.flrque.firepickaxe.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantingTableListener implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onEnchanting(EnchantItemEvent event) {
        if(!EnchantmentTarget.TOOL.includes(event.getItem()))
            return;

        Random random = new Random();
        int luck = random.nextInt(100);

        if(luck <= 90) {
            applyCustomEnchantment(event.getItem());
        }

    }

    public void applyCustomEnchantment(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<String>();

        if(meta.hasLore())
            lore.addAll(meta.getLore());

        lore.add(ChatColor.GRAY + plugin.getCustomEnchantment("hot_edge").getName());
        meta.setLore(lore);

        item.setItemMeta(meta);
        item.addUnsafeEnchantment(plugin.getCustomEnchantment("hot_edge"), 1);
    }
}
