package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class NicknameModule extends SimpleModule {
	private GrieferGames getGG() {
		return GrieferGames.getGriefergames();
	}

	public NicknameModule() {
		getGG().getApi().registerModule(this);
	}

	@Override
	public String getDisplayName() {
		return LanguageManager.translateOrReturnKey("module_gg_nickname_displayName");
	}

	@Override
	public String getDisplayValue() {
		if (getGG().isNicknameActive()) {
			return getGG().getNickname();
		} else {
			return LanguageManager.translateOrReturnKey("module_gg_nickname_off");
		}
	}

	@Override
	public String getDefaultValue() {
		return LanguageManager.translateOrReturnKey("gg_nick_off");
	}

	@Override
	public ControlElement.IconData getIconData() {
		return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/module_nickname.png"));
	}

	@Override
	public void loadSettings() {

	}

	@Override
	public String getSettingName() {
		return "gg_nickname";
	}

	@Override
	public String getDescription() {
		return LanguageManager.translateOrReturnKey("module_gg_nickname_description");
	}

	@Override
	public boolean isShown() {
		return getGG().getSettings().isModEnabled() && getGG().isShowModules();
	}

	@Override
	public ModuleCategory getCategory() {
		return getGG().getModuleCategory();
	}

	@Override
	public int getSortingId() {
		return 50;
	}
}