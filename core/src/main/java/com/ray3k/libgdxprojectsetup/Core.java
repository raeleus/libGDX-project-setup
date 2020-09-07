package com.ray3k.libgdxprojectsetup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.libgdxprojectsetup.tables.LandingTable;
import com.ray3k.libgdxprojectsetup.tables.WizardProgress.ProgressGroupStyle;
import com.ray3k.libgdxprojectsetup.tables.ProjectTable;

public class Core extends ApplicationAdapter {
	public static Stage stage;
	public static Skin skin;
	public static Table root;
	public static LandingTable landingTable;
	public static ProjectTable projectTable;
	
	@Override
	public void create() {
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("skin/skin.json"));
		
		ProgressGroupStyle progressGroupStyle = new ProgressGroupStyle(skin.getDrawable("progress-back-10"), skin.get("progress",
				ButtonStyle.class), skin.get("progress", LabelStyle.class));
		skin.add("default", progressGroupStyle);
		
		root = new Table();
		root.setBackground(skin.getDrawable("bg-10"));
		root.setFillParent(true);
		stage.addActor(root);
		
		landingTable = new LandingTable();
		projectTable = new ProjectTable();
		root.add(projectTable).minSize(600, 530);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
		if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			dispose();
			create();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void dispose() {
		skin.dispose();
	}
}