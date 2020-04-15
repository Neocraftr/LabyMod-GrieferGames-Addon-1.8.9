package de.wuzlwuz.griefergames.listener;

import java.util.Collection;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.core.LabyModCore;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PreRenderListener {
	@SubscribeEvent
	public void onPreRender(RenderGameOverlayEvent event) {
		if (GrieferGames.getGriefergames().getGGServer().getMc().gameSettings.keyBindPlayerList.isKeyDown()
				&& !GrieferGames.getGriefergames().getGGServer().getMc().isIntegratedServerRunning()
				&& GrieferGames.getSettings().isAMPEnabled()) {

			ScoreObjective scoreobjective = LabyModCore.getMinecraft().getWorld().getScoreboard()
					.getObjectiveInDisplaySlot(0);
			NetHandlerPlayClient handler = LabyModCore.getMinecraft().getPlayer().sendQueue;
			if (handler.getPlayerInfoMap().size() > 1 || scoreobjective != null) {
				Collection<NetworkPlayerInfo> players = handler.getPlayerInfoMap();
				for (NetworkPlayerInfo player : players) {
					if (player.getDisplayName() != null) {
						IChatComponent playerDisplayName = (IChatComponent) player.getDisplayName();

						if (playerDisplayName.getUnformattedText().length() > 0
								&& GrieferGames.getSettings().isAMPEnabled()) {
							String oldPlayerDisplayName = GrieferGames.getGriefergames().getHelper()
									.getProperTextFormat(playerDisplayName.getFormattedText());
							if (oldPlayerDisplayName.indexOf("§k") != -1) {
								IChatComponent newPlayerDisplayName = new ChatComponentText("");
								for (IChatComponent displayName : playerDisplayName.getSiblings()) {
									if (displayName.getChatStyle().getObfuscated() && displayName.getUnformattedText()
											.matches("(([A-Za-z\\-]+\\+?) \\| (\\w{1,16}))")) {
										ChatStyle playerDisplayNameStyling = displayName.getChatStyle().createDeepCopy()
												.setObfuscated(false);
										String chatRepText = GrieferGames.getSettings().getAMPTablistReplacement();

										if (chatRepText.indexOf("%CLEAN%") == -1) {
											chatRepText = GrieferGames.getSettings().getDefaultAMPTablistReplacement();
										}

										chatRepText = chatRepText.replaceAll("%CLEAN%",
												displayName.getUnformattedText());
										chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

										newPlayerDisplayName.appendSibling(new ChatComponentText(
												chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
														.setChatStyle(playerDisplayNameStyling));
										player.setDisplayName(newPlayerDisplayName);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}