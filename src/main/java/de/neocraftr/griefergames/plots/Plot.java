package de.neocraftr.griefergames.plots;

import de.neocraftr.griefergames.enums.CityBuild;

public class Plot {

    private String name;
    private String command;
    private CityBuild cityBuild;

    public Plot(String name, String command, CityBuild cityBuild) {
        this.name = name;
        this.command = command;
        this.cityBuild = cityBuild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CityBuild getCityBuild() {
        return cityBuild;
    }

    public void setCityBuild(CityBuild citybuild) {
        this.cityBuild = citybuild;
    }
}
