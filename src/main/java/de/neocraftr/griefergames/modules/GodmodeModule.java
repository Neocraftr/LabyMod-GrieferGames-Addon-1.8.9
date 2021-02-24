package de.neocraftr.griefergames.modules;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class GodmodeModule extends SimpleModule {
	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public GodmodeModule() {
		getGG().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return LanguageManager.translateOrReturnKey("module_gg_godmode_displayName");
	}

	@Override
	public String getDisplayValue() {
		if (getGG().isGodActive()) {
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
		return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/module_god.png"));
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "gg_godmode";
	}

	@Override
	public String getDescription() {
		return LanguageManager.translateOrReturnKey("module_gg_godmode_description");
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
		return 20;
	}
}