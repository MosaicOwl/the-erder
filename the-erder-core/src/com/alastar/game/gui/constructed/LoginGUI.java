package com.alastar.game.gui.constructed;

import ru.alastar.main.net.requests.AuthPacketRequest;
import ru.alastar.net.Client;
import ru.alastar.net.LoginClient;

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

public class LoginGUI extends BaseConstructed
{

    public Table table;

    public LoginGUI(Stage s, String name)
    {
        super(s, name);
        final float bsw = (float)Vars.getDouble("balancedScreenWidth");
        final float bsh = (float)Vars.getDouble("balancedScreenHeight");

        final TextButton btn = new TextButton(
                GameManager.getLocalizedMessage("Login"),
                GameManager.txtBtnStyle);
        btn.setWidth(175 / bsw);
        btn.padLeft(15);
        btn.padRight(15);
        btn.padTop(10);
        btn.padBottom(10);

        final TextButton btnToReg = new TextButton(
                GameManager.getLocalizedMessage("Reg"), GameManager.txtBtnStyle);
        btnToReg.setWidth(175 / bsw);
        btnToReg.padLeft(5);
        btnToReg.padRight(5);
        btnToReg.padTop(5);
        btnToReg.padBottom(5);

        Label nameLabel = new Label(GameManager.getLocalizedMessage("Login")
                + ":", GameManager.labelStyle);

        final TextField nameText = new TextField("Alastar",
                GameManager.txtFieldStyle);
        nameText.setMaxLength(15);
        nameText.setHeight(50 / bsh);
        nameText.setWidth(275 / bsw);

        Label addressLabel = new Label(
                GameManager.getLocalizedMessage("Password") + ":",
                GameManager.labelStyle);

        final TextField passwordText = new TextField("123",
                GameManager.txtFieldStyle);
        passwordText.setMaxLength(15);
        passwordText.setWidth(275 / bsw);
        passwordText.setHeight(50 / bsh);
        passwordText.setMessageText("password");
        passwordText.setPasswordCharacter('*');
        passwordText.setPasswordMode(true);
        
        table = new Table();
        table.setFillParent(true);

        table.add(nameLabel);
        table.row();
        table.add(nameText);
        table.row();
        table.add(addressLabel);
        table.row();
        table.add(passwordText);
        table.row();
        table.add(btn);
        table.row();
        table.add(btnToReg);

        btn.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                AuthPacketRequest r = new AuthPacketRequest();
                r.login = nameText.getText();
                r.pass = passwordText.getText();
                Client.login = r.login;
                Client.pass = r.pass;
                LoginClient.Send(r);
                ((LoadingScreenGUI) GUICore
                        .getConstructedByName("loading_screen")).Show();
            }
        });

        btnToReg.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                GUICore.getConstructedByName("register").Show();
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
