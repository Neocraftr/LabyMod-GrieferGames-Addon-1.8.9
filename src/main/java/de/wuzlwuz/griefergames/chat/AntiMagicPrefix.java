package de.wuzlwuz.griefergames.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class AntiMagicPrefix extends Chat {
	private static Pattern antiMagixPrefixRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 ((\\u007E)?\\w{1,16})");

	@Override
	public String getName() {
		return "antiMagicPrefix";
	}

	@Override
	public boolean doAction(String unformatted, String formatted) {
		String oldMessage = getHelper().getProperTextFormat(formatted);

		Matcher antiMagixPrefix = antiMagixPrefixRegex.matcher(unformatted);

		if (getSettings().isAMPEnabled() && unformatted.trim().length() > 0 && oldMessage.indexOf("§k") != -1
				&& antiMagixPrefix.find())
			return true;

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
		if (doActionModifyChatMessage(msg)) {
			IChatComponent newMsg = new ChatComponentText("");
			Matcher antiMagixPrefix = antiMagixPrefixRegex.matcher(msg.getUnformattedText());

			// §r§6Spieler in der Nähe:§r
			// §r§c§kSupreme§r§8 ┃ §r§c§kCobbleShop_MM§r§f(§r§446m§r§f),
			// §r§c§kGriefer§r§8 ┃ §r§c§kRatschkatl§r§f(§r§44m§r§f)§r

			if (antiMagixPrefix.find()) {
				if (msg.getUnformattedText().trim().toLowerCase().startsWith("spieler in der")) {
					for (IChatComponent component : msg.getSiblings()) {
						if (component.getChatStyle().getObfuscated()) {
							int curIdx = msg.getSiblings().indexOf(component);
							if (msg.getSiblings().get(curIdx + 1).getUnformattedText().trim().contentEquals("\u2503")) {
								ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false);
								String chatRepText = getSettings().getAMPChatReplacement();

								if (chatRepText.indexOf("%CLEAN%") == -1) {
									chatRepText = getSettings().getDefaultAMPChatReplacement();
								}

								chatRepText = chatRepText.replaceAll("%CLEAN%", component.getUnformattedText());
								chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

								newMsg.appendSibling(new ChatComponentText(
										chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
												.setChatStyle(msgStyling));
							} else {
								ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false);
								newMsg.appendSibling(component.setChatStyle(msgStyling));
							}
						} else {
							newMsg.appendSibling(component);
						}
					}
				} else {
					for (IChatComponent component : msg.getSiblings()) {
						if (component.getChatStyle().getObfuscated()
								&& component.getUnformattedText().equalsIgnoreCase(antiMagixPrefix.group(1).trim())) {
							ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false);
							String chatRepText = getSettings().getAMPChatReplacement();

							if (chatRepText.indexOf("%CLEAN%") == -1) {
								chatRepText = getSettings().getDefaultAMPChatReplacement();
							}

							chatRepText = chatRepText.replaceAll("%CLEAN%", component.getUnformattedText());
							chatRepText = "${REPSTART}" + chatRepText + "${REPEND}";

							newMsg.appendSibling(new ChatComponentText(
									chatRepText.replace("${REPSTART}", "").replace("${REPEND}", ""))
											.setChatStyle(msgStyling));
						} else if (component.getChatStyle().getObfuscated()
								&& component.getUnformattedText().equalsIgnoreCase(antiMagixPrefix.group(2))) {
							ChatStyle msgStyling = component.getChatStyle().createDeepCopy().setObfuscated(false);
							newMsg.appendSibling(component.setChatStyle(msgStyling));
						} else {
							newMsg.appendSibling(component);
						}
					}
				}
				return newMsg;
			}
		}

		return msg;
	}
}