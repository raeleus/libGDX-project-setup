package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressEvent;
import com.ray3k.libgdxprojectsetup.widgets.WizardProgress.WizardProgressListener;

import static com.ray3k.libgdxprojectsetup.Core.skin;

public class OptionsTable extends Table  {
    public OptionsTable() {
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
        Table duo = new Table();
        add(duo).expandY();

        duo.defaults().space(25f);
        Table table = new Table();
        duo.add(table).top();

        table.defaults().space(5);
        Label label = new Label("LIBGDX VERSION", skin);
        table.add(label).right();

        SelectBox<String> selectBox = new SelectBox<>(skin);
        selectBox.setItems("1.9.11-SNAPSHOT", "1.9.11", "1.9.10");
        selectBox.setSelectedIndex(1);
        table.add(selectBox).left();

        table.row();
        label = new Label("JAVA VERSION", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("14", "13", "12", "11", "10", "9", "8", "7", "6");
        selectBox.setSelectedIndex(6);
        table.add(selectBox).left();

        table.row();
        label = new Label("ANDROID PLUGIN", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("4.0.0");
        table.add(selectBox).left();

        table.row();
        label = new Label("TARGET ANDROID API", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("25");
        table.add(selectBox).left();

        table.row();
        label = new Label("GWT PLUGIN API", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("1.0.13");
        table.add(selectBox).left();

        table.row();
        label = new Label("APP VERSION", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("0.0.1-SNAPSHOT");
        table.add(selectBox).left();

        table = new Table();
        duo.add(table).expandY();

        table.defaults().space(5);
        label = new Label("SERVER JAVA VER", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("8.0");
        table.add(selectBox).left();

        table.row();
        label = new Label("DESKTOP JAVA VER", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("8.0");
        table.add(selectBox).left();

        table.row();
        label = new Label("ROBOVM VERSION", skin);
        table.add(label).right();

        selectBox = new SelectBox<>(skin);
        selectBox.setItems("2.3.9");
        table.add(selectBox).left();

        table.row();
        label = new Label("ADD GUI ASSETS", skin);
        table.add(label).right();

        CheckBox checkBox = new CheckBox("", skin);
        table.add(checkBox).left();

        table.row();
        label = new Label("ADD README", skin);
        table.add(label).right();

        checkBox = new CheckBox("", skin);
        table.add(checkBox).left();

        table.row();
        label = new Label("GRADLE WRAPPER", skin);
        table.add(label).right();

        checkBox = new CheckBox("", skin);
        table.add(checkBox).left();

        table.row();
        TextButton textButton = new TextButton("GRADLE TASKS...", skin, "small");
        table.add(textButton).colspan(2).right().space(20);

        row();
        table = new Table();
        add(table).growX();

        table.add().uniform();

        WizardProgress progressStack = new WizardProgress(2, 3, "Options", skin);
        table.add(progressStack).expandX();
        progressStack.addListener(new WizardProgressListener() {
            @Override
            public void nodeSelected(int selection, WizardProgressEvent event) {
                System.out.println(selection);
            }
        });

        textButton = new TextButton("NEXT", skin, "small");
        table.add(textButton).uniform();
    }
}
