package de.wuzlwuz.griefergames.chatMenu;

import de.wuzlwuz.griefergames.GrieferGames;
import de.wuzlwuz.griefergames.helper.Helper;
import de.wuzlwuz.griefergames.settings.ModSettings;
import net.labymod.api.LabyModAPI;

public class TeamMenu {
	protected static GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	protected static LabyModAPI getApi() {
		return getGG().getApi();
	}

	protected static Helper getHelper() {
		return GrieferGames.getGriefergames().getHelper();
	}

	protected static ModSettings getSettings() {
		return GrieferGames.getSettings();
	}

	public static String getName() {
		return "teamMenu";
	}

	public static void printMenu() {
		if (getHelper().doCheckPlotRoom(getGG().getPlayerRank())
				|| getHelper().vanishDefaultState(getGG().getPlayerRank())) {
			String menuHeadFoot = "\u00A7r\u00A7f\u00A7m------------\u00A7r\u00A78 [ \u00A7r\u00A76TeamMenu\u00A7r\u00A78 ] \u00A7r\u00A7f\u00A7m------------\u00A7r";

			String vanishHelperStatus = getToggleText(getSettings().isVanishHelper());
			String checkPlotHelperStatus = getToggleText(getSettings().isCheckPlotHelper());

			getApi().displayMessageInChat(menuHeadFoot);
			if (getHelper().vanishDefaultState(getGG().getPlayerRank())) {
				getApi().displayMessageInChat(
						"\u00A7r\u00A76Vanish Helper: " + vanishHelperStatus + " \u00A77/vanishhelper\u00A7r");
			}
			if (getHelper().doCheckPlotRoom(getGG().getPlayerRank())) {
				getApi().displayMessageInChat(
						"\u00A7r\u00A76CheckPlot Helper: " + checkPlotHelperStatus + " \u00A77/checkplothelper\u00A7r");
			}
			getApi().displayMessageInChat(menuHeadFoot);
		}
	}

	private static String getToggleText(boolean toggleVal) {
		return (toggleVal) ? "\u00A7r\u00A72aktiviert" : "\u00A7r\u00A74deaktiviert";
	}
}
