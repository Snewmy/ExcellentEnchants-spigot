package su.nightexpress.excellentenchants.enchantment.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.Placeholders;
import su.nightexpress.excellentenchants.api.enchantment.EnchantmentData;
import su.nightexpress.excellentenchants.command.EnchantRemoverCommand;
import su.nightexpress.excellentenchants.config.Lang;
import su.nightexpress.excellentenchants.enchantment.util.EnchantUtils;
import su.nightexpress.excellentenchants.enchantment.util.ItemBuilder;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Players;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnchantRemoverListener implements Listener {

    EnchantsPlugin plugin;
    Map<Player, ItemStack> removingEnchant = new HashMap<Player, ItemStack>();

    public EnchantRemoverListener(EnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
            Player player = (Player) event.getWhoClicked();
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType() == Material.AIR) {
                return;
            }
            if (event.getView().getBottomInventory() != event.getClickedInventory()) {
                return;
            }
            if (cursor.getType() != Material.BOOK) {
                return;
            }
            ItemMeta im = cursor.getItemMeta();
            if (!im.getPersistentDataContainer().has(ItemBuilder.nameKey)) {
                return;
            }
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }
            Map<EnchantmentData, Integer> customEnchants = EnchantUtils.getCustomEnchantments(clickedItem);
            if (customEnchants.size() == 0) {
                return;
            }
            if (removingEnchant.containsKey(player)) {
                removingEnchant.remove(player);
            }
            event.setCancelled(true);
            removingEnchant.put(player, clickedItem);
            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
            } else {
                player.setItemOnCursor(null);
            }
            Lang.ENCHANT_REMOVER_INITIATE.getMessage().replace(Placeholders.ENCHANTMENTS_ON_ITEM, EnchantUtils.getFormattedListOfEnchants(customEnchants)).send(player);
            player.closeInventory();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (!removingEnchant.containsKey(player)) {
            return;
        }
        event.setCancelled(true);
        if (message.toLowerCase().equalsIgnoreCase("cancel")) {
            cancelEnchantRemoval(player);
            return;
        }
        ItemStack item = removingEnchant.get(player);
        List<String> enchantIdList = EnchantUtils.getListEnchantIds(EnchantUtils.getCustomEnchantments(item));
        if (!enchantIdList.contains(message.toLowerCase())) {
            Lang.ENCHANT_REMOVER_NOT_ENCHANT.getMessage().send(player);
            return;
        }
        Enchantment enchantment = BukkitThing.getEnchantment(message.toLowerCase());
        int level = item.getEnchantmentLevel(enchantment);
        item.removeEnchantment(enchantment);
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantUtils.add(book, enchantment, level, true);
        Players.addItem(player, book);
        Lang.ENCHANT_REMOVER_SUCCESS.getMessage().send(player);
    }

    public void cancelEnchantRemoval(Player player) {
        removingEnchant.remove(player);
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(EnchantRemoverCommand.ENCHANT_REMOVER_ITEM);
        for (Map.Entry<Integer, ItemStack> integerItemStackEntry : leftovers.entrySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(), integerItemStackEntry.getValue());
        }
        Lang.ENCHANT_REMOVER_CANCELLED.getMessage().send(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (removingEnchant.containsKey(event.getPlayer())) {
            cancelEnchantRemoval(event.getPlayer());
        }
    }
}
