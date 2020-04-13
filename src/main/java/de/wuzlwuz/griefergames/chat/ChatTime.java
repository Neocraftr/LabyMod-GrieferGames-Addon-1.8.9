package de.wuzlwuz.griefergames.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
		return getSettings().isShowChatTime();
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return (doAction(unformatted, formatted) && true);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		if (doAction(unformatted, formatted)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String dateNowStr = LocalDateTime.now().format(formatter);

			IChatComponent befTimeMsg = new ChatComponentText("[")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
			IChatComponent timeMsg = new ChatComponentText(dateNowStr)
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
			IChatComponent aftTimeMsg = new ChatComponentText("]")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));

			IChatComponent newMsg = new ChatComponentText("").appendSibling(befTimeMsg).appendSibling(timeMsg)
					.appendSibling(aftTimeMsg).appendSibling(resetMsg).appendSibling(msg);

			return newMsg;
		}

		return super.modifyChatMessage(msg);
	}
}
