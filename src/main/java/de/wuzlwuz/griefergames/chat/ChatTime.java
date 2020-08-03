package de.wuzlwuz.griefergames.chat;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatTime extends Chat {
	IChatComponent resetMsg = new ChatComponentText(" ")
			.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RESET));

	@Override
	public String getName() {
		return "chatTime";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		return getSettings().isShowChatTime() && !unformatted.trim().equals("");
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return doAction(unformatted, formatted);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		if (doAction(unformatted, formatted)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String dateNowStr = LocalDateTime.now().format(formatter);

			IChatComponent timeMsg;
			if(getSettings().isChatTimeShortFormat()) {
				timeMsg = new ChatComponentText("§6[§fT§6]");
				timeMsg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(dateNowStr)));
			} else {
				timeMsg = new ChatComponentText("§6[§f"+dateNowStr+"§6]");
			}

			if(getSettings().isChatTimeAfterMessage()) {
				return new ChatComponentText("").appendSibling(msg).appendSibling(resetMsg).appendSibling(timeMsg);
			} else {
				return new ChatComponentText("").appendSibling(timeMsg).appendSibling(resetMsg).appendSibling(msg);
			}
		}

		return super.modifyChatMessage(msg);
	}
}
