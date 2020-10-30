package de.wuzlwuz.griefergames.chat;

import de.wuzlwuz.griefergames.chatMenu.TeamMenu;

public class VanishHelper extends Chat {
	@Override
	public String getName() {
		return "vanishHelper";
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (getHelper().showVanishModule(getGG().getPlayerRank())
				&& getHelper().vanishDefaultState(getGG().getPlayerRank())
				&& unformatted.toLowerCase().startsWith("/vanishhelper")) {
			if (getSettings().isVanishHelper()) {
				getSettings().deactivateVanishHelper();
				// getApi().displayMessageInChat(ModColor.cl("4") + "Vanish helper
				// deaktiviert!");
				TeamMenu.printMenu();
			} else {
				getSettings().activateVanishHelper();
				// getApi().displayMessageInChat(ModColor.cl("2") + "Vanish helper aktiviert!");
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
}