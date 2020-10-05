package com.ray3k.libgdxprojectsetup.tables;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.ray3k.tenpatch.TenPatchDrawable;

import static com.ray3k.libgdxprojectsetup.Core.*;

public class LoadingTable extends Table  {
    TenPatchDrawable tenPatchDrawable;
    Mode mode;
    public enum Mode {
        LOADING, HIDE, HIDING, DONE
    }

    public LoadingTable() {
        tenPatchDrawable = new TenPatchDrawable(skin.get("loading-animation", TenPatchDrawable.class));
        Image image = new Image(tenPatchDrawable);
        add(image);
        mode = Mode.LOADING;

        addAction(Actions.delay(5.0f, Actions.run(() -> {
            mode = Mode.HIDE;
        })));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (tenPatchDrawable.getRegions().peek().equals(tenPatchDrawable.getKeyFrame())) {
            switch (mode) {
                case HIDE:
                    mode = Mode.HIDING;
                    clearChildren();
                    tenPatchDrawable = new TenPatchDrawable(skin.get("loading-hide", TenPatchDrawable.class));
                    Image image = new Image(tenPatchDrawable);
                    add(image);
                    break;
                case HIDING:
                    clearChildren();
                    mode = Mode.DONE;
                    break;
            }
        }
    }
}
