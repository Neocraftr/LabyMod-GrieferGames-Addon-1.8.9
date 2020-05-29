package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class AntiMagicPrefix extends Chat {
	private static Pattern antiMagicPrefixRegex = Pattern.compile("(([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16}))");

	@Override
	public String getName() {
		return "antiMagicPrefix";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		String oldMessage = getHelper().getProperTextFormat(formatted);

		Matcher antiMagicPrefix = antiMagicPrefixRegex.matcher(unformatted);

		return getSettings().isAMPEnabled() && unformatted.trim().length() > 0 && oldMessage.contains("§k")
				&& antiMagicPrefix.find();
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return doAction(unformatted, formatted);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		if (doActionModifyChatMessage(msg)) {
			IChatComponent newMsg = new ChatComponentText("");
			for(int i=0; i<msg.getSiblings().size(); i++) {
				if(i+2 < msg.getSiblings().size()) {
					String messageToCheck = msg.getSiblings().get(i).getUnformattedText()+msg.getSiblings().get(i+1).getUnformattedText()+msg.getSiblings().get(i+2).getUnformattedText();
					Matcher antiMagicPrefix = antiMagicPrefixRegex.matcher(messageToCheck);
					if (msg.getSiblings().get(i).getChatStyle().getObfuscated() && antiMagicPrefix.find()) {
						ChatStyle msgStyling = msg.getSiblings().get(i).getChatStyle().createDeepCopy().setObfuscated(false);

						String chatRepText = getSettings().getAMPChatReplacement();

						if (!chatRepText.contains("%CLEAN%")) {
							chatRepText = getSettings().getDefaultAMPChatReplacement();
						}

						chatRepText = chatRepText.replaceAll("%CLEAN%", msg.getSiblings().get(i).getUnformattedText());
						chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

						// Rank
						newMsg.appendSibling(
								new ChatComponentText(chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
										.setChatStyle(msgStyling));
						// Separator: ┃
						newMsg.appendSibling(msg.getSiblings().get(i+1));

						// Name
						ChatStyle msgStyling2 = msg.getSiblings().get(i+2).getChatStyle().createDeepCopy().setObfuscated(false);
						newMsg.appendSibling(new ChatComponentText(msg.getSiblings().get(i+2).getUnformattedText()).setChatStyle(msgStyling2));

						i += 2;
					} else {
						newMsg.appendSibling(msg.getSiblings().get(i));
					}
				} else {
					newMsg.appendSibling(msg.getSiblings().get(i));
				}
			}
			return newMsg;
		}

		return msg;
	}
}