package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class VanishModule extends SimpleModule {
	public VanishModule() {
		GrieferGames.getGriefergames().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return "Vanish";
	}

	@Override
	public String getDisplayValue() {
		return (GrieferGames.getGriefergames().isVanishActive()) ? "ON" : "OFF";
	}

	/**
	 * The value that will be shown if TestSimpleModule#isShown() returns
	 * <code>false</code>
	 * 
	 * @return the default value
	 */
	@Override
	public String getDefaultValue() {
		return "OFF";
	}

	@Override
	public ControlElement.IconData getIconData() {
		return new ControlElement.IconData(Material.BARRIER);
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "GG | Vanish-Module";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public int getSortingId() {
		return 502;
	}
}