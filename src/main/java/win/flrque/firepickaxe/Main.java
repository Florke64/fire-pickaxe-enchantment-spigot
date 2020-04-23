package win.flrque.firepickaxe;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import win.flrque.firepickaxe.enchantments.HotEdgeEnchantment;
import win.flrque.firepickaxe.listeners.EnchantingTableListener;
import win.flrque.firepickaxe.listeners.HotEdgeListener;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private final Map<String, Enchantment> customEnchantments = new HashMap<String, Enchantment>();

    private int enchantmentChance;
    private int scanRecipeType;

    @Override
    public void onEnable() {

        reloadConfig();

        getServer().getPluginManager().registerEvents(new HotEdgeListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantingTableListener(), this);

        customEnchantments.put("hot_edge", new HotEdgeEnchantment("hot_edge"));

        if(!registerEnchantments()) {
            getLogger().log(Level.SEVERE, "Error while registering custom Enchantment(s)!");
            getLogger().log(Level.SEVERE, "Plugin will now go off...");
            getPluginLoader().disablePlugin(this);
        };
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        saveDefaultConfig();
        enchantmentChance = getConfig().getInt("custom_enchantment_chance", 1);
        enchantmentChance = enchantmentChance > 100? 100 : enchantmentChance < 0 ? 1 : enchantmentChance;

        scanRecipeType = getConfig().getString("check_for_recipe_type", "blasting") == "blasting" ? 0 : 1;
        scanRecipeType = scanRecipeType > 1? 1 : scanRecipeType < 0 ? 0 : scanRecipeType;
    }

    @Override
    public void onDisable() {
        try {
            final Field fieldByKey = Enchantment.class.getDeclaredField("byKey");
            final Field fieldByName = Enchantment.class.getDeclaredField("byName");
//
            fieldByKey.setAccessible(true);
            fieldByName.setAccessible(true);
//
            final HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) fieldByKey.get(null);
            final HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) fieldByName.get(null);

            for(Enchantment enchantment : customEnchantments.values()) {

                if(byKey.containsKey(enchantment.getKey())) {
                    byKey.remove(enchantment.getKey());
                }

                if(byName.containsKey(enchantment.getName())) {
                    byName.remove(enchantment.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getEnchantmentChance() {
        return enchantmentChance;
    }

    public int getScanRecipeType() {
        return scanRecipeType;
    }

    public List<Enchantment> getCustomEnchantments() {
        return (List<Enchantment>) customEnchantments.values();
    }

    public Enchantment getCustomEnchantment(String nameKey) {
        return customEnchantments.getOrDefault(nameKey, null);
    }

    private boolean registerEnchantments() {
        int failedRegistrations = 0;
        for(Enchantment enchantment : customEnchantments.values()) {
            if(!registerEnchantment(enchantment))
                failedRegistrations ++;
        }

        return failedRegistrations < 1;
    }

    private boolean registerEnchantment(Enchantment enchantment) {
        final Field field;

        try {
            field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);

            if(Enchantment.getByKey(enchantment.getKey()) == null) {
                Enchantment.registerEnchantment(enchantment);
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }
}
