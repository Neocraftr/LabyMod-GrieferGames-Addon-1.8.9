package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.IChatComponent;

public class AntiMagicClanTag extends Chat {
	private static Pattern antiMagicClanTagRegex = Pattern.compile("\\[[^\\]]+\\] ([A-Za-z\\-\\+]+) \\u2503 (~?!?\\w{1,16})");

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
				Matcher chatMatcher = antiMagicClanTagRegex.matcher(unformatted);
				return chatMatcher.find();
			}
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		// Find clan tag
		for(IChatComponent clanTag : msg.getSiblings()) {
			if (clanTag.getFormattedText().contains("§r§6[") && clanTag.getFormattedText().contains("§r§6]")) {
				// remove magic effect
				for (IChatComponent component : msg.getSiblings().get(0).getSiblings()) {
					component.getChatStyle().setObfuscated(false).setStrikethrough(false);
				}
				break;
			}
		}

		return msg;
	}
}