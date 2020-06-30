package de.wuzlwuz.griefergames.listener;

import java.util.Collections;
import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.core.LabyModCore;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OnTickListener {
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (LabyModCore.getMinecraft().getWorld() != null && event.phase == TickEvent.Phase.END) {
			if (System.currentTimeMillis() > GrieferGames.getGriefergames().getGGServer().getNextLastMessageRequest()) {
				GrieferGames.getGriefergames().getGGServer().setNextLastMessageRequest(System.currentTimeMillis()
						+ GrieferGames.getSettings().getFilterDuplicateMessagesTime() * 1000L);
				GrieferGames.getGriefergames().getGGServer().setLastMessage("");
			}
			if (System.currentTimeMillis() > GrieferGames.getGriefergames().getGGServer().getNextScoreboardRequest()) {
				GrieferGames.getGriefergames().getGGServer()
						.setNextScoreboardRequest(System.currentTimeMillis() + 500L);
				Scoreboard scoreboard = LabyModCore.getMinecraft().getWorld().getScoreboard();
				ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);
				if (scoreobjective != null) {
					List<Score> scoreList = (List<Score>) scoreboard.getSortedScores(scoreobjective);
					Collections.reverse(scoreList);
					for (int i = 0; i < scoreList.size(); i++) {
						ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i).getPlayerName());
						String scoreName = ScorePlayerTeam.formatPlayerName(scorePlayerTeam,
								scoreList.get(i).getPlayerName());

						if (GrieferGames.getGriefergames().getHelper().isScoreBoardSubServer(scoreName)) {
							scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i + 1).getPlayerName());
							scoreName = ScorePlayerTeam
									.formatPlayerName(scorePlayerTeam, scoreList.get(i + 1).getPlayerName())
									.replaceAll("\u00A7[0-9a-f]", "").trim();

							if (!GrieferGames.getGriefergames().getGGServer().getSubServer().matches(scoreName)) {
								for (SubServerListener ssl : GrieferGames.getGriefergames().getGGServer()
										.getSubServerListener()) {
									ssl.onSubServerChanged(GrieferGames.getGriefergames().getGGServer().getSubServer(),
											scoreName);
									GrieferGames.getGriefergames().getGGServer().setSubServer(scoreName);
								}
							}
							i = scoreList.size();
						}
					}
				}
			}

			if (System.currentTimeMillis() > GrieferGames.getGriefergames().getGGServer().getNextCheckFly()) {
				GrieferGames.getGriefergames().getGGServer().setNextCheckFly(System.currentTimeMillis() + 500L);
				GrieferGames.getGriefergames()
						.setFlyActive(LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying);
			}

			if (System.currentTimeMillis() > GrieferGames.getGriefergames().getGGServer().getNextUpdateTimeToWait()
					&& GrieferGames.getGriefergames().getTimeToWait() > 0) {
				GrieferGames.getGriefergames().getGGServer().setNextUpdateTimeToWait(System.currentTimeMillis() + 1000L);
				GrieferGames.getGriefergames().setTimeToWait(GrieferGames.getGriefergames().getTimeToWait() - 1);
			}
		}
	}
}