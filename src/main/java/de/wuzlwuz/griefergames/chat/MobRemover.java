package de.wuzlwuz.griefergames.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class MobRemover extends Chat {
	private static Pattern mobRemoverMessageRegex = Pattern
			.compile("^\\[MobRemover\\] Achtung, in ([0-9]+) Minuten? werden alle Tiere gel\u00f6scht.$");
	private static Pattern mobRemoverDoneMessageRegex = Pattern
			.compile("^\\[MobRemover\\] Es wurden ([0-9]+) Tiere entfernt.$");

	@Override
	public String getName() {
		return "mobRemover";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(!getSettings().isMobRemoverChatRight()) return false;

		Matcher mobRemoverMessage = mobRemoverMessageRegex.matcher(unformatted);
		Matcher mobRemoverDoneMessage = mobRemoverDoneMessageRegex.matcher(unformatted);
		return unformatted.trim().length() > 0 && (mobRemoverMessage.find() || mobRemoverDoneMessage.find());
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher mobRemoverDoneMessage = mobRemoverDoneMessageRegex.matcher(unformatted);

		return (mobRemoverDoneMessage.find());
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		if (getSettings().isMobRemoverLastTimeHover() && doActionModifyChatMessage(msg)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String dateNowStr = LocalDateTime.now().format(formatter);

			IChatComponent hoverText = new ChatComponentText(dateNowStr);
			msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		}
		return msg;
	}
}
