package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static com.ray3k.libgdxprojectsetup.Core.*;

public class LoadingTable extends Table  {
    public LoadingTable() {
        Image image = new Image(skin, "loading-animation");
        add(image);
    }
}
