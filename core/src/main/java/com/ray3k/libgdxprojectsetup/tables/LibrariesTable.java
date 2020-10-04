package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ray3k.libgdxprojectsetup.widgets.LibBuilder;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressEvent;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressListener;

import java.util.Iterator;

import static com.ray3k.libgdxprojectsetup.Core.*;

public class LibrariesTable extends Table  {
    public LibrariesTable() {
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
    
        LibBuilder libBuilder = new LibBuilder(skin);
        JsonReader jsonReader = new JsonReader();
        JsonValue json = jsonReader.parse(Gdx.files.internal("data/libraries.json"));
        Iterator<JsonValue> iter = json.iterator();
        while (iter.hasNext()) {
            JsonValue categoryValue = iter.next();
            String category = categoryValue.name;
            Iterator<JsonValue> libraryIter = categoryValue.iterator();
            while (libraryIter.hasNext()) {
                JsonValue libraryValue = libraryIter.next();
                String library = libraryValue.name;
                String description = libraryValue.getString("description");
                libBuilder.addOption(category, library, description);
            }
        }
        
        libBuilder.addLibrary("PLATFORMS", "LWJGL3");
        libBuilder.addLibrary("PLATFORMS", "Android");
        libBuilder.addLibrary("EXTENSIONS", "Box2D");
        libBuilder.addLibrary("THIRD-PARTY", "RegExodus");
        libBuilder.addLibrary("LANGUAGES", "Kotlin");
        add(libBuilder).growY().top().left();
        
        row();
        Table table = new Table();
        add(table).growX();
    
        TextButton textButton = new TextButton("PREVIOUS", skin, "small");
        table.add(textButton).uniform();
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                previousTable(projectTable);
            }
        });
        
        WizardProgress progressStack = new WizardProgress(1, 3, "LIBRARIES", skin);
        table.add(progressStack).expandX();
        progressStack.addListener(new WizardProgressListener() {
            @Override
            public void nodeSelected(int selection, WizardProgressEvent event) {
                System.out.println(selection);
            }
        });
        
        textButton = new TextButton("NEXT", skin, "small");
        table.add(textButton).uniform();
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nextTable(optionsTable);
            }
        });
    }
}
