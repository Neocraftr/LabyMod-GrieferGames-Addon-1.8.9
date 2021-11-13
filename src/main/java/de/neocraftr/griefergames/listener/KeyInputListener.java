package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.enums.CityBuild;
import de.neocraftr.griefergames.grieferwert.GrieferWertGui;
import de.neocraftr.griefergames.plots.gui.PlotsGuiAdd;
import de.neocraftr.griefergames.settings.ModSettings;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KeyInputListener {
    private static Pattern antiMagicPrefixRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\u007E?\\!?\\w{1,16})");

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;

        if(Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown() && getGG().getSettings().isAMPEnabled()) {
            NetHandlerPlayClient handler = LabyModCore.getMinecraft().getPlayer().sendQueue;

            String ampPrefix = getGG().getSettings().getAMPTablistReplacement();
            if(ampPrefix.trim().isEmpty()) ampPrefix = ModSettings.DEFAULT_AMP_REPLACEMENT_TABLIST;

            for (NetworkPlayerInfo player : handler.getPlayerInfoMap()) {
                if(player.getDisplayName() == null) continue;

                IChatComponent displayName = player.getDisplayName();
                if(!displayName.getFormattedText().contains("§k")) continue;

                Matcher matcher = antiMagicPrefixRegex.matcher(displayName.getUnformattedText());
                if(!matcher.find()) continue;

                ChatComponentText newDisplayName = new ChatComponentText("");

                for(IChatComponent sibbling : displayName.getSiblings()) {
                    if(sibbling.getUnformattedText().equalsIgnoreCase(matcher.group(1))) {
                        sibbling.getChatStyle().setObfuscated(false);
                        IChatComponent newSibbling = new ChatComponentText(ampPrefix+" "+sibbling.getUnformattedText());
                        newSibbling.setChatStyle(sibbling.getChatStyle());
                        newDisplayName.appendSibling(newSibbling);
                    } else if(sibbling.getUnformattedText().equalsIgnoreCase(matcher.group(2))) {
                        sibbling.getChatStyle().setObfuscated(false);
                        newDisplayName.appendSibling(sibbling);
                    } else {
                        newDisplayName.appendSibling(sibbling);
                    }
                }

                player.setDisplayName(newDisplayName);
            }
        }

        if(getGG().getHelper().isKeyDown(getGG().getSettings().getAddPlotKey())) {
            CityBuild currentCityBuild = getGG().getHelper().cityBuildFromServerName(getGG().getSubServer(), CityBuild.ALL);
            Minecraft.getMinecraft().displayGuiScreen(new PlotsGuiAdd(null, currentCityBuild, -1));
        }

        if(getGG().getHelper().isKeyDown(getGG().getSettings().getGrieferWertKey())) {
            Minecraft.getMinecraft().displayGuiScreen(new GrieferWertGui(null));
        }
    }

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
