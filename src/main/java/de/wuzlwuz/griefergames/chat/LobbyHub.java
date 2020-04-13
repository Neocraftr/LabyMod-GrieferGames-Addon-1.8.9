package de.wuzlwuz.griefergames.chat;

import net.minecraft.client.Minecraft;

public class LobbyHub extends Chat {
	@Override
	public String getName() {
		return "lobbyHub";
	}

	@Override
	public boolean doActionCommandMessage(String unformatted) {
		if (getSettings().isClearMapCache() && (unformatted.toLowerCase().startsWith("/lobby")
				|| unformatted.toLowerCase().startsWith("/hub") || unformatted.toLowerCase().startsWith("/server"))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean commandMessage(String unformatted) {
		Minecraft.getMinecraft().entityRenderer.getMapItemRenderer().clearLoadedMaps();
		return false;
	}
}