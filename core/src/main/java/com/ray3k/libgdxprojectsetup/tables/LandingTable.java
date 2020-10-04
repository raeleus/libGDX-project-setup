package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.ray3k.libgdxprojectsetup.Core;

import static com.badlogic.gdx.utils.Align.bottomRight;
import static com.ray3k.libgdxprojectsetup.Core.*;

public class LandingTable extends Table  {
    public LandingTable() {
        setBackground(skin.getDrawable("window"));
        pad(10);
        
        add().expandY();
        
        row();
        defaults().space(50f);
        Image image = new Image(skin.getDrawable("logo-libgdx"));
        add(image);
        
        row();
        Table table = new Table();
        add(table);
    
        table.defaults().spaceRight(5);
        Label label = new Label("LIBGDX", skin);
        table.add(label).right();
        
        label = new Label("1.9.11", skin, "light");
        table.add(label).left();
        
        table.row();
        label = new Label("SNAPSHOT", skin);
        table.add(label).right();
    
        label = new Label("1.9.12-SNAPSHOT", skin, "light");
        table.add(label).left();
        
        table.row();
        label = new Label("SETUP VERSION", skin);
        table.add(label).right();
    
        label = new Label("0.0.1", skin, "light");
        table.add(label).left();
        
        row();
        table = new Table();
        add(table);
        
        TextButton textButton = new TextButton("CREATE NEW PROJECT", skin, "big");
        table.add(textButton);
    
        ImageButton imageButton = new ImageButton(skin, "wizard");
        table.add(imageButton);
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextTable(projectTable);
            }
        });
        
        row();
        defaults().clearActor();
        textButton = new TextButton("libgdx.com", skin, "link");
        add(textButton).align(bottomRight).expand();
    }
}
