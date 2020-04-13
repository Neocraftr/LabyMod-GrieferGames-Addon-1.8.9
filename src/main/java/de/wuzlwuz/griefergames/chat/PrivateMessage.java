package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.core.LabyModCore;
import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public class PrivateMessage extends Chat {
	private static Pattern privateMessageRegex = Pattern
			.compile("^\\[([A-Za-z\\-]+\\+?) \\| ((\\u007E)?\\w{1,16}) -> mir\\](.*)$");
	private static Pattern privateMessageSentRegex = Pattern
			.compile("^\\[mir -> ([A-Za-z\\-]+\\+?) \\| ((\\u007E)?\\w{1,16})\\](.*)$");

	@Override
	public String getName() {
		return "privateMessage";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher privateMessage = privateMessageRegex.matcher(unformatted);
		Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);

		if (getSettings().isPrivateChatRight() && unformatted.trim().length() > 0
				&& (privateMessage.find() || privateMessageSent.find()))
			return true;

		return false;
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return (doAction(unformatted, formatted));
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return (doAction(unformatted, formatted));
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (doAction(unformatted, formatted)) {
			return ChatDisplayAction.SWAP;
		}
		return super.handleChatMessage(unformatted, formatted);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		Matcher privateMessage = privateMessageRegex.matcher(unformatted);
		Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);

		if (getSettings().isMsgDisplayNameClick() && doAction(unformatted, formatted)) {
			if (privateMessage.find()) {
				if (getSettings().isPrivateChatSound()) {
					LabyModCore.getMinecraft().playSound(new ResourceLocation(getSettings().getPrivateChatSoundPath()),
							1.0F);
				}

				if (msg.getSiblings().size() > 5) {
					String username = "/msg " + getPrivateMessageName(unformatted) + " ";
					msg.getSiblings().get(1).getChatStyle()
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
					if (getHelper().getProperTextFormat(msg.getSiblings().get(5).getFormattedText()).equals("§6] §r")) {
						msg.getSiblings().get(2).getChatStyle()
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
					}

					return msg;
				}
			}

			if (privateMessageSent.find()) {
				if (msg.getSiblings().size() > 5) {
					String username = "/msg " + getSentPrivateMessageName(unformatted) + " ";
					msg.getSiblings().get(3).getChatStyle()
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
					if (getHelper().getProperTextFormat(msg.getSiblings().get(5).getFormattedText()).equals("§6] §r")) {
						msg.getSiblings().get(4).getChatStyle()
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
					}

					return msg;
				}
			}
		}

		return super.modifyChatMessage(msg);
	}

	private String getSentPrivateMessageName(String unformatted) {
		String displayName = "";

		Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);
		if (privateMessageSent.find()) {
			displayName = privateMessageSent.group(2);
		}
		return displayName;
	}

	private String getPrivateMessageName(String unformatted) {
		String displayName = "";

		Matcher privateMessage = privateMessageRegex.matcher(unformatted);
		if (privateMessage.find()) {
			displayName = privateMessage.group(2);
		}
		return displayName;
	}
}
