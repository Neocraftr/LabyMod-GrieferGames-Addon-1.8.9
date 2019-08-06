package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class GodmodeModule extends SimpleModule {
	public GodmodeModule() {
		GrieferGames.getGriefergames().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return LanguageManager.translateOrReturnKey("module_gg_godmode_displayName", new Object[0]);
	}

	@Override
	public String getDisplayValue() {
		if (GrieferGames.getGriefergames().isGodActive()) {
			return LanguageManager.translateOrReturnKey("gg_on", new Object[0]);
		} else {
			return LanguageManager.translateOrReturnKey("gg_off", new Object[0]);
		}
	}

	@Override
	public String getDefaultValue() {
		return LanguageManager.translateOrReturnKey("gg_off", new Object[0]);
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
		return LanguageManager.translateOrReturnKey("module_gg_godmode_description", new Object[0]);
	}

	@Override
	public boolean isShown() {
		return GrieferGames.getGriefergames().isShowModules();
	}

	@Override
	public ModuleCategory getCategory() {
		return GrieferGames.getGriefergames().getModuleCategory();
	}

	@Override
	public int getSortingId() {
		return 20;
	}
}