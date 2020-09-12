package com.ray3k.libgdxprojectsetup.widgets;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class PopTable extends Table {
    private Stage stage;
    private final Image stageBackground;
    private WidgetGroup group;
    private final static Vector2 temp = new Vector2();
    private boolean hideOnUnfocus;
    private int attachEdge;
    private int attachAlign;
    private boolean keepSizedWithinStage;
    private boolean automaticallyResized;
    private boolean keepCenteredInWindow;
    private Actor attachToActor;
    private final HideListener hideListener;
    private boolean modal;
    private boolean hidden;
    private final PopTableStyle style;
    private Array<InputListener> keyInputListeners;
    Actor previousKeyboardFocus, previousScrollFocus;
    private FocusListener focusListener;
    
    public PopTable() {
        this(new PopTableStyle());
    }
    
    public PopTable(Skin skin) {
        this(skin.get(PopTableStyle.class));
        setSkin(skin);
    }
    
    public PopTable(Skin skin, String style) {
        this(skin.get(style, PopTableStyle.class));
        setSkin(skin);
    }
    
    public PopTable(PopTableStyle style) {
        setTouchable(Touchable.enabled);
        hideListener = new HideListener();
        
        stageBackground = new Image(style.stageBackground);
        stageBackground.setFillParent(true);
        
        setBackground(style.background);
        
        attachEdge = Align.bottom;
        attachAlign = Align.bottom;
        keepSizedWithinStage = true;
        automaticallyResized = true;
        keepCenteredInWindow = false;
        setModal(false);
        setHideOnUnfocus(false);
        hidden = true;
        this.style = style;
        
        keyInputListeners = new Array<InputListener>();
        
        focusListener = new FocusListener() {
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }
    
            public void scrollFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }
    
            private void focusChanged (FocusEvent event) {
                if (modal && stage != null && stage.getRoot().getChildren().size > 0
                        && stage.getRoot().getChildren().peek() == group) { // PopTable is top most actor.

                    Actor newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(PopTable.this)
                            && !(newFocusedActor.equals(previousKeyboardFocus) || newFocusedActor.equals(previousScrollFocus))) {
                        event.cancel();
                    }
                }
            }
        };
    }
    
    private void alignToActorEdge(Actor actor, int edge, int alignment) {
        float widgetX;
        switch (edge) {
            case Align.left:
            case Align.bottomLeft:
            case Align.topLeft:
                widgetX = 0;
                break;
            case Align.right:
            case Align.bottomRight:
            case Align.topRight:
                widgetX = actor.getWidth();
                break;
            default:
                widgetX = actor.getWidth() / 2f;
                break;
        }
    
        float widgetY;
        switch (edge) {
            case Align.bottom:
            case Align.bottomLeft:
            case Align.bottomRight:
                widgetY = 0;
                break;
            case Align.top:
            case Align.topLeft:
            case Align.topRight:
                widgetY = actor.getHeight();
                break;
            default:
                widgetY = actor.getHeight() / 2f;
                break;
        }
        
        switch (alignment) {
            case Align.bottom:
            case Align.top:
            case Align.center:
                widgetX -= getWidth() / 2;
                break;
                
            case Align.left:
            case Align.bottomLeft:
            case Align.topLeft:
                widgetX -= getWidth();
                break;
        }
        
        switch (alignment) {
            case Align.right:
            case Align.left:
            case Align.center:
                widgetY -= getHeight() / 2;
                break;
                
            case Align.bottom:
            case Align.bottomLeft:
            case Align.bottomRight:
                widgetY -= getHeight();
                break;
        }
    
        temp.set(widgetX, widgetY);
        actor.localToStageCoordinates(temp);
        setPosition(MathUtils.round(temp.x), MathUtils.round(temp.y));
    }
    
    public void moveToInsideStage() {
        if (getStage() != null) {
            if (getX() < 0) setX(0);
            else if (getX() + getWidth() > getStage().getWidth()) setX(getStage().getWidth() - getWidth());
            
            if (getY() < 0) setY(0);
            else if (getY() + getHeight() > getStage().getHeight()) setY(getStage().getHeight() - getHeight());
        }
    }
    
    private void resizeWindowWithinStage() {
        if (getWidth() > stage.getWidth()) {
            setWidth(stage.getWidth());
        }
    
        if (getHeight() > stage.getHeight()) {
            setHeight(stage.getHeight());
        }

        invalidateHierarchy();
        
        moveToInsideStage();
    }
    
    public boolean isOutsideStage() {
        return getX() < 0 || getX() + getWidth() > getStage().getWidth() || getY() < 0 || getY() + getHeight() > getStage().getHeight();
    }
    
    public void hide() {
        hide(fadeOut(.2f));
    }
    
    public void hide(Action action) {
        if (action == null) action = delay(0);
        if (!hidden) {
            hidden = true;
            stage.removeCaptureListener(hideListener);
            group.addAction(sequence(action, Actions.removeActor()));
            fire(new TableHiddenEvent());
            for (InputListener inputListener : keyInputListeners) {
                stage.removeListener(inputListener);
            }
            
            stage.removeListener(focusListener);
            if (previousKeyboardFocus != null && previousKeyboardFocus.getStage() == null) previousKeyboardFocus = null;
            Actor actor = stage.getKeyboardFocus();
            if (actor == null || actor.isDescendantOf(this)) stage.setKeyboardFocus(previousKeyboardFocus);
    
            if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor == null || actor.isDescendantOf(this)) stage.setScrollFocus(previousScrollFocus);
        }
    }
    
    public void show(Stage stage) {
        Action action = sequence(alpha(0), fadeIn(.2f));
        this.show(stage, action);
        for (InputListener inputListener : keyInputListeners) {
            stage.addListener(inputListener);
        }
    }
    
    public void show(Stage stage, Action action) {
        if (getStage() != stage) {
            hidden = false;
            this.stage = stage;
            group = new WidgetGroup();
            group.setFillParent(true);
            group.setTouchable(Touchable.childrenOnly);
            stage.addActor(group);
            stage.addCaptureListener(hideListener);

            group.addActor(stageBackground);
            group.addActor(this);

            pack();

            if (keepSizedWithinStage) {
                resizeWindowWithinStage();
            }

            setPosition((int) (stage.getWidth() / 2f - getWidth() / 2f), (int) (stage.getHeight() / 2f - getHeight() / 2f));

            group.addAction(action);
            fire(new TableShownEvent());

            previousKeyboardFocus = null;
            Actor actor = stage.getKeyboardFocus();
            if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

            previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;
            stage.addListener(focusListener);
        }
    }
    
    private class HideListener extends InputListener {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            if (hideOnUnfocus) {
                Actor target = event.getTarget();
                if (isAscendantOf(target)) return false;
                hide();
            }
            return false;
        }
    }
    
    public static class PopTableStyle {
        /*Optional*/
        public Drawable background, stageBackground;
        
        public PopTableStyle() {
        
        }
        
        public PopTableStyle(PopTableStyle style) {
            background = style.background;
            stageBackground = style.stageBackground;
        }
    }
    
    public static class TableShownEvent extends Event {
    
    }
    
    public static class TableHiddenEvent extends Event {
    
    }
    
    public static abstract class TableShowHideListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof TableHiddenEvent) {
                tableHidden(event);
                return true;
            } else if (event instanceof TableShownEvent) {
                tableShown(event);
                return true;
            } else {
                return false;
            }
        }
        
        public abstract void tableShown(Event event);
        public abstract void tableHidden(Event event);
    }
    
    public boolean isHideOnUnfocus() {
        return hideOnUnfocus;
    }
    
    public void setHideOnUnfocus(boolean hideOnUnfocus) {
        this.hideOnUnfocus = hideOnUnfocus;
    }
    
    public int getAttachEdge() {
        return attachEdge;
    }
    
    public int getAttachAlign() {
        return attachAlign;
    }
    
    public boolean isKeepSizedWithinStage() {
        return keepSizedWithinStage;
    }
    
    public void setKeepSizedWithinStage(boolean keepSizedWithinStage) {
        this.keepSizedWithinStage = keepSizedWithinStage;
    }
    
    public boolean isAutomaticallyResized() {
        return automaticallyResized;
    }
    
    public void setAutomaticallyResized(boolean automaticallyResized) {
        this.automaticallyResized = automaticallyResized;
    }
    
    public boolean isKeepCenteredInWindow() {
        return keepCenteredInWindow;
    }
    
    public void setKeepCenteredInWindow(boolean keepCenteredInWindow) {
        this.keepCenteredInWindow = keepCenteredInWindow;
    }
    
    public Actor getAttachToActor() {
        return attachToActor;
    }
    
    public void attachToActor() {
        attachToActor(attachToActor, attachEdge, attachAlign);
    }
    
    public void attachToActor(Actor attachToActor) {
        attachToActor(attachToActor, attachEdge, attachAlign);
    }
    
    public void attachToActor(Actor attachToActor, int edge, int align) {
        alignToActorEdge(attachToActor, edge, align);
        this.attachToActor = attachToActor;
        this.attachEdge = edge;
        this.attachAlign = align;
    }
    
    public boolean isModal() {
        return modal;
    }
    
    public void setModal(boolean modal) {
        this.modal = modal;
        stageBackground.setTouchable(modal ? Touchable.enabled : Touchable.disabled);
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public PopTableStyle getStyle() {
        return style;
    }
    
    @Override
    public void layout() {
        if (keepCenteredInWindow) {
            float x = getStage().getWidth() / 2f;
            float y = getStage().getHeight() / 2f;
            setPosition(x, y, Align.center);
            setPosition(MathUtils.floor(getX()), MathUtils.floor(getY()));
        }
        
        if (automaticallyResized) {
            float centerX = getX(Align.center);
            float centerY = getY(Align.center);
            pack();
            setPosition(centerX, centerY, Align.center);
            setPosition(MathUtils.floor(getX()), MathUtils.floor(getY()));
        }
        
        if (attachToActor != null && attachToActor.getStage() != null) {
            alignToActorEdge(attachToActor, attachEdge, attachAlign);
        }
        
        if (keepSizedWithinStage) {
            resizeWindowWithinStage();
        }
        super.layout();
    }
    
    public PopTable key(final int key, final KeyListener keyListener) {
        keyInputListeners.add(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == key) {
                    keyListener.keyed();
                    return true;
                } else return super.keyDown(event, keycode);
            }
        });
        return this;
    }
    
    public interface KeyListener {
        void keyed();
    }
}