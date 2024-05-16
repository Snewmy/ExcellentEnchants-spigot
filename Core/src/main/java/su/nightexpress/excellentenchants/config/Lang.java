package su.nightexpress.excellentenchants.config;

import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;

import static su.nightexpress.excellentenchants.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Lang extends CoreLang {

    public static final LangString COMMAND_LIST_DESC = LangString.of("Command.List.Desc",
        "List of all custom enchantments.");

    public static final LangString COMMAND_LIST_USAGE = LangString.of("Command.List.Usage",
        "[player]");

    public static final LangText COMMAND_LIST_DONE_OTHERS = LangText.of("Command.List.DoneOthers",
        LIGHT_GRAY.enclose("Opened enchantments GUI for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));


    public static final LangString COMMAND_GET_FUEL_DESC = LangString.of("Command.GetFuel.Desc",
        "Get enchantment fuel item.");

    public static final LangString COMMAND_GET_FUEL_USAGE = LangString.of("Command.GetFuel.Usage",
        "<enchant> [amount]");

    public static final LangText COMMAND_GET_FUEL_DONE = LangText.of("Command.GetFuel.Done",
        LIGHT_GRAY.enclose("You got " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT + " " + GENERIC_NAME) + "."));


    public static final LangString COMMAND_ENCHANT_USAGE = LangString.of("Command.Enchant.Usage",
        "<enchant> <level> [player] [slot]");

    public static final LangString COMMAND_ENCHANT_DESC = LangString.of("Command.Enchant.Desc",
        "Enchants the item in your hand.");

    public static final LangText COMMAND_ENCHANT_DONE_SELF = LangText.of("Command.Enchant.Done.Self",
        LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose(GENERIC_ITEM) + " enchanted with " + LIGHT_YELLOW.enclose(GENERIC_ENCHANT + " " + GENERIC_LEVEL) + "!"));

    public static final LangText COMMAND_ENCHANT_DONE_OTHERS = LangText.of("Command.Enchant.Done.Others",
        LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "'s " + LIGHT_YELLOW.enclose(GENERIC_ITEM) + " enchanted with " + LIGHT_YELLOW.enclose(GENERIC_ENCHANT + " " + GENERIC_LEVEL) + "!"));

    public static final LangText COMMAND_ENCHANT_ERROR_NO_ITEM = LangText.of("Command.Enchant.Error.NoItem",
        RED.enclose("There is no item to enchant!"));


    public static final LangString COMMAND_BOOK_USAGE = LangString.of("Command.Book.Usage",
        "<player> <enchant> <level>");

    public static final LangString COMMAND_BOOK_DESC = LangString.of("Command.Book.Desc",
        "Gives custom enchanted book.");

    public static final LangText COMMAND_BOOK_DONE = LangText.of("Command.Book.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose(GENERIC_ENCHANT) + " enchanted book to " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));


    public static final LangString COMMAND_RARITY_BOOK_USAGE = LangString.of("Command.RarityBook.Usage",
        "<player> <tier> <level>");

    public static final LangString COMMAND_RARITY_BOOK_DESC = LangString.of("Command.RarityBook.Desc",
        "Give an enchanted book with enchantment of specified rarity.");

    public static final LangText COMMAND_RARITY_BOOK_DONE = LangText.of("Command.RarityBook.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose(GENERIC_NAME) + " enchanted book to " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));


    public static final LangText ERROR_INVALID_ENCHANT = LangText.of("Error.InvalidEnchantment",
        RED.enclose("Invalid enchantment."));

    public static final LangText ERROR_INVALID_RARITY = LangText.of("Error.InvalidRarity",
        RED.enclose("Invalid rarity!"));

    public static final LangString COMMAND_ENCHANT_REMOVER_USAGE = LangString.of("Command.Enchant_Remover.Usage",
            "<player> <quantity>");

    public static final LangString COMMAND_ENCHANT_REMOVER_DESC = LangString.of("Command.Enchant_Remover.Desc",
            "Gives an enchantment remover (item).");

    public static final LangText COMMAND_ENCHANT_REMOVER_DONE = LangText.of("Command.Enchant_Remover.Done",
            LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose(GENERIC_AMOUNT) + "x enchantment remover to " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));

    public static final LangText ENCHANT_REMOVER_INITIATE = LangText.of("Enchant_Remover.Initiate",
            LIGHT_GRAY.enclose("Initiated enchantment remover. Valid enchantments to remove are: " + LIGHT_YELLOW.enclose(ENCHANTMENTS_ON_ITEM) + ". Type 'cancel' to cancel."));
    public static final LangText ENCHANT_REMOVER_CANCELLED = LangText.of("Enchant_Remover.Cancelled",
            LIGHT_GRAY.enclose("Cancelled enchantment removal. Returning enchantment remover item to you."));
    public static final LangText ENCHANT_REMOVER_NOT_ENCHANT = LangText.of("Enchant_Remover.Not_Enchantment",
            LIGHT_GRAY.enclose("That is not a valid enchantment on this item!"));
    public static final LangText ENCHANT_REMOVER_SUCCESS = LangText.of("Enchant_Remover.Success",
            LIGHT_GRAY.enclose("Successfully removed enchantment, and returned it to you in book form!"));
}
