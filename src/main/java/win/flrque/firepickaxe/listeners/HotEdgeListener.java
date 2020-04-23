package win.flrque.firepickaxe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import win.flrque.firepickaxe.Main;

import java.util.Iterator;

public class HotEdgeListener implements Listener {

    private final Main plugin;

    public HotEdgeListener() {
        plugin = Main.getPlugin(Main.class);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player == null)
            return;

        boolean hasDrops = event.isDropItems();
        if(!hasDrops)
            return;

        ItemStack tool = player.getInventory().getItemInMainHand();
        if(tool == null || !EnchantmentTarget.TOOL.includes(tool))
            return;

        if(!tool.hasItemMeta() || !tool.getItemMeta().hasEnchant(plugin.getCustomEnchantment("hot_edge")))
            return;

        Block brokenBlock = event.getBlock();

        ItemStack blockItemStack = new ItemStack(brokenBlock.getType());

        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if(!(recipe instanceof BlastingRecipe))
                continue;

            if(((BlastingRecipe) recipe).getInput().isSimilar(blockItemStack)) {
                brokenBlock.getDrops().clear();
                event.setDropItems(false);

                ItemStack result = recipe.getResult();
                float experience = ((BlastingRecipe) recipe).getExperience();

                brokenBlock.getLocation().getWorld().dropItemNaturally(brokenBlock.getLocation(), result);
                ExperienceOrb orbs = (ExperienceOrb) brokenBlock.getLocation().getWorld().spawnEntity(brokenBlock.getLocation(), EntityType.EXPERIENCE_ORB);
                orbs.setExperience((int) experience);

                break;
            }
        }

        player.sendMessage("Boop!");

    }

}
