package com.tcg.firstpersontest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter {

    PerspectiveCamera camera;
    Environment environment;
    ModelBatch modelBatch;
    Model model;
    ModelInstance modelInstance;
    float width, height, centerX, centerY;
    boolean centerMouse;
    float velY;
    SpriteBatch spriteBatch;
    Texture img;

	@Override
	public void create () {

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1));
        environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, -.8f, -.2f));

        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
        modelInstance = new ModelInstance(model);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        centerX = width * .5f;
        centerY = height * .5f;

        centerMouse = true;
        Gdx.input.setCursorCatched(centerMouse);

        velY = 0;

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        centerX = width * .5f;
        centerY = height * .5f;

        float dX = 0;
        float dY = 0;
        if(centerMouse) {
            dX = MathUtils.floor(centerX - Gdx.input.getX());
            dY = MathUtils.floor(centerY - Gdx.input.getY());
            Gdx.input.setCursorPosition((int) centerX, (int) centerY);
            camera.rotate(camera.direction.cpy().crs(Vector3.Y), dY);
            camera.rotate(Vector3.Y, dX);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            camera.translate(v);
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.x = -v.x;
            v.z = -v.z;
            camera.translate(v);
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, -90);
            camera.translate(v);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, 90);
            camera.translate(v);
        }
        if(camera.position.y > 5) {
            velY -= .25f;
        } else if(camera.position.y <= 5) {
            velY = 0;
            camera.position.y = 5;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velY = 3;
        }
        camera.translate(new Vector3(0, velY, 0));
        Gdx.graphics.setTitle(camera.direction.toString());
        camera.update();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            centerMouse = !centerMouse;
            Gdx.input.setCursorPosition((int) centerX, (int) centerY);
            Gdx.input.setCursorCatched(centerMouse);
        }

//        camera.rotate(Vector3.Z, 1);
//        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.draw(img, 0, 0, img.getWidth() * .5f, img.getHeight() * .5f);
        spriteBatch.end();
	}
	
	@Override
	public void dispose () {
        model.dispose();
        modelBatch.dispose();
        img.dispose();
        spriteBatch.dispose();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
