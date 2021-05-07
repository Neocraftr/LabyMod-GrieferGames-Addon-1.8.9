package de.neocraftr.griefergames.chat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class IgnoreList extends Chat {
	private static Pattern ignoreListRegex = Pattern.compile("^Ignoriert:(\\s\\w{1,16})+$");

	@Override
	public String getName() {
		return "ignoreList";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(getSettings().isBetterIgnoreList()) {
			Matcher ignoreList = ignoreListRegex.matcher(unformatted);
			return ignoreList.find();
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		List<IChatComponent> ignoreList = msg.getSiblings();
		if (ignoreList.size() == 2) {
			String ignoredNames = ignoreList.get(1).getUnformattedText().trim();
			String[] ignoredNamesArr = ignoredNames.split(" ");
			for (String ignoreName : ignoredNamesArr) {
				getApi().displayMessageInChat(ignoreName);
			}

			msg.getSiblings().remove(1);
		}
		return msg;
	}
}