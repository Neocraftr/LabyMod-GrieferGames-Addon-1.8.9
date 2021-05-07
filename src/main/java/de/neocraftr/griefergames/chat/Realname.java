package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.neocraftr.griefergames.enums.EnumRealnameShown;
import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Realname extends Chat {
	private static Pattern realnameRegex = Pattern.compile("^(\\$\\{\\{dup\\}\\})?(?:\\[[^\\]]+\\])?[A-Za-z\\-]+\\+? \\u2503 \\u007E?\\!?\\w{1,16} ist (\\!?\\w{1,16})$");

	@Override
	public String getName() {
		return "realname";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if(unformatted.trim().length() <= 0) return false;

		if(getSettings().getRealname() != EnumRealnameShown.DEFAULT) {
			Matcher realname = realnameRegex.matcher(unformatted);
			if(realname.find() && realname.group(1) == null) {
				if(getSettings().getRealname() == EnumRealnameShown.BOTH) {
					getApi().displayMessageInChat("${{dup}}" + formatted);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		return ChatDisplayAction.SWAP;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(unformatted.trim().length() <= 0) return false;

		if(getSettings().getRealname() == EnumRealnameShown.BOTH) {
			Matcher realname = realnameRegex.matcher(unformatted);
			return realname.find() && realname.group(1) != null;
		}

		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		IChatComponent newMsg = new ChatComponentText(msg.getFormattedText().replace("${{dup}}", ""));
		return newMsg;
	}
}
