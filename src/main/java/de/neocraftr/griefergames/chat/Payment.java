package de.neocraftr.griefergames.chat;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.authlib.GameProfile;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.utils.ModColor;
import net.labymod.utils.UUIDFetcher;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class Payment extends Chat {
	private static Pattern getMoneyValidRegex = Pattern.compile("^([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) hat dir \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
	private static Pattern payedMoneyRegex = Pattern.compile("^Du hast ([A-Za-z\\-\\+]+) \\u2503 (~?\\!?\\w{1,16}) \\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) gegeben\\.$");
	private static Pattern earnedMoneyRegex = Pattern.compile("\\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?) wurde zu deinem Konto hinzugefügt\\.$");
	private static Pattern getMoneyRegex = Pattern.compile("\\$((?:[1-9]\\d{0,2}(?:,\\d{1,3})*|0)(?:\\.\\d+)?)");

	@Override
	public String getName() {
		return "payment";
	}

	@Override
	public boolean doActionHandleChatMessage(String unformatted, String formatted) {
		if (!formatted.contains("§r§f §r§ahat dir $")) {
			Matcher matcher = getMoneyValidRegex.matcher(unformatted);
			if (matcher.find()) {
				String name = getHelper().getPlayerName(unformatted);
				double amount = getMoneyPay(unformatted);
				getGG().setIncome(getGG().getIncome() + amount);
				getGG().getFileManager().logTransaction(name, amount, true);

				return true;
			}
		}

		Matcher payedMoney = payedMoneyRegex.matcher(unformatted);
		if(payedMoney.find()) {
			String name = getHelper().getPlayerName(unformatted);
			double amount = getMoneyPay(unformatted);
			getGG().setIncome(getGG().getIncome() - amount);
			getGG().getFileManager().logTransaction(name, amount, false);

			return true;
		}

		Matcher earnedMoney = earnedMoneyRegex.matcher(unformatted);
		if(earnedMoney.find()) {
			double amount = getMoneyPay(unformatted);
			getGG().setIncome(getGG().getIncome() + getMoneyPay(unformatted));
			getGG().getFileManager().logTransaction("Moneydrop", amount, true);

			return true;
		}

		return false;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) {
		if (getSettings().isPayAchievement()) {
			String payerName = getHelper().getPlayerName(unformatted);
			String displayName = getHelper().getDisplayName(unformatted);
			if(!payerName.equals("") && !displayName.equals("")) {
				UUID playerUUID = UUIDFetcher.getUUID(payerName);
				double money = getMoneyPay(unformatted);
				if (money > 0) {
					DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);

					String title = LanguageManager.translateOrReturnKey("message_gg_gotMoney");
					String desc = LanguageManager.translateOrReturnKey("message_gg_gotMoneyFrom");

					Matcher payedMoney = payedMoneyRegex.matcher(unformatted);
					if (payedMoney.find()) {
						title = LanguageManager.translateOrReturnKey("message_gg_paidMoney");
						desc = LanguageManager.translateOrReturnKey("message_gg_paidMoneyTo");
					}

					title = title.replace("{money}", moneyFormat.format(money));
					desc = desc.replace("{money}", moneyFormat.format(money));
					desc = desc.replace("{name}", displayName);

					if (playerUUID == null) {
						LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(title, desc);
					} else {
						LabyMod.getInstance().getGuiCustomAchievement()
								.displayAchievement(new GameProfile(playerUUID, payerName), title, desc);
					}
				}
			}
		}

		if(getSettings().isPayChatRight()) {
			return ChatDisplayAction.SWAP;
		}

		return ChatDisplayAction.NORMAL;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		String unformatted = msg.getUnformattedText();
		String formatted = msg.getFormattedText();

		if (!formatted.contains("§r§f §r§ahat dir $")) {
			Matcher matcher = getMoneyValidRegex.matcher(unformatted);
			return matcher.find();
		}

		return false;
	}

	@Override
	public IChatComponent modifyChatMessage(IChatComponent msg) {
		if (getSettings().isPayMarker()) {
			IChatComponent checkmarkText = new ChatComponentText(" \u2714")
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
			msg.appendSibling(checkmarkText);
		}

		if (getSettings().isPayHover()) {
			String ValidPayment = LanguageManager.translateOrReturnKey("message_gg_validPayment");

			IChatComponent hoverText = new ChatComponentText(ValidPayment);
			msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
		}

		return msg;
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (unformatted.toLowerCase().startsWith("/pay") && unformatted.toLowerCase().contains(",")) {
			getMC().thePlayer.sendChatMessage(unformatted.replace(",", "."));
			return true;
		}

		if(unformatted.toLowerCase().startsWith("/resetincome")) {
			getGG().setIncome(0);
			getGG().getApi().displayMessageInChat(GrieferGames.PREFIX+ModColor.GRAY+LanguageManager.translateOrReturnKey("message_gg_income_reset"));
			return true;
		}
		return false;
	}

	@Override
	public boolean commandMessage(String unformatted) {
		return true;
	}

	public double getMoneyPay(String unformatted) {
		double money = 0.0;
		Matcher matcher = getMoneyRegex.matcher(unformatted);
		if (matcher.find()) {
			String moneyStr = matcher.group(1).trim();
			if (moneyStr.length() > 0) {
				moneyStr = moneyStr.replace("$", "");
				moneyStr = moneyStr.replaceAll(",", "");
				try {
					money = Double.parseDouble(moneyStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return money;
	}
}