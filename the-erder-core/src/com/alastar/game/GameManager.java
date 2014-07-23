package com.alastar.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.alastar.game.enums.EntityType;
import com.alastar.game.enums.ItemType;
import com.alastar.game.enums.TileType;
import com.alastar.game.lang.Entry;
import com.alastar.game.lang.EntryManager;
import com.alastar.game.lang.Language;
import com.alastar.game.lang.LanguageManager;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

public class GameManager {

	public static int fontSize = 10;
	public static Texture grass;
	public static Texture stone;
	public static Texture swamp;
	public static Texture lava;
	public static Texture water;
	public static Texture brick;

	public static Texture elf;
	public static Texture human;
	public static Texture shaolin;
	public static Texture orc;
	public static Texture zombie;
	public static Texture skeleton;
	public static Texture wolf;

	//public static Texture background;

	public static Skin skin;

	public static TextButtonStyle txtBtnStyle;
	public static LabelStyle labelStyle;
	public static TextFieldStyle txtFieldStyle;
    public static WindowStyle windowStyle;
    public static SelectBoxStyle boxStyle;
    public static ScrollPaneStyle scrollStyle;
    public static ListStyle listStyle;
    public static WindowStyle overheadWindowStyle;

	public static int textureResolution;

	public static String lang = "en.txt";
	public static int fieldOfTransparency = 3;
    public static Hashtable<String, Skin> skins;
    public static String selectedSkin = "default";


	public static void LoadContent() {

		System.out.println("Loading languages...");
		LoadLanguage();
		////////////
		//TEXTURES//
		////////////
		grass = new Texture(Gdx.files.internal("textures/tiles/grass.png"));
		water = new Texture(Gdx.files.internal("textures/tiles/Water.png"));
		stone = new Texture(Gdx.files.internal("textures/tiles/Stone.png"));
		lava = new Texture(Gdx.files.internal("textures/tiles/Lava.png"));
		swamp = new Texture(Gdx.files.internal("textures/tiles/Swamp.png"));
		brick = new Texture(Gdx.files.internal("textures/tiles/bricks.png"));

		shaolin = new Texture(
				Gdx.files.internal("textures/entities/shaolin.png"));
		human = new Texture(Gdx.files.internal("textures/entities/human.png"));
		orc = new Texture(Gdx.files.internal("textures/entities/orc.png"));
		elf = new Texture(Gdx.files.internal("textures/entities/elf.png"));
		wolf = new Texture(Gdx.files.internal("textures/entities/human.png"));
		skeleton = new Texture(
				Gdx.files.internal("textures/entities/human.png"));
		zombie = new Texture(Gdx.files.internal("textures/entities/human.png"));
		textureResolution = grass.getWidth();
		
		//background = new Texture(
		//		Gdx.files.internal("textures/gui/TheErderBackground.png"));

		////////////////
		//MUSIC/SOUNDS//
		////////////////
		//dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		//rainMusic.setLooping(true);

		/////////
		//SKINS//
		/////////
        skins = new Hashtable<String, Skin>();
		Skin skin = new Skin();
		skin.add("lbutton", new Texture("textures/gui/lbutton.jpg"));
		skin.add("lwindow", new Texture("textures/gui/lwindow.png"));
		skin.add("ltextBox", new Texture("textures/gui/ltextBox.jpg"));

		txtBtnStyle = new TextButtonStyle();
		txtBtnStyle.font = getLocaleFont();
		txtBtnStyle.up = skin.getDrawable("lbutton");
		txtBtnStyle.down = skin.getDrawable("lbutton");
        skin.add("button", txtBtnStyle, TextButtonStyle.class);

		labelStyle = new LabelStyle();
		labelStyle.font = getLocaleFont();
        skin.add("label", labelStyle, LabelStyle.class);

		txtFieldStyle = new TextFieldStyle();
		txtFieldStyle.font = getLocaleFont();
		txtFieldStyle.fontColor = new Color(1, 1, 1, 1);
		txtFieldStyle.background = skin.getDrawable("ltextBox");
        skin.add("textField", txtFieldStyle, TextFieldStyle.class);
        
        windowStyle = new WindowStyle();
        windowStyle.background = skin.getDrawable("lwindow");
        windowStyle.titleFont = getLocaleFont();
        skin.add("window", windowStyle, WindowStyle.class);
        
        overheadWindowStyle = new WindowStyle();
        overheadWindowStyle.titleFont = getLocaleFont();
        skin.add("overhead_window", overheadWindowStyle, WindowStyle.class);
        
        scrollStyle = new ScrollPaneStyle();
        scrollStyle.background = skin.getDrawable("lwindow");
        scrollStyle.vScroll = skin.getDrawable("lbutton");
        scrollStyle.vScrollKnob = skin.getDrawable("lbutton");
        scrollStyle.corner  = skin.getDrawable("ltextBox");
        skin.add("scroll", scrollStyle, ScrollPaneStyle.class);

        listStyle = new ListStyle();
        listStyle.background = skin.getDrawable("lwindow");
        listStyle.font = getLocaleFont();
        listStyle.selection = skin.getDrawable("ltextBox");
        listStyle.fontColorSelected = new Color(255,255,0,1);
        listStyle.fontColorUnselected = new Color(1,1,1,1);
        skin.add("list", listStyle, ListStyle.class);

        boxStyle = new SelectBoxStyle();
        boxStyle.background = skin.getDrawable("ltextBox");
        boxStyle.font = getLocaleFont();
        boxStyle.backgroundOpen= skin.getDrawable("ltextBox");
        boxStyle.backgroundDisabled = skin.getDrawable("ltextBox");
        boxStyle.backgroundOver = skin.getDrawable("ltextBox");
        boxStyle.scrollStyle = scrollStyle;
        boxStyle.listStyle = listStyle;
        skin.add("box", boxStyle, SelectBoxStyle.class);
        
		skins.put("default", skin);
		System.out.println("gameManager load content done!");

	}

