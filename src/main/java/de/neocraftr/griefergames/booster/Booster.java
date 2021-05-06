package de.neocraftr.griefergames.booster;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.ibm.icu.impl.duration.DurationFormatter;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.elements.ControlElement.IconData;
import org.apache.commons.lang3.time.DurationFormatUtils;

public abstract class Booster {
	private String name;
	private String type;
	private boolean highlightDuration = false;
	private boolean displayCount;
	private int count;
	private int iconIndex;
	private long nextDurationBlink = 0;
	private long endTime = 0;

	Booster(String name, String type, int count, int iconIndex, boolean displayCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.iconIndex = iconIndex;
		this.displayCount = displayCount;
	}

	Booster(String name, String type, int iconIndex, boolean displayCount) {
		this.name = name;
		this.type = type;
		this.iconIndex = iconIndex;
		this.count = 0;
		this.displayCount = displayCount;
	}

	Booster(String name, String type, int count, long endTime, int iconIndex, boolean displayCount) {
		this.name = name;
		this.type = type;
		this.count = count;
		this.iconIndex = iconIndex;
		this.endTime = endTime;
		this.displayCount = displayCount;
	}

	public String getDurationString() {
		if(this.endTime == -1) return LanguageManager.translateOrReturnKey("gg_on");

		long remainingTime = this.endTime - System.currentTimeMillis();
		long displayTime = Math.abs(remainingTime);

		String displayString;
		if(displayTime < TimeUnit.HOURS.toMillis(1)) {
			displayString = DurationFormatUtils.formatDuration(displayTime, "mm:ss");
		} else {
			displayString = DurationFormatUtils.formatDuration(displayTime, "HH:mm:ss");
		}

		return (remainingTime < 0 ? "+" : "") + displayString;
	}

	public boolean doHighlightDuration() {
		if(this.endTime == -1) return false;

		long remainingTime = this.endTime- System.currentTimeMillis();

		if(remainingTime < 0 || remainingTime > TimeUnit.SECONDS.toMillis(30)) return false;

		if (System.currentTimeMillis() > this.nextDurationBlink) {
			this.nextDurationBlink = System.currentTimeMillis() + 500L;
			this.highlightDuration = !this.highlightDuration;
		}

		return highlightDuration;
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
		if(this.count > 0) {
			this.count--;
		}
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setDisplayCount(boolean displayCount) {
		this.displayCount = displayCount;
	}

	public boolean isDisplayCount() {
		return displayCount;
	}

	public String getType() {
		return type;
	}

	public int getIconIndex() {
		return iconIndex;
	}
}
