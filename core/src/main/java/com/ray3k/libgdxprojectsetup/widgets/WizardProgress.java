package com.ray3k.libgdxprojectsetup.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class WizardProgress extends Table {
    private Label label;
    private Button targetButton;
    private Array<Button> buttons;
    
    public WizardProgress(int selected, int nodeCount, String labelText, Skin skin) {
        this(selected, nodeCount, labelText, skin.get(ProgressGroupStyle.class));
    }
    
    public WizardProgress(int selected, int nodeCount, String labelText, Skin skin, String style) {
        this(selected, nodeCount, labelText, skin.get(style, ProgressGroupStyle.class));
    }
    
    public WizardProgress(int selected, int nodeCount, String labelText, ProgressGroupStyle style) {
        setBackground(style.background);
        
        buttons = new Array<>();
        for (int i = 0; i < nodeCount; i++) {
            Button button = new Button(style.buttonStyle);
            if (i == 0 || i == nodeCount - 1) {
                add(button);
            } else {
                add(button).expandX();
            }
            buttons.add(button);
            final int index = i;
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    fire(new WizardProgressEvent(index));
                }
            });
            
            if (selected == i) {
                targetButton = button;
                button.setChecked(true);
                button.setTouchable(Touchable.disabled);
                label = new Label(labelText, style.labelStyle);
                addActor(label);
            }
        }
    }
    
    @Override
    public void layout() {
        super.layout();
        if (targetButton != null) label.setPosition(targetButton.getX(Align.center), targetButton.getY(Align.top), Align.bottom);
    }
    
    public static class ProgressGroupStyle {
        //required
        public Drawable background;
        public ButtonStyle buttonStyle;
        public LabelStyle labelStyle;
    
        public ProgressGroupStyle() {
        }
    
        public ProgressGroupStyle(Drawable background, ButtonStyle buttonStyle, LabelStyle labelStyle) {
            this.background = background;
            this.buttonStyle = buttonStyle;
            this.labelStyle = labelStyle;
        }
        
        public ProgressGroupStyle(ProgressGroupStyle style) {
            this.background = style.background;
            this.buttonStyle = style.buttonStyle;
            this.labelStyle = style.labelStyle;
        }
    }
    
    public static class WizardProgressEvent extends Event {
        int selection;
    
        public WizardProgressEvent(int selection) {
            this.selection = selection;
        }
    }
    
    public abstract static class WizardProgressListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof WizardProgressEvent) {
                WizardProgressEvent wizardProgressEvent = (WizardProgressEvent) event;
                nodeSelected(wizardProgressEvent.selection, wizardProgressEvent);
                return true;
            }
            return false;
        }
        
        public abstract void nodeSelected(int selection, WizardProgressEvent event);
    }
}
