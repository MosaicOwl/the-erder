package com.alastar.game.gui.constructed;

import ru.alastar.main.net.requests.CreateCharacterRequest;
import ru.alastar.net.Client;

import com.alastar.game.GameManager;
import com.alastar.game.Vars;
import com.alastar.game.gui.GUICore;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class CharacterCreateGUI extends BaseConstructed
{

    public Table table;
    private int  id = 0;

    public CharacterCreateGUI(Stage s, String name)
    {
        super(s, name);

        final float bsw = (float)Vars.getDouble("balancedScreenWidth");

        Label nickLabel = new Label(GameManager.getLocalizedMessage("Name")
                + ":", GameManager.labelStyle);

        final TextField nickText = new TextField("Alastar",
                GameManager.txtFieldStyle);

        nickText.setWidth(175 / bsw);

        final Label raceLabel1 = new Label(
                com.alastar.game.enums.EntityType.values()[0].name(),
                GameManager.labelStyle);

        final TextButton btnRP = new TextButton("<", GameManager.txtBtnStyle);

        final TextButton btnRN = new TextButton(">", GameManager.txtBtnStyle);

        final TextButton btnCreate = new TextButton(
                GameManager.getLocalizedMessage("Confirm"),
                GameManager.txtBtnStyle);

        btnCreate.setWidth(175 / bsw);
        btnCreate.padLeft(15);
        btnCreate.padRight(15);
        btnCreate.padTop(10);
        btnCreate.padBottom(10);
        final TextButton btnBack = new TextButton(
                GameManager.getLocalizedMessage("Back"),
                GameManager.txtBtnStyle);

        btnBack.setWidth(175 / bsw);
        btnBack.padLeft(15);
        btnBack.padRight(15);
        btnBack.padTop(10);
        btnBack.padBottom(10);
        
        table = new Table();
        table.setFillParent(true);

        table.add(nickLabel);
        table.row();
        table.add(nickText);
        table.row();

        table.add(btnRP);
        table.add(raceLabel1);
        table.add(btnRN);
        table.row();

        table.add(btnCreate);
        table.row();

        table.add(btnBack);

        btnRP.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if ((id - 1) < 1)
                    id = com.alastar.game.enums.EntityType.values().length;
                else
                    --id;

                raceLabel1.setText((com.alastar.game.enums.EntityType.values()[id - 1])
                        .name());
            }
        });

        btnRN.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                if ((id + 1) > com.alastar.game.enums.EntityType.values().length)
                    id = 1;
                else
                    ++id;

                raceLabel1.setText((com.alastar.game.enums.EntityType.values()[id - 1])
                        .name());
            }
        });

        btnBack.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                GUICore.getConstructedByName("choose").Show();
                Hide();
            }
        });

        btnCreate.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                CreateCharacterRequest r = new CreateCharacterRequest();
                r.nick = nickText.getText().toString();
                r.type = com.alastar.game.enums.EntityType.valueOf(raceLabel1
                        .getText().toString());
                Client.Send(r);
                id = 0;
                GUICore.getConstructedByName("choose").Show();
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
