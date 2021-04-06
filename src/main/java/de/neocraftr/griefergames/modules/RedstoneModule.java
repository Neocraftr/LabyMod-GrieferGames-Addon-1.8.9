package de.neocraftr.griefergames.modules;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class RedstoneModule extends SimpleModule {
	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public RedstoneModule() {
		getGG().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return LanguageManager.translateOrReturnKey("module_gg_redstone_displayName");
	}

	@Override
	public String getDisplayValue() {
		if (getGG().isRedstoneActive()) {
			return LanguageManager.translateOrReturnKey("gg_on");
		} else {
			return LanguageManager.translateOrReturnKey("gg_off");
		}
	}

	@Override
	public String getDefaultValue() {
		return LanguageManager.translateOrReturnKey("gg_off");
	}

	@Override
	public ControlElement.IconData getIconData() {
		return new ControlElement.IconData(Material.REDSTONE);
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "gg_redstone";
	}

	@Override
	public String getDescription() {
		return LanguageManager.translateOrReturnKey("module_gg_redstone_description");
	}

	@Override
	public boolean isShown() {
		return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames();
	}

	@Override
	public ModuleCategory getCategory() {
		return getGG().getModuleCategory();
	}

	@Override
	public int getSortingId() {
		return 35;
	}
}