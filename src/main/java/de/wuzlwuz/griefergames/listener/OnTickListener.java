package de.wuzlwuz.griefergames.listener;

import java.util.Collections;
import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.server.GrieferGamesServer;
import net.labymod.core.LabyModCore;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OnTickListener {
	private GrieferGamesServer server;

	public GrieferGamesServer getServer() {
		return server;
	}

	public void setServer(GrieferGamesServer server) {
		this.server = server;
	}

	public OnTickListener(GrieferGamesServer server) {
		this.setServer(server);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (LabyModCore.getMinecraft().getWorld() != null && event.phase == TickEvent.Phase.START) {
			if (System.currentTimeMillis() > getServer().getNextLastMessageRequest()) {
				getServer().setNextLastMessageRequest(System.currentTimeMillis()
						+ GrieferGames.getSettings().getFilterDuplicateMessagesTime() * 1000L);
				getServer().setLastMessage("");
			}
			if (System.currentTimeMillis() > getServer().getNextScoreboardRequest()) {
				getServer().setNextScoreboardRequest(System.currentTimeMillis() + 500L);
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

							if (!getServer().getSubServer().matches(scoreName)) {
								for (SubServerListener ssl : getServer().getSubServerListener()) {
									ssl.onSubServerChanged(getServer().getSubServer(), scoreName);
									getServer().setSubServer(scoreName);
								}
							}
							i = scoreList.size();
						}
					}
				}
			}

			if (System.currentTimeMillis() > getServer().getNextCheckFly()) {
				getServer().setNextCheckFly(System.currentTimeMillis() + 500L);
				GrieferGames.getGriefergames()
						.setFlyActive(LabyModCore.getMinecraft().getPlayer().capabilities.allowFlying);
			}
		}
	}
}