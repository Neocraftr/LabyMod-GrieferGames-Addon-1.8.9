package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nickname extends Chat {

	private static Pattern nicknameMsgRegex = Pattern.compile("^\\[Nick\\] Dein neuer Name lautet nun (\\w+)\\.$");

	@Override
	public String getName() {
		return "nickname";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if (unformatted.trim().length() > 0) {
			Matcher nicknameMsg = nicknameMsgRegex.matcher(unformatted);
			if (nicknameMsg.find()) {
				getGG().setNickname(nicknameMsg.group(1));
			} else if (unformatted.trim().equalsIgnoreCase("[Nick] Dein Name wurde zurückgesetzt.")) {
				getGG().setNickname("");
			}
		}

		return false;
	}
}
