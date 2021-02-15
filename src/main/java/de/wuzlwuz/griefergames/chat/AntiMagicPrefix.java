package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class AntiMagicPrefix extends Chat {
	private static Pattern antiMagicPrefixRegex = Pattern.compile("(([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!??\\w{1,16}))");

	@Override
	public String getName() {
		return "antiMagicPrefix";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if(getSettings().isAMPEnabled()) {
			String unformatted = msg.getUnformattedText();
			String formatted = msg.getFormattedText();

			String oldMessage = getHelper().getProperTextFormat(formatted);

			if(unformatted.trim().length() > 0 && oldMessage.contains("§k")) {
				Matcher antiMagicPrefix = antiMagicPrefixRegex.matcher(unformatted);
				return antiMagicPrefix.find();
			}
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		boolean clan = msg.getSiblings().size() == 2;

		IChatComponent message = msg;
		if(clan) {
			message = msg.getSiblings().get(1);
		}

		IChatComponent newMsg = new ChatComponentText("");
		for(int i=0; i<message.getSiblings().size(); i++) {
			if(i+3 < message.getSiblings().size()) {
				String messageToCheck = message.getSiblings().get(i).getUnformattedText()+message.getSiblings().get(i+1).getUnformattedText()+message.getSiblings().get(i+2).getUnformattedText()+message.getSiblings().get(i+3).getUnformattedText();
				Matcher antiMagicPrefix = antiMagicPrefixRegex.matcher(messageToCheck);
				if (message.getSiblings().get(i).getChatStyle().getObfuscated() && antiMagicPrefix.find()) {
					ChatStyle msgStyling = message.getSiblings().get(i).getChatStyle().createDeepCopy().setObfuscated(false);

					String chatRepText = getSettings().getAMPChatReplacement();

					if (!chatRepText.contains("%CLEAN%")) {
						chatRepText = getSettings().getDefaultAMPChatReplacement();
					}

					chatRepText = chatRepText.replaceAll("%CLEAN%", message.getSiblings().get(i).getUnformattedText());
					chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

					// Rank
					newMsg.appendSibling(
							new ChatComponentText(chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
									.setChatStyle(msgStyling));
					// Space + Separator: ┃
					newMsg.appendSibling(message.getSiblings().get(i+1));
					newMsg.appendSibling(message.getSiblings().get(i+2));

					// Name
					ChatStyle msgStyling2 = message.getSiblings().get(i+3).getChatStyle().createDeepCopy().setObfuscated(false);
					newMsg.appendSibling(new ChatComponentText(message.getSiblings().get(i+3).getUnformattedText()).setChatStyle(msgStyling2));

					i += 3;
				} else {
					newMsg.appendSibling(message.getSiblings().get(i));
				}
			} else {
				newMsg.appendSibling(message.getSiblings().get(i));
			}
		}

		if(clan) newMsg.getSiblings().add(0, msg.getSiblings().get(0));
		return newMsg;
	}
}