package de.neocraftr.griefergames.modules;

import de.neocraftr.griefergames.GrieferGames;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement;
import net.minecraft.util.ResourceLocation;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class ClearLagModule extends SimpleModule {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

    private GrieferGames getGG() {
        return GrieferGames.getGriefergames();
    }

    public ClearLagModule() {
        getGG().getApi().registerModule(this);
    }

    @Override
    public String getDisplayName() {
        return LanguageManager.translateOrReturnKey("module_gg_clearlag_displayName");
    }

    @Override
    public String getDisplayValue() {
        long remainingTime = getGG().getClearLagTime() - System.currentTimeMillis();
        if(remainingTime > 60000) {
            return timeFormat.format(remainingTime);
        } else {
            return TimeUnit.MILLISECONDS.toSeconds(remainingTime) + "s";
        }
    }

    @Override
    public String getDefaultValue() {
        return "?";
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(new ResourceLocation("labymod/textures/misc/blocked.png"));
    }

    @Override
    public void loadSettings() {}

    @Override
    public String getSettingName() {
        return "gg_clearlag";
    }

    @Override
    public String getDescription() {
        return LanguageManager.translateOrReturnKey("module_gg_clearlag_description");
    }

    @Override
    public boolean isShown() {
        return getGG().getSettings().isModEnabled() && getGG().isOnGrieferGames() && getGG().getClearLagTime() > System.currentTimeMillis();
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
