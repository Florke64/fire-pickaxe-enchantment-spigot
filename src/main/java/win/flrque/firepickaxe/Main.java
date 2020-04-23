package win.flrque.firepickaxe;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import win.flrque.firepickaxe.enchantments.HotEdgeEnchantment;
import win.flrque.firepickaxe.listeners.EnchantingTableListener;
import win.flrque.firepickaxe.listeners.HotEdgeListener;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private final Map<String, Enchantment> customEnchantments = new HashMap<String, Enchantment>();

    @Override
    public void onEnable() {
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
    public void onDisable() {
        try {
            final Field fI = Enchantment.class.getDeclaredField("byId");
            final Field fN = Enchantment.class.getDeclaredField("byName");

            fI.setAccessible(true);
            fN.setAccessible(true);

            final HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) fI.get(null);
            final HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) fN.get(null);

            for(Enchantment enchantment : customEnchantments.values()) {

                if(byId.containsKey(enchantment)) {
                    byId.remove(enchantment.getKey());
                }

                if(byName.containsKey(enchantment)) {
                    byName.remove(enchantment.getKey());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

            Enchantment.registerEnchantment(enchantment);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //TODO: Field "acceptingNew" = false
        }

        return true;
    }
}
