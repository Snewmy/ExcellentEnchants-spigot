package su.nightexpress.excellentenchants.enchantment.impl.tool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.api.enchantment.ItemCategory;
import su.nightexpress.excellentenchants.api.Modifier;
import su.nightexpress.excellentenchants.api.enchantment.Rarity;
import su.nightexpress.excellentenchants.api.enchantment.data.ChanceData;
import su.nightexpress.excellentenchants.api.enchantment.data.ChanceSettings;
import su.nightexpress.excellentenchants.api.enchantment.type.BlockDropEnchant;
import su.nightexpress.excellentenchants.enchantment.data.AbstractEnchantmentData;
import su.nightexpress.excellentenchants.enchantment.data.ChanceSettingsImpl;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.io.File;
import java.util.*;

import static su.nightexpress.excellentenchants.Placeholders.*;

public class GlassGeneratorEnchant extends AbstractEnchantmentData implements ChanceData, BlockDropEnchant {

    public static final String ID = "glassgenerator";

    private UniSound           sound;
    private boolean            disableOnCrouch;
    private ChanceSettingsImpl chanceSettings;

    private final Set<Material> exemptedBlocks;
    private final Set<FurnaceRecipe> recipes;

    public GlassGeneratorEnchant(@NotNull EnchantsPlugin plugin, @NotNull File file) {
        super(plugin, file);
        this.setDescription("Automatically converts sand into glass when broken.");
        this.setMaxLevel(1);
        this.setRarity(Rarity.UNCOMMON);

        this.exemptedBlocks = new HashSet<>();
        this.recipes = new HashSet<>();
    }

    @Override
    protected void loadAdditional(@NotNull FileConfig config) {
        this.plugin.getServer().recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof FurnaceRecipe furnaceRecipe && furnaceRecipe.getInput().getType().isBlock()) {
                this.recipes.add(furnaceRecipe);
            }
        });

        this.chanceSettings = ChanceSettingsImpl.create(config, Modifier.add(100, 0, 0, 100));

        this.disableOnCrouch = ConfigValue.create("Settings.Disable_On_Crouch",
                true,
                "Sets whether or not enchantment will have no effect when crouching."
        ).read(config);

        this.sound = ConfigValue.create("Settings.Sound",
                UniSound.of(Sound.BLOCK_LAVA_EXTINGUISH),
                "Sound to play on smelting.",
                "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
        ).read(config);

        this.exemptedBlocks.addAll(ConfigValue.forSet("Settings.Exempted_Blocks",
                BukkitThing::getMaterial,
                (cfg, path, set) -> cfg.set(path, set.stream().map(material -> material.getKey().getKey()).toList()),
                Set.of(Material.STONE),
                "List of blocks that are immune to smelter effect."
        ).read(config));
    }

    @NotNull
    @Override
    public ChanceSettings getChanceSettings() {
        return chanceSettings;
    }

    @Override
    @NotNull
    public ItemCategory[] getItemCategories() {
        return new ItemCategory[]{ItemCategory.SHOVEL};
    }

    @Override
    @NotNull
    public EnchantmentTarget getCategory() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean onDrop(@NotNull BlockDropItemEvent event, @NotNull LivingEntity entity, @NotNull ItemStack item, int level) {
        if (this.disableOnCrouch && entity instanceof Player player && player.isSneaking()) return false;

        BlockState state = event.getBlockState();
        if (state instanceof Container || this.exemptedBlocks.contains(state.getType())) return false;

        if (!this.checkTriggerChance(level)) return false;
        if (state.getType() != Material.SAND) return false;

        List<ItemStack> smelts = new ArrayList<>();
        event.getItems().removeIf(drop -> {
            FurnaceRecipe recipe = this.recipes.stream().filter(rec -> rec.getInputChoice().test(drop.getItemStack())).findFirst().orElse(null);
            if (recipe == null) return false;

            smelts.add(recipe.getResult());
            return true;
        });
        if (smelts.isEmpty()) return false;

        smelts.forEach(itemStack -> this.plugin.populateResource(event, itemStack));

        Block block = event.getBlockState().getBlock();
        if (this.hasVisualEffects()) {
            Location location = LocationUtil.getCenter(block.getLocation(), true);
            UniParticle.of(Particle.FLAME).play(location, 0.25, 0.05, 20);
            this.sound.play(location);
        }
        return true;
    }
}
