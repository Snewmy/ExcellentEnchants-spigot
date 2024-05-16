package su.nightexpress.excellentenchants.enchantment.util;

import org.bukkit.Material;

public class TreeUtils {

    /**
     * Check if a block is a leaves block
     *
     * @param material material of block
     * @return true if block is of type wood
     */
    public static boolean isLeaves(Material material) {
        return switch (material) {
            case ACACIA_LEAVES, BIRCH_LEAVES, DARK_OAK_LEAVES, JUNGLE_LEAVES, OAK_LEAVES, AZALEA_LEAVES, CHERRY_LEAVES,
                 FLOWERING_AZALEA_LEAVES, MANGROVE_LEAVES, SPRUCE_LEAVES -> true;
            default -> false;
        };
    }

    /**
     * Check if a block is a wood block
     *
     * @param material material of block
     * @return true if block is of type wood
     */
    public static boolean isWood(Material material) {
        switch (material) {
            case ACACIA_LOG:
            case BIRCH_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case OAK_LOG:
            case SPRUCE_LOG:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_OAK_LOG:
            case STRIPPED_SPRUCE_LOG:
            case ACACIA_WOOD:
            case BIRCH_WOOD:
            case DARK_OAK_WOOD:
            case JUNGLE_WOOD:
            case OAK_WOOD:
            case SPRUCE_WOOD:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_WOOD:
            case CHERRY_LOG:
            case MANGROVE_LOG:
            case STRIPPED_CHERRY_LOG:
            case STRIPPED_MANGROVE_LOG:
            case CHERRY_WOOD:
            case MANGROVE_WOOD:
            case STRIPPED_CHERRY_WOOD:
            case STRIPPED_MANGROVE_WOOD:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_WOOD: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
