package de.wuzlwuz.griefergames.modules;

import de.wuzlwuz.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

public class DelayModule extends SimpleModule {
    private GrieferGames getGG() {
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
        return new ControlElement.IconData(new ResourceLocation("griefergames/textures/icons/module_delay.png"));
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
        return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames() && getGG().getTimeToWait() != 0;
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
