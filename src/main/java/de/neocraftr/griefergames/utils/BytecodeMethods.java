package de.neocraftr.griefergames.utils;

import de.neocraftr.griefergames.GrieferGames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BytecodeMethods {

    public static boolean handleRightClick() {
        if(getGG() != null) {
            if(!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return false;
            if(!getGG().getSettings().isOpenBookClientSide()) return false;

            ItemStack currentItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if(currentItem != null && currentItem.getItem() == Items.written_book) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(null, currentItem, false));
                return true;
            }
        }
        return false;
    }

    public static void onItemToolTip(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean showAdvancedItemTooltips) {
        if(getGG() != null && getGG().getGrieferWertManager() != null) {
            if(!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;
            // TODO: setting

            getGG().getGrieferWertManager().onItemTooltip(itemStack, tooltip);
        }
    }

    private static GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
