package su.nightexpress.excellentenchants.enchantment.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.enchantment.util.ColorUtils;

public class ConversionListener implements Listener {

    EnchantsPlugin plugin;

    public ConversionListener(EnchantsPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onConvertInv(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.PICKUP_ALL) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }
            if (event.getView().getBottomInventory() == event.getClickedInventory()) {
                ItemMeta im = clickedItem.getItemMeta();
                if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
                    EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) im;
                    checkOutdatedEnchantBook(clickedItem, player, bookMeta, "aura");
                    checkOutdatedEnchantBook(clickedItem, player, bookMeta, "getaway");
                    checkOutdatedEnchantBook(clickedItem, player, bookMeta, "dexterity");
                } else {
                checkOutdatedEnchant(clickedItem, player, im, "aura");
                checkOutdatedEnchant(clickedItem, player, im, "getaway");
                checkOutdatedEnchant(clickedItem, player, im, "dexterity");
                }
            }
        }
    }

    public void checkOutdatedEnchant(ItemStack clickedItem, Player player, ItemMeta im, String enchantName) {
        if (im.hasEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantName)))) {
            im.removeEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantName)));
            player.sendMessage(ColorUtils.chat("&fHi, sorry we removed the enchantment #7cdcde" + enchantName +"&f from your item, and the server, but here is #7cdcdeThree Thunder Keys!"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "givekeys " + player.getName() + " thunder 3");
            clickedItem.setItemMeta(im);
            this.plugin.getServer().getLogger().info("Removed enchant for " + player.getName() + ".");
        }
    }

    public void checkOutdatedEnchantBook(ItemStack clickedItem, Player player, EnchantmentStorageMeta im, String enchantName) {
        if (im.hasStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantName)))) {
            player.sendMessage(ColorUtils.chat("&fHi, sorry we removed your enchanted book with #7cdcde" + enchantName +"&f, and from the server, but here is #7cdcdeThree Thunder Keys!"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "givekeys " + player.getName() + " thunder 3");
            ItemStack itemToRemove = clickedItem;
            itemToRemove.setAmount(1);
            player.getInventory().removeItem(itemToRemove);
            this.plugin.getServer().getLogger().info("Removed enchant for " + player.getName() + ".");
        }
    }
}
