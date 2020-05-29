package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class FlyModule extends SimpleModule {
	protected GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public FlyModule() {
		getGG().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return LanguageManager.translateOrReturnKey("module_gg_fly_displayName");
	}

	@Override
	public String getDisplayValue() {
		if (getGG().isFlyActive()) {
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
		return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/booster_fliegen.png"));
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "gg_fly";
	}

	@Override
	public String getDescription() {
		return LanguageManager.translateOrReturnKey("module_gg_fly_description");
	}

	@Override
	public boolean isShown() {
		return getGG().isShowModules();
	}

	@Override
	public ModuleCategory getCategory() {
		return getGG().getModuleCategory();
	}

	@Override
	public int getSortingId() {
		return 40;
	}
}