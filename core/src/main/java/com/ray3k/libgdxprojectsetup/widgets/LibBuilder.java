package com.ray3k.libgdxprojectsetup.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.ray3k.libgdxprojectsetup.widgets.PopTable.PopTableStyle;

public class LibBuilder extends VerticalGroup {
    private OrderedMap<String, Array<String>> libraries;
    private OrderedMap<String, Array<LibOption>> options;
    private LibBuilderStyle style;
    private PopTableStyle popTableStyle;
    private PopTableStyle tooltipStyle;
    private PopTableStyle popTooltipStyle;
    public float libraryMaxHeight = 300f;
    public float librarySpacing = 5f;
    public float categorySpacing = 20f;
    private boolean allowAllCheckboxModification = true;
    
    public LibBuilder(Skin skin) {
        this(skin.get(LibBuilderStyle.class));
    }
    
    public LibBuilder(Skin skin, String style) {
        this(skin.get(style, LibBuilderStyle.class));
    }
    
    public LibBuilder(LibBuilderStyle style) {
        libraries = new OrderedMap<>();
        options = new OrderedMap<>();
        this.style = style;
        
        popTableStyle = new PopTableStyle();
        popTableStyle.background = style.popBackground;
        popTableStyle.stageBackground = style.popStageBackground;

        tooltipStyle = new PopTableStyle();
        tooltipStyle.background = style.tooltipBackground;

        popTooltipStyle = new PopTableStyle();
        popTooltipStyle.background = style.popTooltipBackground;
        
        space(categorySpacing);
        wrapSpace(categorySpacing);
        wrap();
        columnAlign(Align.topLeft);
        
        populate();
    }
    
    public void addLibrary(String category, String library) {
        Array<String> values = libraries.get(category);
        if (values == null) {
            values = new Array<>();
            libraries.put(category, values);
        }
        values.add(library);

        populate();
    }
    
    public void removeLibrary(String category, String library) {
        Array<String> values = libraries.get(category);
        values.removeValue(library, false);
        populate();
    }
    
    public void addOption(String category, String library, String description) {
        Array<LibOption> values = options.get(category);
        if (values == null) {
            values = new Array<>();
            options.put(category, values);
        }
        LibOption libOption = new LibOption(library, description);
        values.add(libOption);
    }
    
    public void populate() {
        clear();
        Array<String> keys = libraries.keys().toArray();
        for (String key : keys) {
            Table table = new Table();
            addActor(table);
        
            table.defaults().left();
            Label label = new Label(key, style.category);
            table.add(label);
        
            table.row();
            VerticalGroup verticalGroup = new VerticalGroup();
            verticalGroup.wrapSpace(librarySpacing);
            verticalGroup.columnAlign(Align.topLeft);
            
            Array<String> values = libraries.get(key);
            for (String value : values) {
                ImageTextButton imageTextButton = new ImageTextButton(value, style.delete);
                verticalGroup.addActor(imageTextButton);
                String description = getDescription(key, value);
                OptionHoverListener optionHoverListener = new OptionHoverListener(description, tooltipStyle);
                if (description != null) {
                    imageTextButton.addListener(optionHoverListener);
                }
                imageTextButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        removeLibrary(key, value);
                    }
                });
            }
            verticalGroup.pack();
            if (verticalGroup.getHeight() > libraryMaxHeight) {
                verticalGroup.wrap();
                table.add(verticalGroup).minHeight(libraryMaxHeight);
            } else {
                table.add(verticalGroup);
            }
            
            table.row();
            Button button = new Button(style.add);
            table.add(button);

            PopTable pop = new PopTable(popTableStyle);
            pop.setModal(true);
            pop.setHideOnUnfocus(true);
            
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    pop.show(getStage());
                }
            });
            
            label = new Label(key, style.category);
            pop.add(label);
            
            pop.row();
            pop.defaults().left();
            Array<CheckBox> checkBoxes = new Array<>();
            CheckBox allCheckBox = new CheckBox("All", style.listItem);
            if (options.get(key) != null) for (LibOption option : options.get(key)) {
                CheckBox checkBox = new CheckBox(option.name, style.listItem);
                checkBoxes.add(checkBox);
                checkBox.setChecked(values.contains(option.name, false));
                checkBox.addListener(new OptionHoverListener(option.description, popTooltipStyle));
                pop.add(checkBox);
                pop.row();
                checkBox.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (checkBox.isChecked()) addLibrary(key, option.name);
                        else removeLibrary(key, option.name);

                        if (allowAllCheckboxModification) {
                            boolean allChecked = true;
                            for (CheckBox child : checkBoxes) {
                                if (!child.isChecked()) {
                                    allChecked = false;
                                    break;
                                }
                            }
                            allCheckBox.setChecked(allChecked);
                        }
                    }
                });
            }
            
            boolean allChecked = true;
            for (CheckBox child : checkBoxes) {
                if (!child.isChecked()) {
                    allChecked = false;
                    break;
                }
            }
            allCheckBox.setProgrammaticChangeEvents(false);
            allCheckBox.setChecked(allChecked);
            pop.add(allCheckBox);
            allCheckBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    allowAllCheckboxModification = false;
                    for (int i = 0; i < checkBoxes.size; i++) {
                        CheckBox checkBox = checkBoxes.get(i);
                        checkBox.setChecked(allCheckBox.isChecked());
                    }
                    allowAllCheckboxModification = true;
                }
            });
        }
    }

    private String getDescription(String category, String library) {
        Array<LibOption> options = this.options.get(category);
        if (options != null) for (LibOption option : options) {
            if (option.name.equals(library)) {
                return option.description;
            }
        }
        return null;
    }

    private static class LibOption {
        public String name;
        public String description;

        public LibOption(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    private class OptionHoverListener extends PopTableTooltipListener {
        public OptionHoverListener(String description, PopTableStyle popTableStyle) {
            super(Align.topRight, popTableStyle);

            Table table = getPopTable();

            Label label = new Label(description, style.tooltipLabel);
            label.pack();
            float prefWidth = label.getWidth() > 300 ? 300 : label.getWidth();
            label.setWrap(true);
            table.add(label).growX().prefWidth(prefWidth);
        }
    }
    
    public static class LibBuilderStyle {
        //required
        public LabelStyle category, tooltipLabel;
        public ImageTextButtonStyle delete, check;
        public CheckBoxStyle listItem;
        public ButtonStyle add;
        public Drawable popBackground, popStageBackground, tooltipBackground, popTooltipBackground;
        
        public LibBuilderStyle() {
        
        }
        
        public LibBuilderStyle(LibBuilderStyle style) {
            this.category = style.category;
            this.tooltipLabel = style.tooltipLabel;
            this.delete = style.delete;
            this.check = style.check;
            this.listItem = style.listItem;
            this.add = style.add;
            this.popBackground = style.popBackground;
            this.popStageBackground = style.popStageBackground;
            this.tooltipBackground = style.tooltipBackground;
            this.popTooltipBackground = style.popTooltipBackground;
        }
    }
}
