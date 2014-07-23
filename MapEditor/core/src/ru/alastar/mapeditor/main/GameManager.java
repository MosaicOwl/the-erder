package ru.alastar.mapeditor.main;

import com.alastar.game.enums.TileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class GameManager {

	public static int fontSize = 15;
	public static Texture grass;
	public static Texture stone;
	public static Texture swamp;
	public static Texture lava;
	public static Texture water;
	public static Texture brick;

    static TextButtonStyle txtBtnStyle;
	public static LabelStyle labelStyle;
	public static TextFieldStyle txtFieldStyle;
	public static Texture notpassable;
	public static Texture passable;
	public static BitmapFont font;
    public static int texWidth;
    public static int texHeight;
    public static float screenWidth;
    public static float screenHeight;


	@SuppressWarnings("deprecation")
    public static void LoadContent() {
				
		grass = new Texture(Gdx.files.internal("grass.png"));
		water = new Texture(Gdx.files.internal("Water.png"));
		stone = new Texture(Gdx.files.internal("Stone.png"));
		lava = new Texture(Gdx.files.internal("Lava.png"));
		swamp = new Texture(Gdx.files.internal("Swamp.png"));
		brick = new Texture(Gdx.files.internal("bricks.png"));
		notpassable = new Texture(Gdx.files.internal("notpassable.png"));
		passable = new Texture(Gdx.files.internal("passable.png"));
		texWidth = grass.getWidth();
		texHeight = grass.getWidth();
		FileHandle fontFile = Gdx.files.internal("./bin/data/fonts/tahoma.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                fontFile);
        font = generator.generateFont(fontSize, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ÉÖÓÊÅÍÃØÙÇÕÚÔÛÂÀÏĞÎËÄÆİß×ÑÌÈÒÜÁŞéöóêåíãøùçõúôûâàïğîëäæıÿ÷ñìèòüáş:][_!$%#@|\\/?-+=()*&.;,{}\"'<>",
                false);
        
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        
        Vars.balancedScreenHeight = 1024 / screenHeight;
        Vars.balancedScreenWidth = 1280 / screenWidth;
        
		txtBtnStyle = new TextButtonStyle();
		txtBtnStyle.font = getFont();

		labelStyle = new LabelStyle();
		labelStyle.font = getFont();

		txtFieldStyle = new TextFieldStyle();
		txtFieldStyle.font = getFont();
		txtFieldStyle.fontColor = new Color(1, 1, 1, 1);
		
	}

	public static Texture getTexture(TileType t) {
		switch (t) {
		case Grass:
			return grass;
		case Lava:
			return lava;
		case Stone:
			return stone;
		case Swamp:
			return swamp;
		case Water:
			return water;
		case Brick:
			return brick;
		default:
			return grass;
		}
	}

	public static BitmapFont getFont()
    {
		return font;
    }
}
