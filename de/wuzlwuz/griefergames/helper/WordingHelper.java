package de.wuzlwuz.griefergames.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WordingHelper {
	Properties props = new Properties();

	public WordingHelper() throws IOException {
		InputStream is = WordingHelper.class.getResourceAsStream("wordings.properties");
		props.load(is);
	}

	public String getText(String key) {
		return props.getProperty(key);
	}

	public String getText(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
}
