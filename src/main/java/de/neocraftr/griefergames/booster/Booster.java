package de.neocraftr.griefergames.booster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.labymod.main.lang.LanguageManager;
import org.apache.commons.lang3.time.DurationFormatUtils;

public abstract class Booster {
	private String name;
	private String type;
	private boolean highlightDuration = false;
	private boolean stackable;
	private int count;
	private int iconIndex;
	private long nextDurationBlink = 0;
	private List<Long> endTimes = new ArrayList<>();

	Booster(String name, String type, int iconIndex, boolean stackable) {
		this.name = name;
		this.type = type;
		this.iconIndex = iconIndex;
		this.count = 0;
		this.stackable = stackable;
	}

	public String getDurationString() {
		if(this.endTimes.size() == 0) return LanguageManager.translateOrReturnKey("gg_on");

		long remainingTime = this.endTimes.get(0) - System.currentTimeMillis();
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
		if(this.endTimes.size() == 0) return false;

		long remainingTime = this.endTimes.get(0) - System.currentTimeMillis();

		if(remainingTime < 0 || remainingTime > TimeUnit.SECONDS.toMillis(10)) return false;

		if (System.currentTimeMillis() > this.nextDurationBlink) {
			this.nextDurationBlink = System.currentTimeMillis() + 500L;
			this.highlightDuration = !this.highlightDuration;
		}

		return highlightDuration;
	}

	public void addBooster(long duration) {
		this.count++;
		if(this.stackable) {
			endTimes.add(System.currentTimeMillis() + duration);
		} else {
			if(endTimes.size() == 0) {
				endTimes.add(System.currentTimeMillis() + duration);
			} else if(endTimes.get(0) < System.currentTimeMillis()) {
				endTimes.set(0, System.currentTimeMillis() + duration);
			} else {
				endTimes.set(0, endTimes.get(0) + duration);
			}
		}
	}

	public void setBooster(int count, List<Long> durations) {
		this.count = count;
		this.endTimes.clear();
		if(this.stackable) {
			for(Long duration : durations) {
				this.endTimes.add(System.currentTimeMillis() + duration);
			}
		} else {
			this.endTimes.add(System.currentTimeMillis() + durations.get(0));
		}
	}

	public void removeBooster() {
		if(this.count <= 0) return;
		this.count--;
		if(this.stackable) {
			endTimes.remove(0);
		}
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

	public List<Long> getEndTimes() {
		return endTimes;
	}

	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public boolean isStackable() {
		return stackable;
	}

	public String getType() {
		return type;
	}

	public int getIconIndex() {
		return iconIndex;
	}
}
