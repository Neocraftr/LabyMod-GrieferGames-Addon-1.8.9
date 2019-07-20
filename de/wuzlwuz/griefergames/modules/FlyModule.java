package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class FlyModule extends SimpleModule {
	public FlyModule() {
		GrieferGames.getGriefergames().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.DisplayName", "Fly");
	}

	@Override
	public String getDisplayValue() {
		if (GrieferGames.getGriefergames().isFlyActive()) {
			return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.active.true", "ON");
		} else {
			return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.active.false", "OFF");
		}
	}

	/**
	 * The value that will be shown if TestSimpleModule#isShown() returns
	 * <code>false</code>
	 * 
	 * @return the default value
	 */
	@Override
	public String getDefaultValue() {
		return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.active.false", "OFF");
	}

	@Override
	public ControlElement.IconData getIconData() {
		return new ControlElement.IconData(Material.FEATHER);
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.SettingName",
				"GG-Fly-Module");
	}

	@Override
	public String getDescription() {
		return GrieferGames.getGriefergames().getLanguageHelper().getText("modules.FlyModule.Description",
				"show fly status on/off");
	}

	@Override
	public int getSortingId() {
		return 501;
	}
}