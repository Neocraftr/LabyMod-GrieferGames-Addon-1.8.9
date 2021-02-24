package de.neocraftr.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class Teleport extends Chat {
	private static Pattern tpaMesssageRegexp = Pattern
			.compile("^([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) fragt, ob er sich zu dir teleportieren darf.$");
	private static Pattern tpahereMesssageRegexp = Pattern
			.compile("^([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\!?\\w{1,16}) fragt, ob du dich zu ihm teleportierst.$");

	@Override
	public String getName() {
		return "teleport";
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		if(getSettings().isMarkTPAMsg() && unformatted.trim().length() > 0) {
			Matcher tpaMesssage = tpaMesssageRegexp.matcher(unformatted);
			Matcher tpahereMesssage = tpahereMesssageRegexp.matcher(unformatted);

			return tpaMesssage.find() || tpahereMesssage.find();
		}
		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();

		Matcher tpaMesssage = tpaMesssageRegexp.matcher(unformatted);
		Matcher tpahereMesssage = tpahereMesssageRegexp.matcher(unformatted);

		if (tpaMesssage.find()) {
			IChatComponent beforeTpaMsg = new ChatComponentText("[TPA] ")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_GREEN).setBold(true));
			IChatComponent newMsg = new ChatComponentText("").appendSibling(beforeTpaMsg).appendSibling(msg);
			return newMsg;
		}

		if (tpahereMesssage.find()) {
			IChatComponent beforeTpaMsg = new ChatComponentText("[TPAHERE] ")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED).setBold(true));
			IChatComponent newMsg = new ChatComponentText("").appendSibling(beforeTpaMsg).appendSibling(msg);
			return newMsg;
		}

		return msg;
	}
}