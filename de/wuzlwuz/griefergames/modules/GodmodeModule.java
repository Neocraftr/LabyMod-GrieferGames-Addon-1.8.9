package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class GodmodeModule extends SimpleModule {
	public GodmodeModule() {
		GrieferGames.getGriefergames().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return "God";
	}

	@Override
	public String getDisplayValue() {
		return (GrieferGames.getGriefergames().isGodActive()) ? "ON" : "OFF";
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
		return new ControlElement.IconData(Material.BEDROCK);
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "GG | Godmode-Module";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public int getSortingId() {
		return 500;
	}
}