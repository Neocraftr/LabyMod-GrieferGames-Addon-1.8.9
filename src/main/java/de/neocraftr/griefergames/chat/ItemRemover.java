package de.neocraftr.griefergames.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ItemRemover extends Chat {
	private static Pattern itemRemoverMessageRegex = Pattern.compile("^\\[GrieferGames\\] Warnung! Die auf dem Boden liegenden Items werden in ([0-9]+) Sekunden entfernt!$");
	private static Pattern itemRemoverDoneMessageRegex = Pattern.compile("^\\[GrieferGames\\] Es wurden ([0-9]+) auf dem Boden liegende Items entfernt!$");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public String getName() {
		return "itemRemover";
	}


	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(getSettings().isItemRemoverChatRight() && unformatted.trim().length() > 0) {
			Matcher itemRemoverMessage = itemRemoverMessageRegex.matcher(unformatted);
			Matcher itemRemoverDoneMessage = itemRemoverDoneMessageRegex.matcher(unformatted);

			return itemRemoverMessage.find() || itemRemoverDoneMessage.find();
		}

		return false;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(getSettings().isItemRemoverLastTimeHover() && unformatted.trim().length() > 0) {
			Matcher itemRemoverDoneMessage = itemRemoverDoneMessageRegex.matcher(unformatted);
			return itemRemoverDoneMessage.find();
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
