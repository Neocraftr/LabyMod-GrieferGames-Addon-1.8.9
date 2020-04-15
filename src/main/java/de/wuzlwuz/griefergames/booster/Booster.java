package de.wuzlwuz.griefergames.booster;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.utils.Material;

public class Booster {
	private String name;
	private int count = 0;
	private List<LocalDateTime> endDates = new ArrayList<LocalDateTime>();
	private String username;
	private String type;
	private IconData icon = new IconData(Material.BARRIER);
	private int iconIndex = 0;
	private boolean showCount = true;
	private boolean resetEndDates = false;

	Booster(String name, String type, int count, LocalDateTime endDate, IconData icon, int iconIndex,
			boolean showCount) {
		setName(name);
		setType(type);
		setCount(count);
		addEndDates(endDate);
		setIcon(icon);
		setIconIndex(iconIndex);
		setShowCount(showCount);
	}

	Booster(String name, String type, int count, LocalDateTime endDate, boolean showCount) {
		setName(name);
		setType(type);
		setCount(count);
		addEndDates(endDate);
		setShowCount(showCount);
	}

	Booster(String name, String type, int count, IconData icon, int iconIndex, boolean showCount) {
		setName(name);
		setType(type);
		setCount(count);
		setIcon(icon);
		setIconIndex(iconIndex);
		setShowCount(showCount);
	}

	Booster(String name, String type, int count, boolean showCount) {
		setName(name);
		setType(type);
		setCount(count);
		setShowCount(showCount);
	}

	Booster(String name, String type, IconData icon, int iconIndex, boolean showCount) {
		setName(name);
		setType(type);
		setCount(0);
		setIcon(icon);
		setIconIndex(iconIndex);
		setShowCount(showCount);
	}

	Booster(String name, String type, boolean showCount) {
		setName(name);
		setType(type);
		setCount(0);
		setShowCount(showCount);
	}

	Booster(String name, String type, int count, LocalDateTime endDate, IconData icon, int iconIndex) {
		setName(name);
		setType(type);
		setCount(count);
		addEndDates(endDate);
		setIcon(icon);
		setIconIndex(iconIndex);
	}

	Booster(String name, String type, int count, LocalDateTime endDate) {
		setName(name);
		setType(type);
		setCount(count);
		addEndDates(endDate);
	}

	Booster(String name, String type, int count, IconData icon, int iconIndex) {
		setName(name);
		setType(type);
		setCount(count);
		setIcon(icon);
		setIconIndex(iconIndex);
	}

	Booster(String name, String type, int count) {
		setName(name);
		setType(type);
		setCount(count);
	}

	Booster(String name, String type, IconData icon, int iconIndex) {
		setName(name);
		setType(type);
		setCount(0);
		setIcon(icon);
		setIconIndex(iconIndex);
	}

	Booster(String name, String type) {
		setName(name);
		setType(type);
		setCount(0);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incrementCount() {
		this.count++;
	}

	public void decreaseCount() {
		this.count--;
	}

	public void decreaseEndDates() {
		if (endDates.size() > 0) {
			endDates.remove(0);
		}
	}

	public List<LocalDateTime> getEndDates() {
		return endDates;
	}

	public LocalDateTime getEndDate(int index) {
		return (endDates.size() > index) ? endDates.get(index) : null;
	}

	public LocalDateTime getEndDate() {
		return (endDates.size() > 0) ? endDates.get(0) : null;
	}

	public void setEndDates(List<LocalDateTime> endDate) {
		this.endDates = endDate;
	}

	public void addEndDates(LocalDateTime endDate) {
		this.endDates.add(endDate);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public IconData getIcon() {
		return icon;
	}

	private void setIcon(IconData icon) {
		this.icon = icon;
	}

	public int getIconIndex() {
		return iconIndex;
	}

	public void setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
	}

	public boolean getShowCount() {
		return showCount;
	}

	public void setShowCount(boolean showCount) {
		this.showCount = showCount;
	}

	public boolean getResetEndDates() {
		return resetEndDates;
	}

	public void setResetEndDates(boolean resetEndDates) {
		this.resetEndDates = resetEndDates;
	}

	public String getDurationString() {
		LocalDateTime curDateTime = LocalDateTime.now();
		String ret = LanguageManager.translateOrReturnKey("gg_on", new Object[0]);
		LocalDateTime endDate = getEndDate(0);

		if (endDate != null) {
			boolean additionTime = false;
			Duration duration = Duration.between(curDateTime, curDateTime);
			if (endDate.isAfter(curDateTime)) {
				duration = Duration.between(curDateTime, endDate);
			} else {
				duration = Duration.between(endDate, curDateTime);
				additionTime = true;
			}

			Integer hours = (int) duration.getSeconds() / 3600;
			Integer helper = (int) duration.getSeconds() - hours * 3600;
			Integer minutes = helper / 60;
			helper = helper - minutes * 60;
			Integer seconds = helper;

			if (hours <= 0) {
				ret = minutes.toString();
				ret += ":";
				if (seconds < 10) {
					ret += "0" + seconds.toString();
				} else {
					ret += seconds.toString();
				}
			} else {
				ret = hours.toString();
				ret += ":";
				if (minutes < 10) {
					ret += "0" + minutes.toString();
				} else {
					ret += minutes.toString();
				}
				ret += ":";
				if (seconds < 10) {
					ret += "0" + seconds.toString();
				} else {
					ret += seconds.toString();
				}
			}
			if (additionTime) {
				ret = "+" + ret;
			}
		}

		return ret;
	}
}
