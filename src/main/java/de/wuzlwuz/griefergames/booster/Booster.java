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
	private String type;
	private IconData icon = new IconData(Material.BARRIER);
	private int iconIndex = 0;
	private boolean showCount = true;
	private boolean resetEndDates = false;
	private boolean highlightDuration = false;
	private long nextDurationBlink = 0;

	Booster(String name, String type, int count, LocalDateTime endDate, IconData icon, int iconIndex,
			boolean showCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		endDates.add(endDate);
		this.icon = icon;
		this.iconIndex = iconIndex;
		this.showCount = showCount;
	}

	Booster(String name, String type, int count, LocalDateTime endDate, boolean showCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		endDates.add(endDate);
		this.showCount = showCount;
	}

	Booster(String name, String type, int count, IconData icon, int iconIndex, boolean showCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.icon = icon;
		this.iconIndex = iconIndex;
		this.showCount = showCount;
	}

	Booster(String name, String type, int count, boolean showCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.showCount = showCount;
	}

	Booster(String name, String type, IconData icon, int iconIndex, boolean showCount) {
		this.name = name;
		this.type = type;
		setCount(0);
		this.icon = icon;
		this.iconIndex = iconIndex;
		this.showCount = showCount;
	}

	Booster(String name, String type, boolean showCount) {
		this.name = name;
		this.type = type;
		setCount(0);
		this.showCount = showCount;
	}

	Booster(String name, String type, int count, LocalDateTime endDate, IconData icon, int iconIndex) {
		this.name = name;
		this.type = type;
		this.count = count;
		endDates.add(endDate);
		this.icon = icon;
		this.iconIndex = iconIndex;
	}

	Booster(String name, String type, int count, LocalDateTime endDate) {
		this.name = name;
		this.type = type;
		this.count = count;
		endDates.add(endDate);
	}

	Booster(String name, String type, int count, IconData icon, int iconIndex) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.icon = icon;
		this.iconIndex = iconIndex;
	}

	Booster(String name, String type, int count) {
		this.name = name;
		this.type = type;
		this.count = count;
	}

	Booster(String name, String type, IconData icon, int iconIndex) {
		this.name = name;
		this.type = type;
		setCount(0);
		this.icon = icon;
		this.iconIndex = iconIndex;
	}

	Booster(String name, String type) {
		this.name = name;
		this.type = type;
		setCount(0);
	}

	public String getName() {
		return name;
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

	public String getType() {
		return type;
	}

	public int getIconIndex() {
		return iconIndex;
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
		String ret = LanguageManager.translateOrReturnKey("gg_on");
		LocalDateTime endDate = getEndDate(0);

		if (endDate != null) {
			boolean additionTime = false;
			Duration duration = null;
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

	public boolean doHighlightDuration() {
		LocalDateTime curDateTime = LocalDateTime.now();
		LocalDateTime endDate = getEndDate(0);

		if(endDate == null) return false;

		Duration duration = null;
		if (endDate.isAfter(curDateTime)) {
			duration = Duration.between(curDateTime, endDate);
		} else return false;

		if(duration.getSeconds() > 30) return false;

		if (System.currentTimeMillis() > nextDurationBlink) {
			nextDurationBlink = System.currentTimeMillis() + 500L;
			highlightDuration = !highlightDuration;
		}

		return highlightDuration;
	}
}
