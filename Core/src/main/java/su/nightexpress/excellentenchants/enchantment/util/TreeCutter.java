package su.nightexpress.excellentenchants.enchantment.util;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.enchantment.impl.tool.TreeCapitatorEnchant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TreeCutter extends BukkitRunnable {

    private Player player;
    private Block startBlock;
    private List<String> comparisonBlockArray = new ArrayList<>();
    private List<String> scanned = new ArrayList<>();
    private List<String> comparisonBlockArrayLeaves = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();
    private int indexed = 0;
    private boolean loop = false;
    private boolean initialized;
    private EnchantsPlugin plugin;
    private int blockLimit;


    public TreeCutter(EnchantsPlugin plugin, Player cutter, Block startBlock, int blockLimit) {
        this.player = cutter;
        this.startBlock = startBlock;
        this.plugin = plugin;
        this.blockLimit = blockLimit;
    }

    /**
     * Scan an object find all connecting leaves and wood blocks
     *
     * @param block   start block
     * @param centerX center of object, x
     * @param centerZ center of object, z
     */
    public void runLoop(Block block, final int centerX, final int centerZ) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;
                    Block blockRelative = block.getRelative(x, y, z);
                    String s = blockRelative.getX() + ":" + blockRelative.getY() + ":" + blockRelative.getZ();
                    if (scanned.contains(s)) {
                        continue;
                    }
                    scanned.add(s);
                    if (TreeUtils.isLeaves(blockRelative.getType()) && !comparisonBlockArrayLeaves.contains(s)) {
                        comparisonBlockArrayLeaves.add(s);
                        continue;
                    }

                    if (TreeUtils.isWood(blockRelative.getType())) {
                        int searchSquareSize = 25;
                        if (blockRelative.getX() > centerX + searchSquareSize || blockRelative.getX() < centerX - searchSquareSize
                                || blockRelative.getZ() > centerZ + searchSquareSize || blockRelative.getZ() < centerZ - searchSquareSize)
                            break;
                        if (!comparisonBlockArray.contains(s)) {
                            comparisonBlockArray.add(s);
                            blocks.add(blockRelative);
                            this.runLoop(blockRelative, centerX, centerZ);
                        }
                    }
                }
            }
        }
    }

    /**
     * Chop down a tree if the object is a tree
     */
    @Override
    public void run() {
        if (initialized) {
            return;
        }
        initialized = true;
        blocks.add(startBlock);
        runLoop(startBlock, startBlock.getX(), startBlock.getZ());

        if (isTree()) {
            cutDownTree();
        } else {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Location center = startBlock.getLocation().add(0.5, 0.5, 0.5);
                    for (ItemStack stack : startBlock.getDrops())
                        startBlock.getWorld().dropItem(center, stack);
                    startBlock.getWorld().playEffect(center, Effect.STEP_SOUND, startBlock.getType());
                    startBlock.setType(Material.AIR);
                }
            }.runTask(plugin);
        }
        stop();
    }

    /**
     * Compare the amount of leaves to wood blocks to determine whether the object is tree or not
     *
     * @return true if object is a tree
     */
    private boolean isTree() {
        return (comparisonBlockArrayLeaves.size() * 1D) / (blocks.size() * 1D) > 0.3;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    private void stop() {
        TreeCapitatorEnchant.currentFellers.remove(player.getUniqueId());
    }

    private void cutDownTree() {
        if ((player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
            stop();
            return;
        }

        if (!TreeCapitatorEnchant.currentFellers.contains(player.getUniqueId()))
            TreeCapitatorEnchant.currentFellers.add(player.getUniqueId());

        blocks = blocks.stream().sorted((b, b2) -> b2.getY() - b.getY()).collect(Collectors.toList());
        long speed = TreeCapitatorEnchant.cuttingSpeed.get(player.getInventory().getItemInMainHand().getType());

        new BukkitRunnable() {
            int blocksCut = 0;

            @Override
            public void run() {
                //Instant cut down tree
                if (!loop) {
                    for (int i = 0; i < blocks.size(); i++) {
                        loop = true;
                        this.run();
                    }
                    this.cancel();
                    return;
                }

                //In case player disconnect
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                Integer speed = TreeCapitatorEnchant.cuttingSpeed.get(item.getType());
                if (speed == null) {
                    cutDownTree();
                    this.cancel();
                    return;
                }

                if (blocks.size() < indexed - 2) {
                    this.cancel();
                    return;
                }

                Block block = blocks.get(indexed++);

                //Fire events
                PlayerAnimationEvent animationEvent = new PlayerAnimationEvent(player);
                Bukkit.getPluginManager().callEvent(animationEvent);
                BlockBreakEvent event = new BlockBreakEvent(block, player);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    Location center = block.getLocation().add(0.5, 0.5, 0.5);
                    for (ItemStack drop : block.getDrops()) {
                        startBlock.getWorld().dropItem(center, drop);
                    }

                    item.setDurability((short) (item.getDurability() + 1));
                    if (item.getType().getMaxDurability() == item.getDurability())
                        player.getInventory().setItemInMainHand(null);

                    blocksCut++;
                    block.setType(Material.AIR);
                }

                if (blocks.size() <= indexed || blocksCut >= blockLimit)
                    this.cancel();
            }

            @Override
            public void cancel() {
                stop();
                super.cancel();
            }


        }.runTaskTimer(plugin, 0L, speed);
    }

}
