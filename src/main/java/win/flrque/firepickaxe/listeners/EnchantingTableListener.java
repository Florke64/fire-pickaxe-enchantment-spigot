package win.flrque.firepickaxe.listeners;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
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

        if(luck <= plugin.getEnchantmentChance()) {
            int enchantmentNo = random.nextInt(plugin.getCustomEnchantments().size());
            applyCustomEnchantment(plugin.getCustomEnchantments().get(enchantmentNo), event.getItem());
        }

    }

    public void applyCustomEnchantment(Enchantment enchantment, ItemStack item) {
        applyCustomEnchantment(enchantment, item, enchantment.getMaxLevel());
    }

    public void applyCustomEnchantment(Enchantment enchantment, ItemStack item, int level) {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<String>();

        if(meta.hasLore())
            lore.addAll(meta.getLore());

        lore.add(ChatColor.GRAY + enchantment.getName());
        meta.setLore(lore);

        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment, level);
    }
}
