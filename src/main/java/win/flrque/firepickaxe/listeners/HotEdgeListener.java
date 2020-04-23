package win.flrque.firepickaxe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import win.flrque.firepickaxe.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class HotEdgeListener implements Listener {

    private final Main plugin;

    private final Map<Material, CookingRecipe> recipes = new HashMap<>();

    public HotEdgeListener() {
        plugin = Main.getPlugin(Main.class);

        plugin.getLogger().log(Level.INFO, "Loaded furnace recipes...");

        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            final Recipe recipe = recipeIterator.next();

            if(!(recipe instanceof CookingRecipe))
                continue;

            if(plugin.getScanRecipeType() == 0 && !(recipe instanceof BlastingRecipe))
                continue;

            final Material input = ((CookingRecipe) recipe).getInput().getType();
            final CookingRecipe cookingRecipe = ((CookingRecipe) recipe);
            recipes.put(input, cookingRecipe);

            plugin.getLogger().log(Level.INFO, "Loaded furnace recipe: [" + input + ", " + cookingRecipe + "]");

        }

        plugin.getLogger().log(Level.INFO, "Recipe loading finished.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO: add isCancelled check
        Player player = event.getPlayer();
        if(player == null)
            return;

        if(!event.isDropItems())
            return;

        GameMode gameMode = player.getGameMode();
        if(gameMode.equals(GameMode.CREATIVE) || gameMode.equals(GameMode.SPECTATOR))
            return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        if(tool == null || !EnchantmentTarget.TOOL.includes(tool))
            return;

        if(!tool.hasItemMeta() || !tool.getItemMeta().hasEnchant(plugin.getCustomEnchantment("hot_edge")))
            return;

        Block brokenBlock = event.getBlock();
        if(brokenBlock.getDrops().isEmpty())
            return;

        for(ItemStack originalDrop : brokenBlock.getDrops()) {
            if(recipes.containsKey(originalDrop.getType())) {
                ItemStack recipeResult = recipes.get(originalDrop.getType()).getResult();
                float experience = recipes.get(originalDrop.getType()).getExperience();
                spawnDrop(recipeResult, (int) experience, brokenBlock.getLocation());
                event.setDropItems(false);
            }
        }

    }

    private void spawnDrop(ItemStack itemStack, int exp, Location location) {
        location.getWorld().dropItemNaturally(location, itemStack);
        ExperienceOrb orbs = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        orbs.setExperience(exp);
    }

}
