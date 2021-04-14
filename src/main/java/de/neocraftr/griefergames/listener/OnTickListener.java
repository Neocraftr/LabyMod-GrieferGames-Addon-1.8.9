package de.neocraftr.griefergames.listener;

import java.util.Collections;
import java.util.List;

import de.neocraftr.griefergames.settings.ModSettings;
import de.neocraftr.griefergames.GrieferGames;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OnTickListener {

	private long nextLastMessageRequest = System.currentTimeMillis() + 1000L;
	private long nextScoreboardRequest = System.currentTimeMillis() + (-1 * 1000L);
	private long nextCheckFly = System.currentTimeMillis() + 1000L;
	private long nextUpdateTimeToWait = System.currentTimeMillis() + 1000L;
	private long nextCheckAFKTime = System.currentTimeMillis() + 2000L;
	private long nextColorizePlayerNames = System.currentTimeMillis() + 20000L;

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

			if (now > nextScoreboardRequest) {
				nextScoreboardRequest = now + 500L;
				Scoreboard scoreboard = LabyModCore.getMinecraft().getWorld().getScoreboard();
				ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);
				if (scoreobjective != null) {
					List<Score> scoreList = (List<Score>) scoreboard.getSortedScores(scoreobjective);
					Collections.reverse(scoreList);
					for (int i = 0; i < scoreList.size(); i++) {
						ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i).getPlayerName());
						String scoreName = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, scoreList.get(i).getPlayerName());

						if (getGG().getHelper().isScoreBoardSubServer(scoreName)) {
							scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i + 1).getPlayerName());
							scoreName = ScorePlayerTeam
									.formatPlayerName(scorePlayerTeam, scoreList.get(i + 1).getPlayerName())
									.replaceAll("\u00A7[0-9a-f]", "").trim();

							if (!getGG().getSubServer().matches(scoreName)) {
								getGG().callSubServerEvent(getGG().getSubServer(), scoreName);
									getGG().setSubServer(scoreName);
							}
							i = scoreList.size();
						}
					}
				}
			}

			if(getGG().getSettings().isShowPrefixInDisplayName() && now > nextColorizePlayerNames) {
				nextColorizePlayerNames = now + 20000L;
				getGG().getHelper().colorizePlayerNames();
			}

			if (now > nextCheckFly) {
				nextCheckFly = now + 500L;
				getGG().setFlyActive(LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying);
			}

			if (now > nextUpdateTimeToWait && getGG().getTimeToWait() > 0) {
				nextUpdateTimeToWait = now + 1000L;
				getGG().setTimeToWait(getGG().getTimeToWait() - 1);
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
		}
	}
	
	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}
}