package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.neocraftr.griefergames.settings.ModSettings;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class AntiMagicPrefix extends Chat {
	private static Pattern antiMagicPrefixRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\u007E?\\!?\\w{1,16})");

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
		boolean clan = msg.getSiblings().size() == 2;

		IChatComponent message = msg.getSiblings().get(0);
		if(clan) {
			message = msg.getSiblings().get(1);
		}

		String ampPrefix = getGG().getSettings().getAMPChatReplacement();
		if(ampPrefix.trim().isEmpty()) ampPrefix = ModSettings.DEFAULT_AMP_REPLACEMENT_CHAT;

		Matcher matcher = antiMagicPrefixRegex.matcher(message.getUnformattedText());
		if(!matcher.find()) return message;

		IChatComponent newMsg = new ChatComponentText("");
		for(IChatComponent sibbling : message.getSiblings()) {
			if(sibbling.getUnformattedText().equalsIgnoreCase(matcher.group(1))) {
				sibbling.getChatStyle().setObfuscated(false);
				IChatComponent newSibbling = new ChatComponentText(ampPrefix+" "+sibbling.getUnformattedText());
				newSibbling.setChatStyle(sibbling.getChatStyle());
				newMsg.appendSibling(newSibbling);
			} else if(sibbling.getUnformattedText().equalsIgnoreCase(matcher.group(2))) {
				sibbling.getChatStyle().setObfuscated(false);
				newMsg.appendSibling(sibbling);
			} else {
				newMsg.appendSibling(sibbling);
			}
		}

		if(clan) newMsg.getSiblings().add(0, msg.getSiblings().get(0));
		return newMsg;
	}
}