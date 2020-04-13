package de.wuzlwuz.griefergames.chat;

import de.wuzlwuz.griefergames.chatMenu.TeamMenu;
import net.minecraft.util.IChatComponent;

public class CheckPlot extends Chat {
	private Thread thread = null;
	private boolean secondChat = false;

	private boolean isSecondChat() {
		return secondChat;
	}

	private void setSecondChat(boolean secondChat) {
		this.secondChat = secondChat;
	}

	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1500);
			} catch (Exception e) {
				getHelper().delChatToolFilter("GG-Addon-Filter-helper");
				getHelper().delChatToolFilter("GG-Addon-Filter-helper_2nd");
			}
		}
	};

	@Override
	public String getName() {
		return "checkPlot";
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank())
				&& unformatted.toLowerCase().startsWith("/checkplothelper")) {
			if (getSettings().isCheckPlotHelper()) {
				getSettings().deactivateCheckPlotHelper();
				// getApi().displayMessageInChat(ModColor.cl("4") + "Checkplot helper
				// deaktiviert!");
				TeamMenu.printMenu();
			} else {
				getSettings().activateCheckPlotHelper();
				// getApi().displayMessageInChat(ModColor.cl("2") + "Checkplot helper
				// aktiviert!");
				TeamMenu.printMenu();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean commandMessage(String unformatted) {
		return true;
	}

	@Override
	public boolean doActionModifyChatMessage(IChatComponent msg) {
		if (doActionReceiveMessage(msg.getFormattedText(), msg.getUnformattedText())) {
			// getHelper().setChatToolFilterText();
			// getHelper().setChatToolFilterRoom();
		}
		return super.doActionModifyChatMessage(msg);
	}

	@Override
	public boolean doActionReceiveMessage(String formatted, String unformatted) {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank()) && getSettings().isCheckPlotHelper()) {
			String propFormatted = getHelper().getProperTextFormat(formatted.trim()).trim();

			if (unformatted.startsWith("===== CheckPlot-Anfragen")) {// check headline
				setSecondChat(false);
				return true;
			} else if (unformatted.startsWith("= Seite") && propFormatted.indexOf("§6§l= Seite §r§e§l") >= 0) {// check
																												// curpage
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§7<-§r §6|§r §r§6->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§6->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.equalsIgnoreCase("§6Seite §r§6<-§r §6|§r §r§7->§r")) {// check paging
				setSecondChat(false);
				return true;
			} else if (propFormatted.indexOf("§r§r§r §r§6[§eCheck§6]§r§r§r") >= 0) {// check entry
				setSecondChat(false);
				return true;
			} else if (propFormatted.indexOf("§6[§causw\u00E4hlen§6]§r") >= 0) {// check refuse entry
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§r§cDu stehst nicht auf diesem Grundst\u00FCck!§r") >= 0) {// check not on
																											// plot
				setSecondChat(true);
				return true;
			} else if (propFormatted
					.indexOf("§r§cGrundst\u00FCcke dieses Spielers k\u00F6nnen nicht gel\u00F6scht werden.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§7Zuletzt online: §r§c") >= 0 && propFormatted.indexOf("§r§7Uhr") >= 0) {// check
																														// last
																														// seen
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§e[§r§2ANALYSE§r§e]§r Bewertet als") >= 0) {// check plot data
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§7Das Grundst\u00FCck ist ab dem §r§a") >= 0
					&& (propFormatted.indexOf("§r§4UNBEBAUT §r§7ist!§r") >= 0
							|| propFormatted.indexOf("§r§2BEBAUT §r§7ist!§r") >= 0)) {// check plot date
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf(
					"§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf(
					"§7Bitte klicke auf den gew\u00FCnschten Ablehnungsgrund oder nutze §r§e/checkplot deny <RequestID> <Grund>§r§7.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDu musst dich auf einem Grundst\u00FCck befinden!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§aDer Antrag wurde angenommen.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted
					.indexOf("§cDu hast die Anfrage f\u00FCr das Grundst\u00FCck abgelehnt. Grund:") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDer Spieler ist aktiv auf GrieferGames.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§6§lInformationen: §r§f§nhttp://plots.griefergames.net/§r§f§n §r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§6ID") >= 0 && (propFormatted.indexOf("§r§eoffen §r") >= 0
					|| propFormatted.indexOf("§r§4§labgelehnt: §r§4") >= 0)) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDu bist zu weit vom Grundst\u00FCck entfernt!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDas Grundst\u00FCck befindet sich zu nah am Spawn!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted
					.indexOf("§cDu darfst den Befehl nicht so oft hintereinander ausf\u00FChren!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDer Spieler ist unbekannt.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cMerge-Grundst\u00FCcke k\u00F6nnen nicht beantragt werden.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf(
					"§aDu kannst das Grundst\u00FCck jetzt l\u00F6schen. Bitte best\u00E4tige mit §r§2/checkplot confirm§r§a.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§aDas Grundst\u00FCck mit der ID") >= 0
					&& propFormatted.indexOf("§r§awurde gel\u00F6scht!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDieses Grundst\u00FCck hat keinen Besitzer!§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§cDer Antrag wurde nicht gefunden.§r") >= 0) {
				setSecondChat(true);
				return true;
			} else if (propFormatted.indexOf("§6Das Grundst\u00FCck wird gepr\u00FCft!") >= 0) {
				setSecondChat(true);
				return true;
			}
		}

		return super.doActionReceiveMessage(formatted, unformatted);
	}

	@Override
	public boolean receiveMessage(String formatted, String unformatted) {
		String name = (isSecondChat()) ? "GG-Addon-Filter-helper_2nd" : "GG-Addon-Filter-helper";

		String cleanText = formatted.replaceAll("\u00A7[0-9a-frl]", "").trim();

		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			thread = new Thread(runnable);
			thread.start();
		}

		getHelper().addChatToolFilterText(name, cleanText);
		getHelper().setChatToolFilterRoom(name, "CheckPlot");
		getHelper().setChatToolFilterSecondChat(name, isSecondChat());
		return super.receiveMessage(formatted, unformatted);
	}
}