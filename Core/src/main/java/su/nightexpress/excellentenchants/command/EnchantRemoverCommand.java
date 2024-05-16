package su.nightexpress.excellentenchants.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentenchants.EnchantsPlugin;
import su.nightexpress.excellentenchants.Placeholders;
import su.nightexpress.excellentenchants.config.Lang;
import su.nightexpress.excellentenchants.config.Perms;
import su.nightexpress.excellentenchants.enchantment.util.ColorUtils;
import su.nightexpress.excellentenchants.enchantment.util.ItemBuilder;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Players;

import java.util.Arrays;
import java.util.List;

public class EnchantRemoverCommand extends AbstractCommand<EnchantsPlugin> {

    public static final ItemStack ENCHANT_REMOVER_ITEM = new ItemBuilder(Material.BOOK, 1, ColorUtils.chat("&8- {#A2FC9F}Drag and drop on an item"), ColorUtils.chat("{#A2FC9F}to initiate a chat-sequence"), ColorUtils.chat("{#A2FC9F}to remove one enchantment."), "", ColorUtils.chat("&8- {#A2FC9F}Returns enchantment book")).setDisplayName(ColorUtils.chat("&8> {#A2FC9F}&lEnchantment Remover &8<")).addEnchants(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).addPersistentDataString("enchantment_remover").toItemStack();


    public EnchantRemoverCommand(@NotNull EnchantsPlugin plugin) {
        super(plugin, new String[]{"giveenchantremover"}, Perms.COMMAND_ENCHANT_REMOVER);
        this.setDescription(Lang.COMMAND_ENCHANT_REMOVER_DESC);
        this.setUsage(Lang.COMMAND_ENCHANT_REMOVER_USAGE);
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return Players.playerNames(player);
        }
        if (arg == 2) {
            return Arrays.asList("1", "2", "3", "10");
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 3) {
            this.errorUsage(sender);
            return;
        }

        Player player = Players.getPlayer(result.getArg(1));
        if (player == null) {
            this.errorPlayer(sender);
            return;
        }

        int amount = result.getInt(2, -1);
        if (amount < 1) {
            amount = 1;
        }

        Players.addItem(player, ENCHANT_REMOVER_ITEM);

        Lang.COMMAND_ENCHANT_REMOVER_DONE.getMessage()
                .replace(Placeholders.GENERIC_AMOUNT, amount)
                .replace(Placeholders.forPlayer(player))
                .send(sender);
    }
}
