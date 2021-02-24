package de.neocraftr.griefergames.listener;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.neocraftr.griefergames.GrieferGames;
import de.neocraftr.griefergames.settings.ModSettings;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PreRenderListener {
	private static Pattern antiMagicPrefixRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})");

	@SubscribeEvent
	public void onPreRender(RenderGameOverlayEvent event) {
		if (!getGG().getSettings().isModEnabled() || !getGG().isOnGrieferGames()) return;
		if (Minecraft.getMinecraft().gameSettings.keyBindPlayerList.isKeyDown()
				&& !Minecraft.getMinecraft().isIntegratedServerRunning()
				&& getGG().getSettings().isAMPEnabled()) {

			ScoreObjective scoreobjective = LabyModCore.getMinecraft().getWorld().getScoreboard()
					.getObjectiveInDisplaySlot(0);
			NetHandlerPlayClient handler = LabyModCore.getMinecraft().getPlayer().sendQueue;
			if (handler.getPlayerInfoMap().size() > 1 || scoreobjective != null) {
				Collection<NetworkPlayerInfo> players = handler.getPlayerInfoMap();
				for (NetworkPlayerInfo player : players) {
					if (player.getDisplayName() != null) {
						IChatComponent playerDisplayName = player.getDisplayName();

						if (playerDisplayName.getUnformattedText().length() > 0
								&& getGG().getSettings().isAMPEnabled()) {
							String oldPlayerDisplayName = GrieferGames.getGriefergames().getHelper()
									.getProperTextFormat(playerDisplayName.getFormattedText());
							if (oldPlayerDisplayName.contains("§k")) {
								Matcher antiMagicPrefix = antiMagicPrefixRegex
										.matcher(playerDisplayName.getUnformattedText());
								IChatComponent newPlayerDisplayName = new ChatComponentText("");
								if (antiMagicPrefix.find()) {
									for (IChatComponent displayName : playerDisplayName.getSiblings()) {
										// if (displayName.getChatStyle().getObfuscated() &&
										// displayName.getUnformattedText().matches("(([A-Za-z\\-]+\\+?) \\u2503
										// ((\\u007E)?\\w{1,16}))")) {
										if (displayName.getChatStyle().getObfuscated() && displayName
												.getUnformattedText().equalsIgnoreCase(antiMagicPrefix.group(1))) {

											ChatStyle playerDisplayNameStyling = displayName.getChatStyle()
													.createDeepCopy().setObfuscated(false);
											String chatRepText = getGG().getSettings().getAMPTablistReplacement();

											if (!chatRepText.contains("%CLEAN%") && !chatRepText.contains("%clean%")) {
												chatRepText = ModSettings.DEFAULT_AMP_REPLACEMENT_TABLIST;
											}

											chatRepText = chatRepText.replaceAll("%CLEAN%", displayName.getUnformattedText()).replaceAll("%clean%", displayName.getUnformattedText());
											chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

											newPlayerDisplayName.appendSibling(new ChatComponentText(
													chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
													.setChatStyle(playerDisplayNameStyling));
										} else if (displayName.getChatStyle().getObfuscated() && displayName
												.getUnformattedText().equalsIgnoreCase(antiMagicPrefix.group(2))) {
											ChatStyle playerDisplayNameStyling = displayName.getChatStyle()
													.createDeepCopy().setObfuscated(false);
											newPlayerDisplayName
													.appendSibling(displayName.setChatStyle(playerDisplayNameStyling));
										} else {
											newPlayerDisplayName.appendSibling(displayName);
										}
									}

									player.setDisplayName(newPlayerDisplayName);
								}
							}
						}
					}
				}
			}
		}
	}

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}
}