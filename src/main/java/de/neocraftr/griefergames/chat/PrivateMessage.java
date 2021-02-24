package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.core.LabyModCore;
import net.labymod.main.lang.LanguageManager;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
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

	public boolean doAction(String unformatted) {
		if(unformatted.trim().length() > 0) {
			Matcher privateMessage = privateMessageRegex.matcher(unformatted);
			Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);

			return privateMessage.find() || privateMessageSent.find();
		}
		return false;
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		return getSettings().isPrivateChatRight() && doAction(unformatted);
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		return doAction(unformatted);
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher privateMessage = privateMessageRegex.matcher(unformatted);
		Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);

		String suggestMsgHoverTxt =
				LanguageManager.translateOrReturnKey("message_gg_suggestMsgHoverMsg");
		IChatComponent hoverText = new ChatComponentText(ModColor.cl("a") + suggestMsgHoverTxt);

		if (privateMessage.find()) {
			if (getSettings().isPrivateChatSound()) {
				LabyModCore.getMinecraft().playSound(new ResourceLocation(getHelper().getSoundPath(getSettings().getPrivateChatSound())), 1.0F);
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
							.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username))
							.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
				}
			}

			if(getSettings().isAfkMsgAnswear() && getGG().isAfk()) {
				String message = getSettings().getAfkMsgText();
				if(message.length() > 0) {
					if(message.startsWith("~")) message = message.replaceFirst("~", "");
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/msg "+getPrivateMessageName(unformatted)+" "+message);
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
						.setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, username))
						.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
			}
		}

		return msg;
	}

	private String getSentPrivateMessageName(String unformatted) {
		Matcher privateMessageSent = privateMessageSentRegex.matcher(unformatted);
		if (privateMessageSent.find()) {
			String msg = privateMessageSent.group(2);
			if(msg.startsWith("~")) msg = msg.replaceFirst("~", "");
			return msg;
		}
		return "";
	}

	private String getPrivateMessageName(String unformatted) {
		Matcher privateMessage = privateMessageRegex.matcher(unformatted);
		if (privateMessage.find()) {
			String msg = privateMessage.group(2);
			if(msg.startsWith("~")) msg = msg.replaceFirst("~", "");
			return msg;
		}
		return "";
	}
}
