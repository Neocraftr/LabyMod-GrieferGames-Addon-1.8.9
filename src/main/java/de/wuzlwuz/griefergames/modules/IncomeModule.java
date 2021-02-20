package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;

public class IncomeModule extends SimpleModule {
    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }

    public IncomeModule() {
        getGG().getApi().registerModule(this);
    }

    @Override
    public String getDisplayName() {
        return LanguageManager.translateOrReturnKey("module_gg_income_displayName");
    }

    @Override
    public String getDisplayValue() {
        if(getGG().getIncome() >= 0) {
            return "$"+getGG().getIncome();
        } else {
            return "§c$"+getGG().getIncome();
        }
    }

    @Override
    public String getDefaultValue() {
        return "$0";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData("labymod/textures/misc/economy_cash.png");
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "gg_income";
    }

    @Override
    public String getDescription() {
        return LanguageManager.translateOrReturnKey("module_gg_income_description");
    }

    @Override
    public boolean isShown() {
        return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames() && getGG().getIncome() != 0;
    }

    @Override
    public ModuleCategory getCategory() {
        return getGG().getModuleCategory();
    }

    @Override
    public int getSortingId() {
        return 70;
    }
}
