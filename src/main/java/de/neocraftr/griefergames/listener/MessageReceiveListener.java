package de.neocraftr.griefergames.listener;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.chat.Chat;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MessageReceiveListener implements MessageReceiveEvent {
    @Override
    public boolean onReceive(String formatted, String unformatted) {
        if (!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return false;

        if (unformatted.startsWith("[SCAMMER] ")) {
            formatted = formatted.replaceFirst("((§r)?§6\\[§r§([0-9]|[a-f])§lSCAMMER§r§6\\]§r§r)", "").trim();

            unformatted = unformatted.replaceFirst("\\[SCAMMER\\]", "").trim();
        }

        List<Chat> chatModules = getGG().getChatModules();
        for (Chat chatModule : chatModules) {
            if (chatModule.doActionReceiveMessage(formatted, unformatted)) {
                return chatModule.receiveMessage(formatted, unformatted);
            }
        }

        getGG().getHelper().handleBoosterMessage(unformatted);
        getGG().getHelper().handleCityBuildDelay(unformatted);

        if (getGG().getHelper().isSwitcherDoneMsg(unformatted)) {
            if(getGG().getSettings().isUpdateBoosterState()) {
                getGG().setHideBoosterMenu(true);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/booster");
            }

            if(getGG().getSettings().isVanishOnJoin() && getGG().getHelper().showVanishModule(getGG().getPlayerRank()) && !getGG().isVanishActive()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/vanish");
            }

            if(getGG().getSettings().isFlyOnJoin() && getGG().getHelper().hasFlyPermission(getGG().getPlayerRank())) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/fly");
                        }
                    }
                }, 5000);
            }
        }

        if(!getGG().isCityBuildDelay() && getGG().getHelper().isResetWaitTimeMessage(unformatted)) {
            getGG().setWaitTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(12));
        }

        return false;
    }

    public GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }
}
