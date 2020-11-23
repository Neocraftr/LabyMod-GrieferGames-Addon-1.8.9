package de.wuzlwuz.griefergames.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonObject;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.chat.AntiMagicClanTag;
import de.wuzlwuz.griefergames.chat.AntiMagicPrefix;
import de.wuzlwuz.griefergames.chat.Bank;
import de.wuzlwuz.griefergames.chat.Blanks;
import de.wuzlwuz.griefergames.chat.Chat;
import de.wuzlwuz.griefergames.chat.ChatTime;
import de.wuzlwuz.griefergames.chat.CheckPlot;
import de.wuzlwuz.griefergames.chat.ClanTag;
import de.wuzlwuz.griefergames.chat.ClearChat;
import de.wuzlwuz.griefergames.chat.GlobalMessage;
import de.wuzlwuz.griefergames.chat.IgnoreList;
import de.wuzlwuz.griefergames.chat.ItemRemover;
import de.wuzlwuz.griefergames.chat.LobbyHub;
import de.wuzlwuz.griefergames.chat.MobRemover;
import de.wuzlwuz.griefergames.chat.News;
import de.wuzlwuz.griefergames.chat.Nickname;
import de.wuzlwuz.griefergames.chat.Payment;
import de.wuzlwuz.griefergames.chat.PlotChat;
import de.wuzlwuz.griefergames.chat.PreventCommandFailure;
import de.wuzlwuz.griefergames.chat.PrivateMessage;
import de.wuzlwuz.griefergames.chat.Realname;
import de.wuzlwuz.griefergames.chat.SupremeBlanks;
import de.wuzlwuz.griefergames.chat.Teleport;
import de.wuzlwuz.griefergames.chat.VanishHelper;
import de.wuzlwuz.griefergames.chat.Vote;
import de.wuzlwuz.griefergames.gui.VanishHelperGui;
import de.wuzlwuz.griefergames.utils.Helper;
import de.wuzlwuz.griefergames.listener.OnTickListener;
import de.wuzlwuz.griefergames.listener.PreRenderListener;
import de.wuzlwuz.griefergames.listener.SubServerListener;
import de.wuzlwuz.griefergames.modules.*;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAPI;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrieferGamesServer extends Server {
	private List<SubServerListener> subServerListener = new ArrayList<SubServerListener>();
	private String subServer = "";
	private String lastLabyChatSubServer = "", lastDiscordSubServer = "";
	private long nextLastMessageRequest = System.currentTimeMillis() + 1000L;
	private long nextScoreboardRequest = System.currentTimeMillis() + (-1 * 1000L);
	private long nextCheckFly = System.currentTimeMillis() + 1000L;
	private long nextUpdateTimeToWait = System.currentTimeMillis() + 1000L;
	private String lastMessage = "";
	private boolean modulesLoaded = false;
	private boolean listenerLoaded = false;

	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	private ModSettings getSettings() {
		return GrieferGames.getSettings();
	}

	private Helper getHelper() {
		return GrieferGames.getGriefergames().getHelper();
	}

	private LabyModAPI getApi() {
		return GrieferGames.getGriefergames().getApi();
	}

	private Minecraft getMC() {
		return Minecraft.getMinecraft();
	}

	public void addSubServerListener(SubServerListener ssl) {
		if (!subServerListener.contains(ssl)) {
			subServerListener.add(ssl);
		}
	}
	public List<SubServerListener> getSubServerListener() {
		return subServerListener;
	}

	public GrieferGamesServer() {
		super("GrieferGames", GrieferGames.SERVER_IP,
				GrieferGames.SECOND_SERVER_IP);

		ModuleCategoryRegistry.loadCategory(getGG().getModuleCategory());

		new BoosterModule();
		new FlyModule();
		new NicknameModule();
		new DelayModule();
		new IncomeModule();

		// add Chat Modules
		getGG().addChatModule(new PreventCommandFailure());
		getGG().addChatModule(new ClearChat());
		getGG().addChatModule(new Blanks());
		getGG().addChatModule(new SupremeBlanks());
		getGG().addChatModule(new News());
		getGG().addChatModule(new PrivateMessage());
		getGG().addChatModule(new Payment());
		getGG().addChatModule(new Bank());
		getGG().addChatModule(new ItemRemover());
		getGG().addChatModule(new MobRemover());
		getGG().addChatModule(new PlotChat());
		getGG().addChatModule(new Vote());
		getGG().addChatModule(new GlobalMessage());
		getGG().addChatModule(new LobbyHub());
		getGG().addChatModule(new Realname());
		getGG().addChatModule(new IgnoreList());
		getGG().addChatModule(new ClanTag());
		getGG().addChatModule(new AntiMagicClanTag());
		getGG().addChatModule(new Teleport());
		getGG().addChatModule(new AntiMagicPrefix());
		getGG().addChatModule(new Nickname());
		getGG().addChatModule(new CheckPlot());
		getGG().addChatModule(new VanishHelper());
		getGG().addChatModule(new ChatTime());

		addSubServerListener(new SubServerListener() {
			@Override
			public void onSubServerChanged(String subServerNameOld, String subServerName) {
				getGG().setNickname("");
				getGG().setNewsStart(false);

				if(getSettings().isLabyChatShowSubServerEnabled()) {
					getGG().getHelper().updateLabyChatSubServer(subServerName);
				}
				if(getSettings().isDiscordShowSubServerEnabled()) {
					getGG().getHelper().updateDiscordSubServer(subServerName);
				}

				if (getHelper().doResetBoosterBySubserver(subServerName)) {
					getGG().getBoosters().clear();
				} else {
					if (getSettings().isVanishHelper() && getHelper().showVanishModule(getGG().getPlayerRank())) {
						final VanishHelperGui vanishHelper = new VanishHelperGui();
						getMC().displayGuiScreen(vanishHelper);
						Thread thread = new Thread() {
							public void run() {
								try {
									Thread.sleep(3000);
									if (getMC().currentScreen != null && getMC().currentScreen.equals(vanishHelper)) {
										getMC().displayGuiScreen(null);
									}
								} catch (Exception e) {
									System.err.println(e);
								}
							}
						};

						thread.start();
					}
				}

				if(getHelper().doHaveToWaitAfterJoin(subServerName)) {
					getGG().setTimeToWait(15);
				} else if(subServerName.equalsIgnoreCase("portal")) {
					getGG().setTimeToWait(12);
				} else {
					getGG().setTimeToWait(0);
				}

				if (subServerName.equalsIgnoreCase("lobby")) {
					getGG().setTimeToWait(0);
					//getGG().setShowBoosterDummy(true);
					if (!loadPlayerRank()) {
						Thread thread = new Thread() {
							public void run() {
								try {
									Thread.sleep(500);
									if (!loadPlayerRank()) {
										Thread.sleep(500);
										if (!loadPlayerRank()) {
											getApi().displayMessageInChat(GrieferGames.PREFIX+"§4"+ LanguageManager.translateOrReturnKey("message_gg_error")+
													": §c"+LanguageManager.translateOrReturnKey("message_gg_rankError"));
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						thread.start();
					}
				} else {
					// Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().clearLoadedMaps();
					//getGG().setShowBoosterDummy(false);
				}

				getGG().setGodActive(false);
				getGG().setVanishActive(getHelper().vanishDefaultState(getGG().getPlayerRank()));
			}
		});
	}

	public String getSubServer() {
		return subServer;
	}
	public void setSubServer(String subServer) {
		this.subServer = subServer.trim();
	}

	public String getLastDiscordSubServer() {
		return lastDiscordSubServer;
	}
	public void setLastDiscordSubServer(String lastDiscordSubServer) {
		this.lastDiscordSubServer = lastDiscordSubServer;
	}

	public String getLastLabyChatSubServer() {
		return lastLabyChatSubServer;
	}
	public void setLastLabyChatSubServer(String lastLabyChatSubServer) {
		this.lastLabyChatSubServer = lastLabyChatSubServer;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public void setNextLastMessageRequest(long nextLastMessageRequest) {
		this.nextLastMessageRequest = nextLastMessageRequest;
	}

	public long getNextLastMessageRequest() {
		return this.nextLastMessageRequest;
	}

	public void setNextScoreboardRequest(long nextScoreboardRequest) {
		this.nextScoreboardRequest = nextScoreboardRequest;
	}

	public long getNextScoreboardRequest() {
		return this.nextScoreboardRequest;
	}

	public void setNextCheckFly(long nextCheckFly) {
		this.nextCheckFly = nextCheckFly;
	}

	public long getNextCheckFly() {
		return this.nextCheckFly;
	}

	public void setNextUpdateTimeToWait(long nextUpdateTimeToWait) {
		this.nextUpdateTimeToWait = nextUpdateTimeToWait;
	}

	public long getNextUpdateTimeToWait() {
		return nextUpdateTimeToWait;
	}

	private boolean loadPlayerRank() {
		String accountName = LabyModCore.getMinecraft().getPlayer().getName().trim();

		try {
			NetHandlerPlayClient nethandlerplayclient = LabyModCore.getMinecraft().getPlayer().sendQueue;
			Collection<NetworkPlayerInfo> playerMap = nethandlerplayclient.getPlayerInfoMap();

			for (NetworkPlayerInfo player : playerMap) {
				IChatComponent tabListName = player.getDisplayName();
				// String teamName = player.getPlayerTeam().getTeamName();
				// String registeredName = player.getGameProfile().getName();

				if (tabListName != null) {
					if (accountName.length() > 0 && accountName
							.equalsIgnoreCase(getHelper().getPlayerName(tabListName.getUnformattedText()).trim())) {

						getGG().setPlayerRank(getHelper().getPlayerRank(tabListName.getUnformattedText().trim()));
						getGG().setIsInTeam(getHelper().isInTeam(getGG().getPlayerRank()));
					}
				}
				/*
				 * else if (accountName.length() > 0 && registeredName.length() > 0 &&
				 * teamName.length() > 0 &&
				 * accountName.trim().equalsIgnoreCase(registeredName.trim())) {
				 * setPlayerRank(getHelper().getPlayerRank(teamName.trim()));
				 * getGG().setIsInTeam(getHelper().isInTeam(getPlayerRank())); }
				 */
			}
			if (!modulesLoaded) {
				modulesLoaded = true;

				if (getHelper().showGodModule(getGG().getPlayerRank())) {
					new GodmodeModule();
				}
				if (getHelper().showAuraModule(getGG().getPlayerRank())) {
					new AuraModule();
				}
				if (getHelper().showVanishModule(getGG().getPlayerRank())) {
					new VanishModule();
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	@Override
	public ChatDisplayAction handleChatMessage(String unformatted, String formatted) throws Exception {
		if (getSettings().isModEnabled()) {
			try {
				if (getSettings().isFilterDuplicateMessages() && getLastMessage().equals(formatted)) {
					setLastMessage(formatted);
					return ChatDisplayAction.HIDE;
				}
				setLastMessage(formatted);

				List<Chat> chatModules = getGG().getChatModules();
				for (Chat chatModule : chatModules) {
					if (chatModule.doActionHandleChatMessage(unformatted, formatted)) {
						ChatDisplayAction retVal = chatModule.handleChatMessage(unformatted, formatted);
						if (retVal != ChatDisplayAction.NORMAL) {
							return retVal;
						}
					}
				}

				getHelper().isValidBoosterMessage(unformatted, formatted);
				getHelper().isValidBoosterDoneMessage(unformatted, formatted);
				getHelper().isValidBoosterMultiDoneMessage(unformatted, formatted);
				getHelper().checkCurrentBoosters(unformatted, formatted);

				if (getSettings().isUpdateBoosterState() && getHelper().isSwitcherDoneMsg(unformatted, formatted) > 0) {
					getGG().getBoosters().clear();
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/booster");
				}

				int status = getHelper().isVanishMessage(unformatted, formatted);
				if (status >= 0) {
					getGG().setVanishActive(status > 0);
					return ChatDisplayAction.NORMAL;
				}

				status = getHelper().isGodmodeMessage(unformatted, formatted);
				if (status >= 0) {
					getGG().setGodActive(status > 0);
					return ChatDisplayAction.NORMAL;
				}

				status = getHelper().isAuraMessage(unformatted, formatted);
				if (status >= 0) {
					getGG().setAuraActive(status > 0);
					return ChatDisplayAction.NORMAL;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ChatDisplayAction.NORMAL;
	}

	@Override
	public void fillSubSettings(List<SettingsElement> settings) {}

	@Override
	public void handlePluginMessage(String channelName, PacketBuffer packetBuffer) throws Exception {}

	@Override
	public void handleTabInfoMessage(TabListEvent.Type tabInfoType, String formatted, String clean) throws Exception {}

	@Override
	public void onJoin(ServerData serverData) {
		subServer = "";
		lastLabyChatSubServer = "";
		lastDiscordSubServer = "";
		getGG().setNickname("");
		getGG().setShowModules(true);

		if (!listenerLoaded) {
			this.getApi().getEventManager().register(new MessageModifyChatEvent() {

				@Override
				@SubscribeEvent(priority = EventPriority.HIGH)
				public Object onModifyChatMessage(Object o) {
					return modifyChatMessage(o);
				}

			});

			getApi().registerForgeListener(new PreRenderListener());
			getApi().registerForgeListener(new OnTickListener());

			getApi().getEventManager().register(new MessageSendEvent() {
				public boolean onSend(String message) {

					if (!getSettings().isModEnabled())
						return false;

					List<Chat> chatModules = getGG().getChatModules();
					for (Chat chatModule : chatModules) {
						if (chatModule.doActionCommandMessage(message)) {
							return chatModule.commandMessage(message);
						}
					}

					return false;
				}
			});

			getApi().getEventManager().register(new MessageReceiveEvent() {
				@Override
				public boolean onReceive(String formatted, String unformatted) {
					if (!getSettings().isModEnabled())
						return false;

					if (unformatted.startsWith("[SCAMMER] ")) {
						String newFormatted = formatted.replaceFirst("((§r)?§6\\[§r§([0-9]|[a-f])§lSCAMMER§r§6\\]§r§r)", "").trim();
						formatted = getHelper().getProperChatFormat(newFormatted);

						String newUnformatted = unformatted.replaceFirst("\\[SCAMMER\\]", "").trim();
						unformatted = newUnformatted;
					}

					List<Chat> chatModules = getGG().getChatModules();
					for (Chat chatModule : chatModules) {
						if (chatModule.doActionReceiveMessage(formatted, unformatted)) {
							return chatModule.receiveMessage(formatted, unformatted);
						}
					}

					return false;
				}
			});

			listenerLoaded = true;
		}

		if(getSettings().isModEnabled() && getSettings().isAutoPortl()) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/portal");
		}
	}

	public Object modifyChatMessage(Object o) {
		if (!getSettings().isModEnabled())
			return o;

		try {
			boolean hasPrefix = false;
			IChatComponent msg = (IChatComponent) o;
			IChatComponent prefix = new ChatComponentText("");

			if (msg.getUnformattedText().startsWith("[SCAMMER] ")) {
				hasPrefix = true;
				for (int i = 0; i < (msg.getSiblings().size() - 1); i++) {
					prefix.appendSibling(msg.getSiblings().get(i));
				}
				msg = msg.getSiblings().get(msg.getSiblings().size() - 1);
			}

			List<Chat> chatModules = getGG().getChatModules();
			for (Chat chatModule : chatModules) {
				if (chatModule.doActionModifyChatMessage(msg)) {
					if (chatModule.getName().equalsIgnoreCase("chatTime") && hasPrefix) {
						msg = prefix.appendSibling(msg);
					}
					msg = chatModule.modifyChatMessage(msg);
				}
			}

			return msg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
}