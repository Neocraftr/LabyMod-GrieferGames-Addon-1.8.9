package de.wuzlwuz.griefergames.chat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class IgnoreList extends Chat {
	private static Pattern ignoreListRegex = Pattern.compile("^Ignoriert:((\\s\\w{1,16})+)$");

	@Override
	public String getName() {
		return "ignoreList";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		Matcher ignoreList = ignoreListRegex.matcher(unformatted);
		if (getSettings().isBetterIgnoreList() && ignoreList.find()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		return doAction(unformatted, formatted);
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		List<IChatComponent> ignoreList = msg.getSiblings();
		if (ignoreList.size() == 2) {
			ChatStyle ignoChatStyle = ignoreList.get(0).getChatStyle().createDeepCopy();
			IChatComponent newMsg = new ChatComponentText("Ignoriert:").setChatStyle(ignoChatStyle);

			String ignoredNames = ignoreList.get(1).getUnformattedText().trim();
			String[] ignoredNamesArr = ignoredNames.split(" ");
			for (String ignoName : ignoredNamesArr) {
				// newMsg.appendSibling(new ChatComponentText("\n"));
				// newMsg.appendSibling(new ChatComponentText(ignoName).setChatStyle(new
				// ChatStyle().setColor(EnumChatFormatting.WHITE)));
				getApi().displayMessageInChat(ignoName);
			}
			msg = newMsg;
		}
		return msg;
	}
}