	@SuppressWarnings("deprecation")
	private static void LoadLanguage() {
		try {
			FileHandle[] files = getLocaleDir();
			FileHandle[] fontTxtFiles = getFontTxtDir();
			FileHandle[] fontFiles = getFontDataDir();

			System.out.println("files amt: " + files.length);
			
			LanguageManager.relaunchLanguageManager(1);

			Language l;
			EntryManager eM;

			File fontTxtFile = null;
			FileHandle fontFile = null;
			File langFile;
			FileReader fr;
			BufferedReader br;
			BitmapFont nFont;

			String s = "";
			String allowedCharacters = "";
			String line = "";
			String fontName = "";

			final ArrayList<String> lines = new ArrayList<String>();
			System.out.println("txt fonts files: " + fontTxtFiles.length);

			for (int i = 0; i < files.length; ++i) {

				langFile = files[i].file();
				System.out.println("file name: " + langFile.getName());

				if (langFile.getName().equals(lang)) {
					for (int f = 0; f < fontTxtFiles.length; ++f) {
						System.out.println(fontTxtFiles[f].name());
						if (fontTxtFiles[f].name().equals(langFile.getName())) {
							fontTxtFile = fontTxtFiles[f].file();
							break;
						}
					}
					 System.out.println("file name -  " + langFile.getName());

					fr = new FileReader(langFile);
					br = new BufferedReader(fr);

					while ((s = br.readLine()) != null) {
						lines.add(s);
					}

					br.close();

					fr.close();

					fr = new FileReader(fontTxtFile);
					br = new BufferedReader(fr);
					allowedCharacters = br.readLine();
					fontName = br.readLine();

					br.close();
					fr.close();

					for (int f = 0; f < fontFiles.length; ++f) {
						if (fontFiles[f].name().equals(fontName)) {
							fontFile = fontFiles[f];
							break;
						}
					}

					// System.out.println("Allowed chars for " +
					// langFile.getName()
					// + " are " + allowedCharacters);
					eM = new EntryManager(lines.size());

					// /////////////////
					// LOADING ENTRIES//
					// /////////////////
					// System.out.println(lines.size());
					for (int j = 0; j < lines.size(); ++j) {
						line = lines.get(j);
						// System.out.println(line + " j = " + j);
						eM.addEntry(new Entry(line.split("=")[0], line
								.split("=")[1]));
					}
					FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
							fontFile);
					nFont = generator.generateFont(fontSize, allowedCharacters,
							false);
					l = new Language(langFile.getName(), eM, nFont);
					LanguageManager.addLang(l);
					System.out.println(l.langName + " added! ");
					lines.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BitmapFont getLocaleFont() {
		return LanguageManager.getLang(lang).getFont();
	}

	public static FileHandle[] getFontDataDir() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			return Gdx.files.absolute(
					System.getProperty("user.dir") + "\\bin\\data\\fonts\\")
					.list();
		} else if (Gdx.app.getType() == ApplicationType.Android) {
			return Gdx.files.internal("data/fonts/").list();
		}
		return null;
	}

	public static FileHandle[] getWorldsDataDir() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			return Gdx.files.absolute(
					System.getProperty("user.dir") + "\\bin\\data\\worlds\\")
					.list();

		} else if (Gdx.app.getType() == ApplicationType.Android) {
			return Gdx.files.internal("data/worlds/").list();
		}
		return null;
	}

	public static String getWorldsDataPath() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			return System.getProperty("user.dir") + "\\data\\worlds\\";
		} else if (Gdx.app.getType() == ApplicationType.Android) {
			return "data/worlds/";
		}
		return lang;
	}

	public static FileHandle[] getFontTxtDir() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			System.out.println(System.getProperty("user.dir")
					+ "\\bin\\fonts\\");

			return Gdx.files.absolute(
					System.getProperty("user.dir") + "\\bin\\fonts\\").list();

		} else if (Gdx.app.getType() == ApplicationType.Android) {
			return Gdx.files.internal("fonts/").list();
		}
		return null;
	}

	public static FileHandle[] getLocaleDir() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			System.out.println(System.getProperty("user.dir")
					+ "\\bin\\languages\\");
			return Gdx.files.absolute(
					System.getProperty("user.dir") + "\\bin\\languages").list();

		} else if (Gdx.app.getType() == ApplicationType.Android) {
			return Gdx.files.internal("languages/").list();
		}
		return null;
	}

	public static Texture getTexture(TileType t) {
		switch (t) {
		case Grass:
			return grass;
		case Brick:
			return brick;
		case Lava:
			return lava;
		case Stone:
			return stone;
		case Swamp:
			return swamp;
		case Water:
			return water;
		default:
			return grass;

		}
	}

	public static String getLocalizedMessage(int res) {
		try {
			return LanguageManager.getLang(lang).getLangStringById(res).strValue;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getLocalizedMessage(String res) {
		try {
			return LanguageManager.getLang(lang).getLangByString(res).strValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Vector3 vectorDifference(Vector3 f, Vector3 w) {
		return new Vector3(f.x - w.x, f.y - w.y, f.z - w.z);
	}

	public static Texture getEntityTexture(EntityType type) {
		switch (type) {
		case Human:
			return human;
		case Elf:
			return elf;
		case Orc:
			return orc;
		case Shaolin:
			return shaolin;
		case Skeleton:
			return skeleton;
		case Wolf:
			return wolf;
		case Zombie:
			return zombie;
		default:
			return human;
		}
	}

    public static Skin getSkin(String skinName)
    {
        return skins.get(skinName);
    }

    public static Texture getItemTexture(ItemType type)
    {
        return human;
    }

}
