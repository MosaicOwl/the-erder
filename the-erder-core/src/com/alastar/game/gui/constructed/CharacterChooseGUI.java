package com.alastar.game.gui.constructed;

import ru.alastar.main.net.requests.CharacterChooseRequest;
import ru.alastar.net.Client;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.alastar.game.gui.GUICore;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class CharacterChooseGUI extends BaseConstructed
{

    public Table table;
    private int id = 0;

    public CharacterChooseGUI(Stage s, String name)
    {
        super(s, name);
        final int bsw = Vars.getInt("balancedScreenWidth");
        
        String n = "";

        final Label nameLabel1 = new Label(n, GameManager.labelStyle);

        final TextButton btnN = new TextButton(">", GameManager.txtBtnStyle);

        final TextButton btnP = new TextButton("<", GameManager.txtBtnStyle);


        final TextButton btnCh = new TextButton(
                GameManager.getLocalizedMessage("Choose"),
                GameManager.txtBtnStyle);

        btnCh.setWidth(175 / bsw);

        final TextButton btnCr = new TextButton(
                GameManager.getLocalizedMessage("Create"),
                GameManager.txtBtnStyle);

        btnCr.setWidth(175 / bsw);

        final TextButton btnDel = new TextButton(
                GameManager.getLocalizedMessage("Delete"),
                GameManager.txtBtnStyle);

        btnDel.setWidth(175 / bsw);

        table = new Table();
        table.setFillParent(true);
        table.add(nameLabel1);
        table.row();

        table.add(nameLabel1);      
        table.row();
        
        table.add(btnP);
        table.add(btnN);
        table.row();

        table.add(btnCr); 
        table.row();

        table.add(btnDel); 
        table.row();

        table.add(btnCh);

        btnCh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Client.characters.size() > 0) {
                    CharacterChooseRequest r = new CharacterChooseRequest();
                    r.nick = nameLabel1.getText().toString();
                    Client.Send(r);
                    Hide();
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
                GUICore.getConstructedByName("create").Show();
                Hide();
            }
        });
        this.register(table);

    }
    @Override
    public void Hide()
    {
        table.setVisible(false);
    }
    
    @Override
    public void Show()
    {
        table.setVisible(true);
    }
}
