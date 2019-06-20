package de.wuzlwuz.griefergames.server;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.helper.MessageHelper;
import de.wuzlwuz.griefergames.listener.SubServerListener;
import net.labymod.api.LabyModAPI;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.UUIDFetcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GrieferGamesServer extends Server {
	private List<SubServerListener> subServerListener = new ArrayList<SubServerListener>();
	private Minecraft mc;
	private LabyModAPI api;
	private MessageHelper msgHelper;
	private String subServer = "Lobby";
	private long nextLastMessageRequest = System.currentTimeMillis()
			+ (-GrieferGames.getSettings().getFilterDuplicateMessagesTime() * 1000L);
	private long nextScoreboardRequest = System.currentTimeMillis() + (-1 * 1000L);
	private String lastMessage = "";
	private boolean doClearChat = false;
	private boolean changedSubserver = false;

	public Minecraft getMc() {
		return mc;
	}

	public void setMc(Minecraft mc) {
		this.mc = mc;
	}

	public LabyModAPI getApi() {
		return api;
	}

	public void addSubServerListener(SubServerListener ssl) {
		subServerListener.add(ssl);
	}

	private void setApi(LabyModAPI api) {
		this.api = api;
	}

	public MessageHelper getMsgHelper() {
		return msgHelper;
	}

	private void setMsgHelper(MessageHelper msgHelper) {
		this.msgHelper = msgHelper;
	}

	public GrieferGamesServer(Minecraft minecraft) {
		super("GrieferGames", "griefergames.net");
		setMc(minecraft);
		setApi(GrieferGames.getGriefergames().getApi());
		setMsgHelper(new MessageHelper());
		addSubServerListener(new SubServerListener() {
			@Override
			public void onSubServerChanged(String subServerNameOld, String subServerName) {
				if (GrieferGames.getSettings().isModEnabled() && GrieferGames.getSettings().isServerSwitchMsg()) {
					ChatComponentText switchServerMSG = new ChatComponentText("");

					ChatComponentText switchServerMSGBefore = new ChatComponentText(
							"\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500[");
					switchServerMSGBefore.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
					switchServerMSG.appendSibling(switchServerMSGBefore);

					ChatComponentText switchServerMSGBetween = new ChatComponentText("SubServer: " + subServerName);
					switchServerMSGBetween
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA).setBold(true));
					switchServerMSG.appendSibling(switchServerMSGBetween);

					ChatComponentText switchServerMSGAfter = new ChatComponentText(
							"]\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500");
					switchServerMSGAfter.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
					switchServerMSG.appendSibling(switchServerMSGAfter);

					getMc().thePlayer.addChatMessage(switchServerMSG);
				}
			}
		});
	}

	public String getSubServer() {
		return subServer;
	}

	private void setSubServer(String subServer) {
		this.subServer = subServer.trim();
	}

	public String getLastMessage() {
		return lastMessage;
	}

	private void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	private boolean DoClearChat() {
		return this.doClearChat;
	}

	private void setDoClearChat(boolean doClearChat) {
		this.doClearChat = doClearChat;
	}

	private boolean hasChangedSubserver() {
		return this.changedSubserver;
	}

	private void setChangedSubserver(boolean changedSubserver) {
		this.changedSubserver = changedSubserver;
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) throws Exception {
		if (GrieferGames.getSettings().isModEnabled()) {
			try {
				if (getMsgHelper().isBlankMessage(unformatted)) {
					return GrieferGames.getSettings().isCleanBlanks() && !DoClearChat() && !hasChangedSubserver()
							? ChatDisplayAction.HIDE
							: ChatDisplayAction.NORMAL;
				}

				if (GrieferGames.getSettings().isFilterDuplicateMessages() && getLastMessage().equals(formatted)) {
					return ChatDisplayAction.HIDE;
				}

				setLastMessage(formatted);

				if (getMsgHelper().isSupremeBlank(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isCleanSupremeBlanks() ? ChatDisplayAction.HIDE
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().isValidPayMessage(unformatted, formatted) > 0) {
					if (GrieferGames.getSettings().isPayAchievement()) {
						String payerName = getMsgHelper().getPayerName(unformatted);
						String displayName = getMsgHelper().getDisplayName(unformatted);
						UUID playerUUID = UUIDFetcher.getUUID(payerName);
						double money = getMsgHelper().getMoneyPay(unformatted);
						if (money > 0) {
							DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
							if (playerUUID == null) {
								LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
										"$" + moneyFormat.format(money) + " erhalten.",
										"Du hast $" + moneyFormat.format(money) + " von " + displayName + " erhalten.");
							} else {
								LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
										new GameProfile(playerUUID, payerName),
										"$" + moneyFormat.format(money) + " erhalten.",
										"Du hast $" + moneyFormat.format(money) + " von " + displayName + " erhalten.");
							}
						}
					}

					return GrieferGames.getSettings().isPayChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().hasPayedMessage(unformatted, formatted) > 0) {
					if (GrieferGames.getSettings().isPayAchievement()) {
						String payerName = getMsgHelper().getPayerName(unformatted);
						String displayName = getMsgHelper().getDisplayName(unformatted);
						UUID playerUUID = UUIDFetcher.getUUID(payerName);
						double money = getMsgHelper().getMoneyPay(unformatted);
						if (money > 0) {
							DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
							if (playerUUID == null) {
								LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
										"$" + moneyFormat.format(money) + " bezahlt.",
										"Du hast $" + moneyFormat.format(money) + " an " + displayName + " bezahlt.");
							} else {
								LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
										new GameProfile(playerUUID, payerName),
										"$" + moneyFormat.format(money) + " bezahlt.",
										"Du hast $" + moneyFormat.format(money) + " an " + displayName + " bezahlt.");
							}
						}
					}
					return GrieferGames.getSettings().isPayChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().bankPayInMessage(unformatted, formatted) > 0) {
					if (GrieferGames.getSettings().isBankAchievement()) {
						int money = getMsgHelper().getMoneyBank(unformatted);
						if (money > 0) {
							DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
							LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
									"$" + moneyFormat.format(money) + " eingezahlt.",
									"Du hast $" + moneyFormat.format(money) + " in die Bank eingezahlt.");
						}
					}
					return GrieferGames.getSettings().isBankChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().bankPayOutMessage(unformatted, formatted) > 0) {
					if (GrieferGames.getSettings().isBankAchievement()) {
						int money = getMsgHelper().getMoneyBank(unformatted);
						if (money > 0) {
							DecimalFormat moneyFormat = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.ENGLISH);
							LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(
									"$" + moneyFormat.format(money) + " abgehoben.",
									"Du hast $" + moneyFormat.format(money) + " von der Bank abgehoben.");
						}
					}
					return GrieferGames.getSettings().isBankChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().bankBalanceMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isBankChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().bankMessageOther(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isBankChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().isValidPrivateMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isPrivateChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().isValidSendPrivateMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isPrivateChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().clearLagMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isClearlagChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().mobRemoverMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isMobRemoverChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().mobRemoverDoneMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isMobRemoverChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().isClearChatMessage(unformatted, formatted) > 0) {
					setDoClearChat(false);
					return ChatDisplayAction.NORMAL;
				} else if (getMsgHelper().isPlotChatMessage(unformatted, formatted) > 0) {
					return GrieferGames.getSettings().isPlotChatRight() ? ChatDisplayAction.SWAP
							: ChatDisplayAction.NORMAL;
				} else if (hasChangedSubserver()) {
					setChangedSubserver(false);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ChatDisplayAction.NORMAL;
	}

	@Override
	public void fillSubSettings(List<SettingsElement> settings) {

	}

	@Override
	public void handlePluginMessage(String channelName, PacketBuffer packetBuffer) throws Exception {

	}

	@Override
	public void handleTabInfoMessage(TabListEvent.Type tabInfoType, String formatted, String clean) throws Exception {

	}

	@Override
	public void onJoin(ServerData serverData) {
		this.getApi().getEventManager().register(new MessageModifyChatEvent() {
			@Override
			public Object onModifyChatMessage(Object o) {
				return modifyChatMessage(o);
			}
		});
		this.getApi().registerForgeListener(this);
	}

	public Object modifyChatMessage(Object o) {
		if (!GrieferGames.getSettings().isModEnabled())
			return o;

		try {
			IChatComponent msg = (IChatComponent) o;

			if (msg.getUnformattedText().length() == 0)
				return msg;

			MessageHelper msgHelper = getMsgHelper();
			String unformatted = msg.getUnformattedText();
			String formatted = msg.getFormattedText();

			System.out.println(formatted);

			if (GrieferGames.getSettings().isPayHover() || GrieferGames.getSettings().isPayMarker()) {
				if (msgHelper.isValidPayMessage(unformatted, formatted) > 0) {
					if (GrieferGames.getSettings().isPayMarker()) {
						IChatComponent checkmarkText = new ChatComponentText(" \u2714")
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
						msg.appendSibling(checkmarkText);
					}
					if (GrieferGames.getSettings().isPayHover()) {
						IChatComponent hoverText = new ChatComponentText("Es ist eine valide Zahlung!");
						msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
					}
				}
			}
			if (msgHelper.isClearChatMessage(unformatted, formatted) > 0) {
				setDoClearChat(true);
				IChatComponent newMsg = new ChatComponentText("\n");
				for (int i = 0; i < 100; i++) {
					newMsg.appendSibling(new ChatComponentText("\n"));
				}
				newMsg.appendSibling(msg);
				return newMsg;
			}
			if (GrieferGames.getSettings().isBetterIgnoreList()
					&& msgHelper.isIngnoreListChatMessage(unformatted, formatted) > 0) {
				List<IChatComponent> ignoreList = msg.getSiblings();
				if (ignoreList.size() == 2) {
					ChatStyle ignoChatStyle = ignoreList.get(0).getChatStyle().createDeepCopy();
					IChatComponent newMsg = new ChatComponentText("Ignoriert: ").setChatStyle(ignoChatStyle);

					String ignoredNames = ignoreList.get(1).getUnformattedText().trim();
					String[] ignoredNamesArr = ignoredNames.split(" ");
					for (String ignoName : ignoredNamesArr) {
						newMsg.appendSibling(new ChatComponentText("\n"));
						newMsg.appendSibling(new ChatComponentText(" - " + ignoName)
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE)));
					}

					return newMsg;
				}
			}
			if (GrieferGames.getSettings().isMobRemoverLastTimeHover()
					&& msgHelper.mobRemoverDoneMessage(unformatted, formatted) > 0) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				String dateNowStr = LocalDateTime.now().format(formatter);

				IChatComponent hoverText = new ChatComponentText(dateNowStr);
				msg.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (LabyModCore.getMinecraft().getWorld() != null && event.phase == TickEvent.Phase.START) {
			if (System.currentTimeMillis() > this.nextLastMessageRequest) {
				this.nextLastMessageRequest = System.currentTimeMillis()
						+ GrieferGames.getSettings().getFilterDuplicateMessagesTime() * 1000L;
				setLastMessage("");
			}
			if (System.currentTimeMillis() > this.nextScoreboardRequest) {
				this.nextScoreboardRequest = System.currentTimeMillis() + 500L;
				Scoreboard scoreboard = LabyModCore.getMinecraft().getWorld().getScoreboard();
				ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(1);
				if (scoreobjective != null) {
					List<Score> scoreList = (List<Score>) scoreboard.getSortedScores(scoreobjective);
					Collections.reverse(scoreList);
					for (int i = 0; i < scoreList.size(); i++) {
						ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i).getPlayerName());
						String scoreName = ScorePlayerTeam.formatPlayerName(scorePlayerTeam,
								scoreList.get(i).getPlayerName());
						if (scoreName.indexOf("Server:") > 0) {
							scorePlayerTeam = scoreboard.getPlayersTeam(scoreList.get(i + 1).getPlayerName());
							scoreName = ScorePlayerTeam
									.formatPlayerName(scorePlayerTeam, scoreList.get(i + 1).getPlayerName())
									.replaceAll("§[0-9a-z]", "").trim();
							if (!getSubServer().matches(scoreName)) {
								for (SubServerListener ssl : subServerListener)
									ssl.onSubServerChanged(getSubServer(), scoreName);
								setSubServer(scoreName);
							}
							i = scoreList.size();
						}
					}
				}
			}
		}
	}
}