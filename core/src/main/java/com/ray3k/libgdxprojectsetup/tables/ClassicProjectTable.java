package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressEvent;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressListener;

import static com.ray3k.libgdxprojectsetup.Core.*;

public class ClassicProjectTable extends Table  {
    public ClassicProjectTable() {
        setBackground(skin.getDrawable("window"));
        pad(10);
        
        row();
        Image image = new Image(skin.getDrawable("logo-libgdx-small"));
        add(image);
        
        row();
        image = new Image(skin.getDrawable("divider-horizontal"));
        add(image).growX().space(15);
        
        row();
        defaults().space(50f);
        Table outer = new Table();
        add(outer).expand();

        Table table = new Table();
        outer.add(table);
    
        table.defaults().spaceRight(5);
        Label label = new Label("PROJECT NAME", skin);
        table.add(label).right();
    
        TextField textField = new TextField("", skin);
        textField.setMessageText("my-gdx-game");
        table.add(textField);
        
        table.row();
        label = new Label("MAIN CLASS", skin);
        table.add(label).right();
    
        textField = new TextField("", skin);
        textField.setMessageText("Main");
        table.add(textField).left();

        outer.row();
        image = new Image(skin, "divider-horizontal");
        outer.add(image).growX().space(10);

        outer.row();
        table = new Table();
        outer.add(table);

        table.defaults().left();
        CheckBox checkBox = new CheckBox("DESKTOP LWJGL3", skin);
        table.add(checkBox);

        table.row();
        checkBox = new CheckBox("ANDROID", skin);
        table.add(checkBox);

        table.row();
        checkBox = new CheckBox("IOS ROBOVM", skin);
        table.add(checkBox);

        table.row();
        checkBox = new CheckBox("HTML5 GWT", skin);
        table.add(checkBox);
        
        row();
        table = new Table();
        add(table).growX();

        TextButton textButton = new TextButton("PREVIOUS", skin, "small");
        table.add(textButton).uniform();
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                previousTable(landingTable);
            }
        });

        table.add().expandX();
        
        textButton = new TextButton("GENERATE", skin, "small");
        table.add(textButton).uniform();
    }
}
