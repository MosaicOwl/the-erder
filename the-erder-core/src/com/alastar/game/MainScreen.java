package com.alastar.game;

import ru.alastar.main.net.requests.AuthPacketRequest;
import ru.alastar.main.net.requests.CharacterChooseRequest;
import ru.alastar.main.net.requests.CreateCharacterRequest;
import ru.alastar.main.net.requests.DropdownMenuRequest;
import ru.alastar.main.net.requests.InputRequest;
import ru.alastar.main.net.requests.RegistrationPacketRequest;
import ru.alastar.main.net.requests.TargetRequest;
import ru.alastar.net.Client;
import ru.alastar.net.LoginClient;

import com.alastar.game.gui.GUICore;
import com.alastar.game.gui.constructed.LoadingScreenGUI;
import com.alastar.game.gui.constructed.ServersListGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainScreen implements Screen, InputProcessor, GestureListener  {

	final ErderGame game;

	public static Stage LoginStage;
	public static Stage RegisterStage;
	public static Stage stageCreate;
	public static Stage stageChoose;
    public static Stage currentStage;
    public static Stage serversStage;

	public static int tileView = 13;
	
	public static Stage gui;
	
	public static Label nameLabel1;
	// public static Image back;
	public static int id = 0;

	public static OrthographicCamera camera;


	public MainScreen(ErderGame gam) {
		LoginClient.Connect();
        final int bsw = Vars.getInt("balancedScreenWidth");
        final int bsh = Vars.getInt("balancedScreenHeight");
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 750 / Vars.getInt("balancedScreenWidth"), 400 / Vars.getInt("balancedScreenHeight"));
	
        game = gam;

		gui = new Stage();

		LoginStage = new Stage();
		currentStage = LoginStage;
		
		//Constructed guis
        GUICore.addConstructedGUI(new LoadingScreenGUI(LoginStage));
        ((LoadingScreenGUI)GUICore.getConstructedByName("loading_screen")).Hide();
		final TextButton btn = new TextButton(
				GameManager.getLocalizedMessage("Login"),
				GameManager.txtBtnStyle);
		btn.setPosition(700 / bsw,
				700 / bsh);
		btn.setWidth(175 / bsw);
		btn.padLeft(5);

		final TextButton btnToReg = new TextButton(
				GameManager.getLocalizedMessage("Reg"), GameManager.txtBtnStyle);
		btnToReg.setPosition(700 / bsw,
				750 / bsh);
		btnToReg.setWidth(175 / bsw);
		btnToReg.padLeft(5);
		Label nameLabel = new Label(GameManager.getLocalizedMessage("Login")
				+ ":", GameManager.labelStyle);
		nameLabel.setPosition(700 / bsw,
				950 / bsh);

		final TextField nameText = new TextField("Alastar",
				GameManager.txtFieldStyle);
		nameText.setPosition(700 / bsw,
				900 / bsh);
		nameText.setWidth(175 / bsw);

		Label addressLabel = new Label(
				GameManager.getLocalizedMessage("Password") + ":",
				GameManager.labelStyle);
		addressLabel.setPosition(700 / bsw,
				850 / bsh);

		final TextField passwordText = new TextField("123",
				GameManager.txtFieldStyle);
		passwordText.setPosition(700 / bsw,
				800 / bsh);
		passwordText.setWidth(175 / bsw);
		passwordText.setMessageText("password");
		passwordText.setPasswordCharacter('*');
		passwordText.setPasswordMode(true);
		LoginStage.addActor(nameLabel);
		LoginStage.addActor(nameText);
		LoginStage.addActor(addressLabel);
		LoginStage.addActor(passwordText);
		LoginStage.addActor(btn);
		LoginStage.addActor(btnToReg);

		btn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AuthPacketRequest r = new AuthPacketRequest();
				r.login = nameText.getText();
				r.pass = passwordText.getText();
				Client.login = r.login;
				Client.pass = r.pass;
				LoginClient.Send(r);
		        ((LoadingScreenGUI)GUICore.getConstructedByName("loading_screen")).Show();
			}
		});

		btnToReg.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    currentStage = RegisterStage;
			}
		});

		RegisterStage = new Stage();

		final TextButton btnToLog = new TextButton(
				GameManager.getLocalizedMessage("Login"),
				GameManager.txtBtnStyle);
		btnToLog.setPosition(700 / bsw,
				650 / bsh);
		btnToLog.setWidth(175 / bsw);

		final TextButton btnReg = new TextButton(
				GameManager.getLocalizedMessage("Reg"), GameManager.txtBtnStyle);
		btnReg.setPosition(700 / bsw,
				600 / bsh);
		btnReg.setWidth(175 / bsw);

		Label loginLabel = new Label(GameManager.getLocalizedMessage("Login")
				+ ":", GameManager.labelStyle);
		loginLabel.setPosition(700 / bsw,
				950 / bsh);

		final TextField loginText = new TextField("Login",
				GameManager.txtFieldStyle);
		loginText.setPosition(700 / bsw,
				900 / bsh);
		loginText.setWidth(175 / bsw);

		Label passLabel = new Label(GameManager.getLocalizedMessage("Password")
				+ ":", GameManager.labelStyle);
		passLabel.setPosition(700 / bsw,
				850 / bsh);

		final TextField passText = new TextField("Pass",
				GameManager.txtFieldStyle);
		passText.setPosition(700 / bsw,
				800 / bsh);
		passText.setWidth(175 / bsw);

		Label mailLabel = new Label("E-Mail:", GameManager.labelStyle);
		mailLabel.setPosition(700 / bsw,
				750 / bsh);

		final TextField mailText = new TextField("Mail",
				GameManager.txtFieldStyle);
		mailText.setPosition(700 / bsw,
				700 / bsh);
		mailText.setWidth(175 / bsw);

		RegisterStage.addActor(btnToLog);
		RegisterStage.addActor(btnReg);
		RegisterStage.addActor(loginLabel);
		RegisterStage.addActor(loginText);
		RegisterStage.addActor(passLabel);
		RegisterStage.addActor(passText);
		RegisterStage.addActor(mailLabel);
		RegisterStage.addActor(mailText);

		btnToLog.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    currentStage = LoginStage;
			}
		});
		btnReg.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				RegistrationPacketRequest r = new RegistrationPacketRequest();
				r.login = loginText.getText();
				r.pass = passText.getText();
				r.mail = mailText.getText();

				LoginClient.Send(r);
			}
		});
		stageChoose = new Stage();

		String n = "";

		nameLabel1 = new Label(n, GameManager.labelStyle);
		nameLabel1.setPosition(700 / bsw,
				960 / bsh);

		final TextButton btnN = new TextButton(">", GameManager.txtBtnStyle);
		btnN.setPosition(765 / bsw,
				900 / bsh);

		final TextButton btnP = new TextButton("<", GameManager.txtBtnStyle);
		btnP.setPosition(725 / bsw,
				900 / bsh);

		final TextButton btnCh = new TextButton(
				GameManager.getLocalizedMessage("Choose"),
				GameManager.txtBtnStyle);
		btnCh.setPosition(700 / bsw,
				850 / bsh);
		btnCh.setWidth(175 / bsw);

		final TextButton btnCr = new TextButton(
				GameManager.getLocalizedMessage("Create"),
				GameManager.txtBtnStyle);
		btnCr.setPosition(700 / bsw,
				800 / bsh);
		btnCr.setWidth(175 / bsw);

		final TextButton btnDel = new TextButton(
				GameManager.getLocalizedMessage("Delete"),
				GameManager.txtBtnStyle);
		btnDel.setPosition(700 / bsw,
				750 / bsh);
		btnDel.setWidth(175 / bsw);

		stageChoose.addActor(nameLabel1);

		stageChoose.addActor(btnP);

		stageChoose.addActor(nameLabel1);

		stageChoose.addActor(btnN);

		stageChoose.addActor(btnCr);
		stageChoose.addActor(btnDel);
		stageChoose.addActor(btnCh);

		btnCh.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (Client.characters.size() > 0) {
					CharacterChooseRequest r = new CharacterChooseRequest();
					r.nick = nameLabel1.getText().toString();
					Client.Send(r);
				}
			}
		});

		btnP.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (Client.characters.size() > 0) {

					if ((id - 1) < 0) {
						id = Client.characters.size() - 1;
					} else {
						--id;
					}

					nameLabel1.setText((String)Client.characters.keySet().toArray()[id]);
				}
			}
		});

		btnN.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (Client.characters.size() > 0) {
					if ((id + 1) >= Client.characters.size()) {
						id = 0;
					} else {
						++id;
					}

					nameLabel1.setText((String)Client.characters.keySet().toArray()[id]);
				}
			}
		});

		btnCr.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    currentStage = stageCreate;
			}
		});

		stageCreate = new Stage();

		Label nickLabel = new Label(GameManager.getLocalizedMessage("Name")
				+ ":", GameManager.labelStyle);
		nickLabel.setPosition(700 / bsw,
				900 / bsh);

		final TextField nickText = new TextField("Alastar",
				GameManager.txtFieldStyle);
		nickText.setPosition(700 / bsw,
				850 / bsh);
		nickText.setWidth(175 / bsw);

		final Label raceLabel1 = new Label(com.alastar.game.enums.EntityType.values()[0].name(),
				GameManager.labelStyle);
		raceLabel1.setPosition(700 / bsw,
				800 / bsh);

		final TextButton btnRP = new TextButton("<", GameManager.txtBtnStyle);
		btnRP.setPosition(725 / bsw,
				750 / bsh);

		final TextButton btnRN = new TextButton(">", GameManager.txtBtnStyle);
		btnRN.setPosition(765 / bsw,
				750 / bsh);

		final TextButton btnCreate = new TextButton(
				GameManager.getLocalizedMessage("Confirm"),
				GameManager.txtBtnStyle);
		btnCreate.setPosition(700 / bsw,
				700 / bsh);
		btnCreate.setWidth(175 / bsw);

		final TextButton btnBack = new TextButton(
				GameManager.getLocalizedMessage("Back"),
				GameManager.txtBtnStyle);
		btnBack.setPosition(700 / bsw,
				650 / bsh);
		btnBack.setWidth(175 / bsw);

		stageCreate.addActor(nickLabel);
		stageCreate.addActor(nickText);
		stageCreate.addActor(btnRP);
		stageCreate.addActor(raceLabel1);
		stageCreate.addActor(btnRN);
		stageCreate.addActor(btnCreate);
		stageCreate.addActor(btnBack);

		btnRP.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if ((id - 1) < 1)
					id = com.alastar.game.enums.EntityType.values().length;
				else
					--id;

				raceLabel1.setText((com.alastar.game.enums.EntityType.values()[id - 1]).name());
			}
		});

		btnRN.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if ((id + 1) > com.alastar.game.enums.EntityType.values().length)
					id = 1;
				else
					++id;

				raceLabel1.setText((com.alastar.game.enums.EntityType.values()[id - 1]).name());
			}
		});

		btnBack.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    currentStage = stageChoose;
			}
		});

		btnCreate.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CreateCharacterRequest r = new CreateCharacterRequest();
				r.nick = nickText.getText().toString();
				r.type = com.alastar.game.enums.EntityType.valueOf(raceLabel1.getText().toString());
				Client.Send(r);
				id = 0;
                currentStage = stageChoose;
			}
		});
		
		serversStage = new Stage();
		GUICore.addConstructedGUI(new ServersListGUI(serversStage));
		
		
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);

		Gdx.input.setInputProcessor(im);
    }
	
  
    
	@Override
	public void render(float delta) {
       
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
        
		ErderGame.batch.begin(); 
	    ErderGame.batch.setProjectionMatrix(camera.combined);

		        Map.StepInterpolations();
		        if (Map.world != null && Client.controlledEntity != null) {      
		            if (Map.world
		                    .isUnderTile(Client.controlledEntity.position)) {
		                Draw(Map.world.zMin,
		                       (int)Client.controlledEntity.position.z,
		                (int) camera.position.x / GameManager.textureResolution
		                        - tileView, (int) camera.position.x
		                        / GameManager.textureResolution + tileView,
		                (int) camera.position.y / GameManager.textureResolution
		                        - tileView, (int) camera.position.y
		                        / GameManager.textureResolution + tileView);
		            } else {
		                 Draw(Map.world.zMin,
		                            Map.world.zMax + 1,
		                            (int) camera.position.x / GameManager.textureResolution
		                                    - tileView, (int) camera.position.x
		                                    / GameManager.textureResolution + tileView,
		                            (int) camera.position.y / GameManager.textureResolution
		                                    - tileView, (int) camera.position.y
		                                    / GameManager.textureResolution + tileView);
		            }
		        }
		        ErderGame.batch.end(); 
		        
	            currentStage.act(Gdx.graphics.getDeltaTime());
	            currentStage.draw();
	            Table.drawDebug(currentStage);
	            
	            if(ErderGame.canMove){
		        InputRequest r = new InputRequest();
		        if (Gdx.input.isKeyPressed(Keys.W)) {
		            r.x = 0;
		            r.y = 1;
		            Client.Send(r);
		        }
		        if (Gdx.input.isKeyPressed(Keys.S)) {
		            r.x = 0;
		            r.y = -1;
		            Client.Send(r);
		        }
		        if (Gdx.input.isKeyPressed(Keys.D)) {
		            r.x = 1;
		            r.y = 0;
		            Client.Send(r);
		        }
		        if (Gdx.input.isKeyPressed(Keys.A)) {
		            r.x = -1;
		            r.y = 0;
		            Client.Send(r);
		        }
	            }
	} 
	 
	private void Draw(int zMin, int zMax, int xMin, int xMax, int yMin, int yMax)
	    {
	        TexturedObject t;
	        for (int z = zMin - 1; z <= zMax + 1; ++z) {
	            for (int x = xMin; x <= xMax; ++x) {
	                for (int y = yMax; y >= yMin; --y) {

	                    t = Map.world.tiles.get(new Vector3(x,
	                            y, z));

	                    if (t != null) {
	                        t.Draw(ErderGame.batch, x * GameManager.textureResolution,
	                                (y * GameManager.textureResolution)
	                                + (z * GameManager.textureResolution));
	                      }
	                    }
	                }
	            Map.drawAllByZ(z);

	            }
	 }

	@Override
	public void resize(int width, int height) {
	  currentStage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		LoginStage.dispose();
		RegisterStage.dispose();
		stageCreate.dispose();
		stageChoose.dispose();
		gui.dispose();
	}

    @Override
    public boolean keyDown(int keycode)
    {
        currentStage.keyDown(keycode);
        return false;
    }


    @Override
    public boolean keyUp(int keycode)
    {
        MainScreen.currentStage.keyUp(keycode);
        return false;
    }



    @Override
    public boolean keyTyped(char character)
    {
        MainScreen.currentStage.keyTyped(character);
        return false;
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {    
        return false;
    }



    private boolean ProcessTouch(int x, int y, int count, int button)
    {

        Vector3 vec = camera.unproject(new Vector3(x, y, 0));
        for(TexturedObject obj: Map.entities)
        {
           if(obj.getWindowRectangle().contains(vec.x, vec.y))
           {
               if(count == 1){
               DropdownMenuRequest r = new DropdownMenuRequest();
               r.id = obj.getId();
               r.type = obj.getType();
               r.x = obj.getTransform().position.x;
               r.y = obj.getTransform().position.y;
               Client.Send(r);
               }
               else
               {
                  TargetRequest r = new TargetRequest();
                  r.id = obj.getId();
                  Client.Send(r);
                  System.out.println("Send target packet");
               }
               return true;
               
           }
        }
     /*   for(TexturedObject obj: Map.world.tiles.values())
        {
           if(obj.getWindowRectangle().contains(vec.x, vec.y))
           {
               DropdownMenuRequest r = new DropdownMenuRequest();
               r.id = obj.getId();
               r.type = obj.getType();
               r.x = obj.getTransform().position.x;
               r.y = obj.getTransform().position.y;
               PacketGenerator.generatePacket(r);
               return true;
           }
        }*/
        return false;

    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        MainScreen.currentStage.touchUp(screenX, screenY, pointer, button);
        return false;
    }



    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        MainScreen.currentStage.touchDragged(screenX, screenY, pointer);
        return false;
    }



    @Override
   public boolean mouseMoved(int screenX, int screenY)
    {
        MainScreen.currentStage.mouseMoved(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        MainScreen.currentStage.scrolled(amount);
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        if(!MainScreen.currentStage.touchDown((int)x, (int)y, 0, button))
        {
            ProcessTouch((int)x, (int)y, count, button);
            return true;
        }
        else
        {
           return false;
        }
    }

    @Override
    public boolean longPress(float x, float y)
    {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        return false;
    }



    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        return false;
    }



    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        return false;
    }



    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        return false;
    }



    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2)
    {
        return false;
    }



    public static void PushMessage(String string, boolean canDisturb)
    {
        ((LoadingScreenGUI)GUICore.getConstructedByName("loading_screen")).ChangeCaption(string, canDisturb);
    }

}
