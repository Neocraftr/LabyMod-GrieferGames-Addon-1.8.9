package de.neocraftr.griefergames.utils;

import de.neocraftr.griefergames.GrieferGames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

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

    private static GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
