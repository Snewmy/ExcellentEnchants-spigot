package su.nightexpress.excellentenchants.enchantment.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    public static final Pattern patternBrackets = Pattern.compile("\\{#[0-9a-fA-F]{6}\\}");
    public static final Pattern pattern = Pattern.compile("#[0-9a-fA-F]{6}");

    public ColorUtils() {
    }

    public static String chat(String s) {
        s = removeBrackets(s);

        for(Matcher match = pattern.matcher(s); match.find(); match = pattern.matcher(s)) {
            String color = s.substring(match.start(), match.end());
            s = s.replace(color, "" + ChatColor.of(color));
        }

        return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
    }

    private static String removeBrackets(String text) {
        Matcher m = patternBrackets.matcher(text);

        String replaced;
        String hexcode;
        String fixed;
        for(replaced = text; m.find(); replaced = replaced.replace(hexcode, "#" + fixed)) {
            hexcode = m.group();
            fixed = hexcode.substring(2, 8);
        }

        return replaced;
    }
}
