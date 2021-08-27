package de.neocraftr.griefergames.listener;

import java.util.concurrent.TimeUnit;

import de.neocraftr.griefergames.settings.ModSettings;
import de.neocraftr.griefergames.GrieferGames;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickListener {

	private long nextLastMessageRequest = System.currentTimeMillis() + 1000L;
	private long nextCheckAFKTime = System.currentTimeMillis() + 2000L;
	private long nextColorizePlayerNames = System.currentTimeMillis() + 20000L;
	private boolean itemRemoverNotificationShown = false;

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;

		if (LabyModCore.getMinecraft().getWorld() != null && event.phase == TickEvent.Phase.END) {
			getGG().getPlotSwitchGui().tick();

			final long now = System.currentTimeMillis();
			if (now > nextLastMessageRequest) {
				nextLastMessageRequest = now + getGG().getSettings().getFilterDuplicateMessagesTime() * 1000L;
				getGG().setFilterDuplicateLastMessage("");
			}

			if(getGG().getSettings().isShowPrefixInDisplayName() && now > nextColorizePlayerNames) {
				nextColorizePlayerNames = now + 20000L;
				getGG().getHelper().colorizePlayerNames();
			}

			if(now > nextCheckAFKTime) {
				nextCheckAFKTime = now + 2000L;
				BlockPos currentPos = Minecraft.getMinecraft().thePlayer.getPosition();
				if(currentPos.compareTo(getGG().getLastPlayerPosition()) != 0) {
					getGG().setLastActiveTime(now);
				}
				getGG().setLastPlayerPosition(currentPos);
			}

			if(getGG().isAfk()) {
				if(now < getGG().getLastActiveTime() + getGG().getSettings().getAfkTime()*60000L) {
					getGG().setAfk(false);
					getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+"§7"+ LanguageManager.translateOrReturnKey("message_gg_afkBackMessage"));
					if(getGG().getSettings().isAfkNick()) {
						Minecraft.getMinecraft().thePlayer.sendChatMessage("/unnick");
					}
				}
			} else {
				if(now > getGG().getLastActiveTime() + getGG().getSettings().getAfkTime()*60000L) {
					getGG().setAfk(true);
					getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+"§7"+ LanguageManager.translateOrReturnKey("message_gg_afkMessage"));
					if(getGG().getSettings().isAfkNick()) {
						String nickname = getGG().getSettings().getAfkNickname();
						if(nickname.length() < 4 || nickname.length() > 16) {
							nickname = ModSettings.DEFAULT_AFK_NICKNAME;
						}

						nickname = nickname.replace("%NAME%", LabyMod.getInstance().getPlayerName()).replace("%name%", LabyMod.getInstance().getPlayerName());
						if(nickname.length() > 16) {
							nickname = nickname.substring(0, 16);
						}

						Minecraft.getMinecraft().thePlayer.sendChatMessage("/nick "+nickname);
					}
				}
			}

			if(getGG().getSettings().isHideBoosterMenu() || getGG().isHideBoosterMenu()) {
				Container cont = Minecraft.getMinecraft().thePlayer.openContainer;
				if(cont instanceof ContainerChest) {
					ContainerChest chest = (ContainerChest) cont;
					IInventory inv = chest.getLowerChestInventory();
					if(inv.getName().equals("§6Booster - Übersicht")) {
						getGG().setHideBoosterMenu(false);
						Minecraft.getMinecraft().thePlayer.closeScreen();
					}
				}
			}

			if(getGG().getSettings().getItemRemoverNotification() > 0) {
				int remainingTime = (int) TimeUnit.MILLISECONDS.toSeconds(getGG().getClearLagTime() - now);
				if(remainingTime > 0 && remainingTime <= getGG().getSettings().getItemRemoverNotification()) {
					if(!itemRemoverNotificationShown) {
						itemRemoverNotificationShown = true;
						String subtitle = "§c"+LanguageManager.translateOrReturnKey("message_gg_itemRemover");
						subtitle = subtitle.replace("${time}", String.valueOf(remainingTime));
						Minecraft.getMinecraft().ingameGUI.displayTitle("§4"+LanguageManager.translateOrReturnKey("gg_attention"), null, 10, 10, 10);
						Minecraft.getMinecraft().ingameGUI.displayTitle(null, subtitle, 10, 10, 10);
					}
				} else {
					itemRemoverNotificationShown = false;
				}
			}

			if(getGG().getWaitTime() < System.currentTimeMillis()) {
				getGG().setCityBuildDelay(false);
			}
		}
	}
	
	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}
}