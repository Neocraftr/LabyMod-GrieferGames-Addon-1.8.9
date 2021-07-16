package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.neocraftr.griefergames.settings.ModSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class AntiMagicPrefix extends Chat {
	private static Pattern antiMagicPrefixRegex = Pattern.compile("([A-Za-z\\-\\+]+) \\u2503 (~?!?\\w{1,16})");
	private static Pattern globalChatRegex = Pattern.compile("[A-Za-z\\-]+\\+? \\u2503 (\\!?\\w{1,16}) \\u00BB");

	@Override
	public String getName() {
		return "antiMagicPrefix";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if(getSettings().isAMPEnabled()) {
			String unformatted = msg.getUnformattedText();
			String formatted = msg.getFormattedText();

			return unformatted.trim().length() > 0 && formatted.contains("§k");
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		// Check if message contains playername
		Matcher matcher = antiMagicPrefixRegex.matcher(msg.getUnformattedText());
		if(!matcher.find()) return msg;

		// Find message component in global chat
		IChatComponent message = msg;
		for(IChatComponent sibbling : msg.getSiblings()) {
			Matcher m = globalChatRegex.matcher(sibbling.getUnformattedText());
			if(m.find()) {
				message = sibbling;
				break;
			}
		}

		// Prepare prefix
		String ampPrefix = getGG().getSettings().getAMPChatReplacement();
		if(ampPrefix.trim().isEmpty()) ampPrefix = ModSettings.DEFAULT_AMP_REPLACEMENT_CHAT;

		// Replace magic prefix
		for(int i=0; i<message.getSiblings().size(); i++) {
			IChatComponent currentSibbling = message.getSiblings().get(i);
			if(currentSibbling.getUnformattedText().equalsIgnoreCase(matcher.group(1))) {
				currentSibbling.getChatStyle().setObfuscated(false);
				IChatComponent newRankSibbling = new ChatComponentText(ampPrefix+" "+currentSibbling.getUnformattedText());
				newRankSibbling.setChatStyle(currentSibbling.getChatStyle());
				message.getSiblings().set(i, newRankSibbling);
			} else if(currentSibbling.getUnformattedText().equalsIgnoreCase(matcher.group(2))) {
				currentSibbling.getChatStyle().setObfuscated(false);
				break;
			}
		}

		return msg;
	}
}