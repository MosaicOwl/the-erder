package com.alastar.game.gui.constructed;

import ru.alastar.main.net.requests.RegistrationPacketRequest;
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

public class RegisterGUI extends BaseConstructed
{

    public Table table;

    public RegisterGUI(Stage s, String name)
    {
        super(s, name);

        final int bsw = Vars.getInt("balancedScreenWidth");

        final TextButton btnToLog = new TextButton(
                GameManager.getLocalizedMessage("Login"),
                GameManager.txtBtnStyle);
        btnToLog.setWidth(175 / bsw);

        final TextButton btnReg = new TextButton(
                GameManager.getLocalizedMessage("Reg"), GameManager.txtBtnStyle);
        btnReg.setWidth(175 / bsw);

        Label loginLabel = new Label(GameManager.getLocalizedMessage("Login")
                + ":", GameManager.labelStyle);

        final TextField loginText = new TextField("Login",
                GameManager.txtFieldStyle);
        loginText.setWidth(175 / bsw);

        Label passLabel = new Label(GameManager.getLocalizedMessage("Password")
                + ":", GameManager.labelStyle);

        final TextField passText = new TextField("Pass",
                GameManager.txtFieldStyle);
        passText.setWidth(175 / bsw);

        Label mailLabel = new Label("E-Mail:", GameManager.labelStyle);

        final TextField mailText = new TextField("Mail",
                GameManager.txtFieldStyle);
        mailText.setWidth(175 / bsw);

        table = new Table();
        table.setFillParent(true);

        table.add(loginLabel);
        table.row();

        table.add(loginText);
        table.row();

        table.add(passLabel);
        table.row();

        table.add(passText);
        table.row();

        table.add(mailLabel);
        table.row();

        table.add(mailText);
        table.row();

        table.add(btnReg);
        table.row();

        table.add(btnToLog);

        btnToLog.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                GUICore.getConstructedByName("login").Show();
                Hide();
            }
        });

        btnReg.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                RegistrationPacketRequest r = new RegistrationPacketRequest();
                r.login = loginText.getText();
                r.pass = passText.getText();
                r.mail = mailText.getText();

                LoginClient.Send(r);
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
