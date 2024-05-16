package su.nightexpress.excellentenchants.enchantment.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemBuilder {
    public String name;
    public ItemStack item;
    public Material m;
    public int amount;
    public ArrayList<String> lore = new ArrayList<String>();
    public static NamespacedKey nameKey = new NamespacedKey("excellentenchants", "enchantment_remover");

    public ItemBuilder(Material m, int amount, String... lore) {
        this.item = new ItemStack(m, amount);
        ItemMeta im = this.item.getItemMeta();
        im.setLore(Arrays.asList(lore));

        this.item.setItemMeta(im);
        this.amount = amount;
        this.m = m;
    }

    public ItemBuilder(Material m, int amount) {
        this.item = new ItemStack(m, amount);
        this.amount = amount;
        this.m = m;
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public Material getMaterial() {
        return this.m;
    }

    public int getAmount() {
        return this.amount;
    }

    public String getDisplayName() {
        return this.name;
    }

    public ArrayList<String> getLore() {
        return this.lore;
    }

    public ItemBuilder addPersistentDataString(String s){
        ItemMeta im = this.item.getItemMeta();
        im.getPersistentDataContainer().set(nameKey, PersistentDataType.STRING, s);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag){
        ItemMeta im = this.item.getItemMeta();
        im.addItemFlags(flag);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        this.item.setItemMeta(im);
        this.name = name;
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        this.lore = lore;
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchants(Enchantment ench, int level) {
        this.item.addUnsafeEnchantment(ench, level);
        return this;
    }
}