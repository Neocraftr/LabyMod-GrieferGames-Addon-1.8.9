package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class DelayModule extends SimpleModule {
    protected GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }

    public DelayModule() {
        getGG().getApi().registerModule(this);
    }

    @Override
    public String getDisplayName() {
        return LanguageManager.translateOrReturnKey("module_gg_delay_displayName");
    }

    @Override
    public String getDisplayValue() {
        return getGG().getTimeToWait()+"s";
    }

    @Override
    public String getDefaultValue() {
        return "?";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.WATCH);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "gg_delay";
    }

    @Override
    public String getDescription() {
        return LanguageManager.translateOrReturnKey("module_gg_delay_description");
    }

    @Override
    public boolean isShown() {
        return getGG().isShowModules() && getGG().getTimeToWait() != 0;
    }

    @Override
    public ModuleCategory getCategory() {
        return getGG().getModuleCategory();
    }

    @Override
    public int getSortingId() {
        return 60;
    }
}
