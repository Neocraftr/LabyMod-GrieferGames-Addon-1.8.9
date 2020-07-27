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
			.compile("^\\[([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) -> mir\\](.*)$");
	private static Pattern privateMessageSentRegex = Pattern
			.compile("^\\[mir -> ([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16})\\](.*)$");

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

		if (doAction(unformatted, formatted)) {
			if (privateMessage.find()) {
				// String suggestMsgHoverTxt =
				// LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg",new
				// Object[0]);
				// IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") +
				// suggestMsgHoverTxt);
				if (getSettings().isPrivateChatSound()) {
					LabyModCore.getMinecraft().playSound(new ResourceLocation(getSettings().getPrivateChatSoundPath()),
							1.0F);
				}

				if (getSettings().isMsgDisplayNameClick()) {
					String username = "/msg " + getPrivateMessageName(unformatted) + " ";
					int siblingCnt = 0;
					int nameStart = 0;
					int nameEnd = 0;
					for (IChatComponent msgs : msg.getSiblings()) {
						if (nameStart == 0
								&& getHelper().getProperTextFormat(msgs.getFormattedText()).contains("§6[§r")) {
							nameStart = siblingCnt + 1;
						}
						if (nameEnd == 0
								&& getHelper().getProperTextFormat(msgs.getFormattedText()).equals("§6 -> §r")) {
							nameEnd = siblingCnt - 1;
						}
						siblingCnt++;
					}
					for (int i = nameStart; i <= nameEnd; i++) {
						msg.getSiblings().get(i).getChatStyle()
								.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
						// .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
					}
				}
			}

			if (privateMessageSent.find() && getSettings().isMsgDisplayNameClick()) {
				String username = "/msg " + getSentPrivateMessageName(unformatted) + " ";
				int siblingCnt = 0;
				int nameStart = 0;
				int nameEnd = 0;
				for (IChatComponent msgs : msg.getSiblings()) {
					if (nameStart == 0 && getHelper().getProperTextFormat(msgs.getFormattedText()).equals("§6 -> §r")) {
						nameStart = siblingCnt + 1;
					}
					if (nameEnd == 0 && getHelper().getProperTextFormat(msgs.getFormattedText()).equals("§6] §r")) {
						nameEnd = siblingCnt - 1;
					}
					siblingCnt++;
				}
				for (int i = nameStart; i <= nameEnd; i++) {
					msg.getSiblings().get(i).getChatStyle()
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username));
					// .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
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
