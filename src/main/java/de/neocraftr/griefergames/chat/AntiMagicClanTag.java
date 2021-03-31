package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.IChatComponent;

public class AntiMagicClanTag extends Chat {
	private static Pattern antiMagicClanTagRegex = Pattern.compile("^\\[[^\\]]+\\] ([A-Za-z\\-]+\\+?) \\u2503 (\\u007E?\\!?\\w{1,16})");

	@Override
	public String getName() {
		return "antiMagicClanTag";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if(getSettings().isAMPClanEnabled()) {
			String unformatted = msg.getUnformattedText();
			String formatted = msg.getFormattedText();

			if(unformatted.trim().length() > 0 && (formatted.contains("§k") || formatted.contains("§m"))) {
				Matcher antiMagicClanTag = antiMagicClanTagRegex.matcher(unformatted);
				return antiMagicClanTag.find();
			}
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		for (IChatComponent sibbling : msg.getSiblings().get(0).getSiblings()) {
			sibbling.getChatStyle().setObfuscated(false).setStrikethrough(false);
		}

		return msg;
	}
}