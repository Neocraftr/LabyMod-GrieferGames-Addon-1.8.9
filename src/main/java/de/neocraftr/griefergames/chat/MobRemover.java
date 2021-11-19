package de.neocraftr.griefergames.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class MobRemover extends Chat {
	private static Pattern mobRemoverDoneMessageRegex = Pattern.compile("^\\[MobRemover\\] Es wurden ([0-9]+) Tiere entfernt\\.$");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public String getName() {
		return "mobRemover";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(getSettings().isMobRemoverChatRight()) {
			return unformatted.startsWith("[MobRemover]");
		}
		return false;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(getSettings().isMobRemoverLastTimeHover() && unformatted.trim().length() > 0) {
			Matcher mobRemoverDoneMessage = mobRemoverDoneMessageRegex.matcher(unformatted);
			return mobRemoverDoneMessage.find();
		}
		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String dateNowStr = LocalDateTime.now().format(formatter);

		IChatComponent hoverText = new ChatComponentText(dateNowStr);
		msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		return msg;
	}
}
