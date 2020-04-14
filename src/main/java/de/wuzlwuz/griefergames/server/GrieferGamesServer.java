package de.wuzlwuz.griefergames.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.booster.Booster;
import de.wuzlwuz.griefergames.chat.Chat;
import de.wuzlwuz.griefergames.gui.vanishHelperGui;
import de.wuzlwuz.griefergames.helper.Helper;
import de.wuzlwuz.griefergames.listener.OnTickListener;
import de.wuzlwuz.griefergames.listener.PreRenderListener;
import de.wuzlwuz.griefergames.listener.SubServerListener;
import de.wuzlwuz.griefergames.modules.AuraModule;
import de.wuzlwuz.griefergames.modules.BoosterModule;
import de.wuzlwuz.griefergames.modules.FlyModule;
import de.wuzlwuz.griefergames.modules.GodmodeModule;
import de.wuzlwuz.griefergames.modules.NicknameModule;
import de.wuzlwuz.griefergames.modules.VanishModule;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAPI;
import net.labymod.api.events.MessageModifyChatEvent;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrieferGamesServer extends Server {
	private List<SubServerListener> subServerListener = new ArrayList<SubServerListener>();
	private Minecraft mc;
	private LabyModAPI api;
	private String subServer = "";
	private long nextLastMessageRequest = System.currentTimeMillis() + 1000L;
	private long nextScoreboardRequest = System.currentTimeMillis() + (-1 * 1000L);
	private long nextCheckFly = System.currentTimeMillis() + 1000L;
	private String lastMessage = "";
	private String playerRank = "Spieler";
	private boolean modulesLoaded = false;
	private boolean listenerLoaded = false;

	protected GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	protected ModSettings getSettings() {
		return GrieferGames.getSettings();
	}

	protected Helper getHelper() {
		return GrieferGames.getGriefergames().getHelper();
	}

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
		if (!subServerListener.contains(ssl)) {
			subServerListener.add(ssl);
		}
	}

	public List<SubServerListener> getSubServerListener() {
		return subServerListener;
	}

	private void setApi(LabyModAPI api) {
		this.api = api;
	}

	public String getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(String playerRank) {
		GrieferGames.getGriefergames().setPlayerRank(playerRank);
		this.playerRank = playerRank;
	}

	private void setModulesLoaded(boolean modulesLoaded) {
		this.modulesLoaded = modulesLoaded;
	}

	public boolean getModulesLoaded() {
		return this.modulesLoaded;
	}

	private void setListenerLoaded(boolean listenerLoaded) {
		this.listenerLoaded = listenerLoaded;
	}

	public boolean getListenerLoaded() {
		return this.listenerLoaded;
	}

	public GrieferGamesServer(Minecraft minecraft) {
		super("GrieferGames", GrieferGames.getGriefergames().getServerIp());
		setMc(minecraft);
		setApi(getGG().getApi());
		addSubServerListener(new SubServerListener() {
			@Override
			public void onSubServerChanged(String subServerNameOld, String subServerName) {
				getGG().setNickname("");

				if (getHelper().doResetBoosterBySubserver(subServerName)) {
					getGG().setBoosters(new ArrayList<Booster>());
				} else {
					if (getSettings().isVanishHelper() && getHelper().showVanishModule(getPlayerRank())) {
						mc.displayGuiScreen(new vanishHelperGui());
					}
				}
				if (subServerName.equalsIgnoreCase("lobby")) {
					getGG().setShowBoosterDummy(true);
					loadPlayerRank();
				} else {
					// Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().clearLoadedMaps();
					getGG().setShowBoosterDummy(false);
				}

				getGG().setGodActive(false);
				getGG().setVanishActive(getHelper().vanishDefaultState(getPlayerRank()));
			}
		});
	}

	public String getSubServer() {
		return subServer;
	}

	public void setSubServer(String subServer) {
		this.subServer = subServer.trim();
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

	private boolean loadPlayerRank() {
		String accountName = LabyModCore.getMinecraft().getPlayer().getName().trim();

		try {
			NetHandlerPlayClient nethandlerplayclient = LabyModCore.getMinecraft().getPlayer().sendQueue;
			Collection<NetworkPlayerInfo> playerMap = nethandlerplayclient.getPlayerInfoMap();

			for (NetworkPlayerInfo player : playerMap) {
				IChatComponent tabListName = player.getDisplayName();
				String teamName = player.getPlayerTeam().getTeamName();
				String registeredName = player.getGameProfile().getName();

				if (tabListName != null) {
					if (accountName.length() > 0 && accountName
							.equalsIgnoreCase(getHelper().getPlayerName(tabListName.getUnformattedText()).trim())) {

						setPlayerRank(getHelper().getPlayerRank(tabListName.getUnformattedText().trim()));
						getGG().setIsInTeam(getHelper().isInTeam(getPlayerRank()));
					}
				} else if (accountName.length() > 0 && registeredName.length() > 0 && teamName.length() > 0
						&& accountName.trim().equalsIgnoreCase(registeredName.trim())) {
					setPlayerRank(getHelper().getPlayerRank(teamName.trim()));
					getGG().setIsInTeam(getHelper().isInTeam(getPlayerRank()));
				}
			}
			if (!getModulesLoaded()) {
				setModulesLoaded(true);

				ModuleCategoryRegistry.loadCategory(getGG().getModuleCategory());

				new BoosterModule();
				new FlyModule();
				new NicknameModule();
				if (getHelper().showGodModule(getPlayerRank())) {
					new GodmodeModule();
				}
				if (getHelper().showAuraModule(getPlayerRank())) {
					new AuraModule();
				}
				if (getHelper().showVanishModule(getPlayerRank())) {
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
				getHelper().checkCurrentBoosters(unformatted, formatted);

				if (getSettings().isUpdateBoosterState() && getHelper().isSwitcherDoneMsg(unformatted, formatted) > 0) {
					getGG().setBoosters(new ArrayList<Booster>());
					getMc().thePlayer.sendChatMessage("/booster");
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
		setSubServer("");
		getGG().setNickname("");

		if (!getListenerLoaded()) {
			this.getApi().getEventManager().register(new MessageModifyChatEvent() {

				@Override
				@SubscribeEvent(priority = EventPriority.HIGH)
				public Object onModifyChatMessage(Object o) {
					return modifyChatMessage(o);
				}

			});

			this.getApi().registerForgeListener(new PreRenderListener(this));
			this.getApi().registerForgeListener(new OnTickListener(this));

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

					List<Chat> chatModules = getGG().getChatModules();
					for (Chat chatModule : chatModules) {
						if (chatModule.doActionReceiveMessage(formatted, unformatted)) {
							return chatModule.receiveMessage(formatted, unformatted);
						}
					}

					return false;
				}
			});

			setListenerLoaded(true);
		}
	}

	public Object modifyChatMessage(Object o) {
		if (!getSettings().isModEnabled())
			return o;

		try {
			IChatComponent msg = (IChatComponent) o;

			List<Chat> chatModules = getGG().getChatModules();
			for (Chat chatModule : chatModules) {
				if (chatModule.doActionModifyChatMessage(msg)) {
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