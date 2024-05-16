package su.nightexpress.excellentenchants.enchantment.impl.tool;

import com.gmail.nossr50.events.fake.FakeBlockBreakEvent;
import com.gmail.nossr50.skills.woodcutting.WoodcuttingManager;
import com.gmail.nossr50.util.player.UserManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.api.Modifier;
import su.nightexpress.excellentenchants.api.enchantment.ItemCategory;
import su.nightexpress.excellentenchants.api.enchantment.Rarity;
import su.nightexpress.excellentenchants.api.enchantment.type.BlockBreakEnchant;
import su.nightexpress.excellentenchants.enchantment.data.AbstractEnchantmentData;
import su.nightexpress.excellentenchants.enchantment.util.EnchantUtils;
import su.nightexpress.excellentenchants.enchantment.util.TreeCutter;
import su.nightexpress.excellentenchants.enchantment.util.TreeUtils;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static su.nightexpress.excellentenchants.Placeholders.GENERIC_AMOUNT;

public class TreeCapitatorEnchant extends AbstractEnchantmentData implements BlockBreakEnchant {

    public static final String ID = "treecapitator";
    public static Set<UUID> currentFellers = new HashSet<>();
    public static HashMap<Material, Integer> cuttingSpeed = new HashMap<>();

    private Modifier blocksLimit;
    private boolean disableOnCrouch;

    public TreeCapitatorEnchant(@NotNull EnchantsPlugin plugin, @NotNull File file) {
        super(plugin, file);
        //this.setDescription("Breaks up to " + GENERIC_AMOUNT + " logs of a tree at once.");
        this.setDescription("Breaks all logs of a tree at once.");
        this.setMaxLevel(1);
        this.setRarity(Rarity.VERY_RARE);
        cuttingSpeed.put(Material.NETHERITE_AXE, 0);
        cuttingSpeed.put(Material.DIAMOND_AXE, 0);
        cuttingSpeed.put(Material.IRON_AXE, 1);
        cuttingSpeed.put(Material.GOLDEN_AXE, 1);
        cuttingSpeed.put(Material.STONE_AXE, 3);
        cuttingSpeed.put(Material.WOODEN_AXE, 4);
    }

    @Override
    protected void loadAdditional(@NotNull FileConfig config) {
        this.disableOnCrouch = ConfigValue.create("Settings.Disable_On_Crouch",
                true,
                "Sets whether or not enchantment will have no effect when crouching."
        ).read(config);

        this.blocksLimit = Modifier.read(config, "Settings.Blocks.Limit",
                Modifier.add(90, 0, 0, 90),
                "Max. possible amount of blocks to be mined at the same time.");

        this.addPlaceholder(GENERIC_AMOUNT, level -> String.valueOf(this.getBlocksLimit(level)));
    }

    public int getBlocksLimit(int level) {
        return (int) this.blocksLimit.getValue(level);
    }

    @Override
    @NotNull
    public ItemCategory[] getItemCategories() {
        return new ItemCategory[]{ItemCategory.AXE};
    }

    @NotNull
    @Override
    public EnchantmentTarget getCategory() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean onBreak(@NotNull BlockBreakEvent event, @NotNull LivingEntity entity, @NotNull ItemStack tool, int level) {
        if (!(entity instanceof Player player)) return false;
        if (EnchantUtils.isBusy()) return false;
        if (this.disableOnCrouch && player.isSneaking()) return false;

        Block block = event.getBlock();
        /*
        if (!TreeUtils.isWood(block.getType()))
            return false;
        if (currentFellers.contains(event.getPlayer().getUniqueId()))
            return false;
        if (cuttingSpeed.get(player.getInventory().getItemInMainHand().getType()) == null)
            return false;
        event.setCancelled(true);
        currentFellers.add(event.getPlayer().getUniqueId());
        new TreeCutter(plugin, player, block, getBlocksLimit(level)).runTaskAsynchronously(plugin);

         */
        if (event instanceof FakeBlockBreakEvent) {
            return false;
        }
        UserManager.getPlayer(player).getWoodcuttingManager().processTreeFeller(block.getState());
        return true;
    }
}
