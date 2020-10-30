package de.wuzlwuz.griefergames.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ItemRemover extends Chat {
	private static Pattern itemRemoverMessageRegex = Pattern.compile(
			"^\\[GrieferGames\\] Warnung! Die auf dem Boden liegenden Items werden in ([0-9]+) Sekunden entfernt!$");
	private static Pattern itemRemoverDoneMessageRegex = Pattern
			.compile("^\\[GrieferGames\\] Es wurden ([0-9]+) auf dem Boden liegende Items entfernt!$");

	@Override
	public String getName() {
		return "itemRemover";
	}


	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(!getSettings().isItemRemoverChatRight()) return false;

		Matcher itemRemoverMessage = itemRemoverMessageRegex.matcher(unformatted);
		Matcher itemRemoverDoneMessage = itemRemoverDoneMessageRegex.matcher(unformatted);
		return unformatted.trim().length() > 0 && (itemRemoverMessage.find() || itemRemoverDoneMessage.find());
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher itemRemoverDoneMessage = itemRemoverDoneMessageRegex.matcher(unformatted);

		return itemRemoverDoneMessage.find();
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		if (getSettings().isItemRemoverLastTimeHover() && doActionModifyChatMessage(msg)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			String dateNowStr = LocalDateTime.now().format(formatter);

			IChatComponent hoverText = new ChatComponentText(dateNowStr);
			msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		}
		return msg;
	}
}
