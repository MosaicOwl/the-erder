package com.alastar.game.lang;

public class LanguageManager {

	public static Language[] langs = new Language[] {};

	public static void relaunchLanguageManager(int langsAmt) {
		langs = new Language[langsAmt];
	}

	public static void addLang(Language l) {
		for (int i = 0; i < langs.length; ++i) {
			if (langs[i] == null) {
				langs[i] = l;
				break;
			}

		}
	}

	public static Language getLang(String lN) {
		// System.out.println("get lang " + lN);

		try {
			for (int i = 0; i < langs.length; ++i) {
				if (langs[i] != null) {
					// System.out.println("Language exist. Name: " +
					// langs[i].langName);
					if (langs[i].langName.equals(lN)) {
						// System.out.println("Language name: " + lN);
						return langs[i];
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public static void dispose() {
		for (int i = 0; i < langs.length; ++i) {
			langs[i].font.dispose();
		}
	}

}
