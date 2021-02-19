package de.wuzlwuz.griefergames.listener;

import java.util.Collections;
import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.settings.ModSettings;
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
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (LabyModCore.getMinecraft().getWorld() != null && event.phase == TickEvent.Phase.END) {
			final long now = System.currentTimeMillis();
			if (now > getGG().getGGServer().getNextLastMessageRequest()) {
				getGG().getGGServer().setNextLastMessageRequest(now
						+ GrieferGames.getSettings().getFilterDuplicateMessagesTime() * 1000L);
				getGG().getGGServer().setLastMessage("");
			}

			if (now > getGG().getGGServer().getNextScoreboardRequest()) {
				getGG().getGGServer().setNextScoreboardRequest(now + 500L);
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

							if (!getGG().getGGServer().getSubServer().matches(scoreName)) {
								for (SubServerListener ssl : getGG().getGGServer().getSubServerListener()) {
									ssl.onSubServerChanged(getGG().getGGServer().getSubServer(),
											scoreName);
									getGG().getGGServer().setSubServer(scoreName);
								}
							}
							i = scoreList.size();
						}
					}
				}
			}

			if (now > getGG().getGGServer().getNextCheckFly()) {
				getGG().getGGServer().setNextCheckFly(now + 500L);
				getGG().setFlyActive(LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying);
			}

			if (now > getGG().getGGServer().getNextUpdateTimeToWait() && getGG().getTimeToWait() > 0) {
				getGG().getGGServer().setNextUpdateTimeToWait(now + 1000L);
				getGG().setTimeToWait(getGG().getTimeToWait() - 1);
			}

			if(now > getGG().getGGServer().getNextCheckAFKTime()) {
				getGG().getGGServer().setNextCheckAFKTime(now + 2000L);
				BlockPos currentPos = Minecraft.getMinecraft().thePlayer.getPosition();
				if(currentPos.compareTo(getGG().getLastPlayerPosition()) != 0) {
					getGG().getGGServer().setLastActiveTime(now);
				}
				getGG().setLastPlayerPosition(currentPos);
			}

			if(getGG().isAfk()) {
				if(now < getGG().getGGServer().getLastActiveTime() + GrieferGames.getSettings().getAfkTime()*60000L) {
					getGG().setAfk(false);
					getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+"§7"+ LanguageManager.translateOrReturnKey("message_gg_afkBackMessage"));
					if(GrieferGames.getSettings().isAfkNick()) {
						Minecraft.getMinecraft().thePlayer.sendChatMessage("/unnick");
					}
				}
			} else {
				if(now > getGG().getGGServer().getLastActiveTime() + GrieferGames.getSettings().getAfkTime()*60000L) {
					getGG().setAfk(true);
					getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+"§7"+ LanguageManager.translateOrReturnKey("message_gg_afkMessage"));
					if(GrieferGames.getSettings().isAfkNick()) {
						String nickname = GrieferGames.getSettings().getAfkNickname();
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

			if(GrieferGames.getSettings().isHideBoosterMenu()) {
				Container cont = Minecraft.getMinecraft().thePlayer.openContainer;
				if(cont instanceof ContainerChest) {
					ContainerChest chest = (ContainerChest) cont;
					IInventory inv = chest.getLowerChestInventory();
					if(inv.getName().equals("§6Booster - Übersicht")) {
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