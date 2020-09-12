package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressEvent;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressListener;

import static com.ray3k.libgdxprojectsetup.Core.skin;

public class ProjectTable extends Table  {
    public ProjectTable() {
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
        Table table = new Table();
        add(table).expandY();
    
        table.defaults().spaceRight(5);
        Label label = new Label("PROJECT NAME", skin);
        table.add(label).right();
    
        TextField textField = new TextField("", skin);
        textField.setMessageText("my-gdx-game");
        table.add(textField);
        
        table.row();
        label = new Label("CORE PACKAGE", skin);
        table.add(label).right();
    
        textField = new TextField("", skin);
        textField.setMessageText("com.mygdx.game");
        table.add(textField).left();
        
        table.row();
        label = new Label("MAIN CLASS", skin);
        table.add(label).right();
    
        textField = new TextField("", skin);
        textField.setMessageText("Main");
        table.add(textField).left();
        
        row();
        table = new Table();
        add(table).growX();
        
        table.add().uniform();
        
        WizardProgress progressStack = new WizardProgress(0, 3, "Project Details", skin);
        table.add(progressStack).expandX();
        progressStack.addListener(new WizardProgressListener() {
            @Override
            public void nodeSelected(int selection, WizardProgressEvent event) {
                System.out.println(selection);
            }
        });
        
        TextButton textButton = new TextButton("NEXT", skin, "small");
        table.add(textButton).uniform();
    }
}
