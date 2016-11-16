package com.tcg.firstpersontest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
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
    Model model, wallModel;
    ModelInstance modelInstance, wallModelInstance, wallModelInstance1, wallModelInstance2, wallModelInstance3, wallModelInstance4, wallModelInstance5;
    AssetManager assetManager;
    ModelInstance skyBox;
    float width, height, centerX, centerY;
    boolean centerMouse;
    float velY;
    Texture img, img32;
    boolean loading;

	@Override
	public void create () {

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1));
        environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, -.8f, -.2f));
//        environment.add(new PointLight().set(Color.WHITE, 0f, 25f, 0f, 500f));

        modelBatch = new ModelBatch();
        img = new Texture("badlogic.jpg");
        img32 = new Texture("32.png");

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 500f;
        camera.update();

        ModelBuilder modelBuilder = new ModelBuilder();
//        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(TextureAttribute.createDiffuse(img32), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        wallModel = modelBuilder.createBox(50f, .1f, 50f, new Material(TextureAttribute.createDiffuse(img), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(0, 2.5f, 0);
        wallModelInstance = new ModelInstance(wallModel);
        wallModelInstance1 = new ModelInstance(wallModel);
        wallModelInstance1.transform.setToRotation(Vector3.X, 90);
        wallModelInstance1.transform.setToRotation(Vector3.Z, 90);
        wallModelInstance1.transform.trn(25, 25, 0);
        wallModelInstance1.calculateTransforms();
        wallModelInstance2 = new ModelInstance(wallModel);
        wallModelInstance2.transform.setToRotation(Vector3.X, 90);
        wallModelInstance2.transform.setToRotation(Vector3.Z, 90);
        wallModelInstance2.transform.trn(-25, 25, 0);
        wallModelInstance2.calculateTransforms();
        wallModelInstance3 = new ModelInstance(wallModel);
        wallModelInstance3.transform.setToRotation(Vector3.X, 90);
        wallModelInstance3.transform.rotate(Vector3.Y, 90);
        wallModelInstance3.transform.trn(0, 25, 25);
        wallModelInstance3.calculateTransforms();
        wallModelInstance4 = new ModelInstance(wallModel);
        wallModelInstance4.transform.setToRotation(Vector3.X, 90);
        wallModelInstance4.transform.rotate(Vector3.Y, 90);
        wallModelInstance4.transform.trn(0, 25, -25);
        wallModelInstance4.calculateTransforms();
        wallModelInstance5 = new ModelInstance(wallModel);
        wallModelInstance5.transform.trn(0, 50, 0);
        wallModelInstance5.calculateTransforms();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        centerX = width * .5f;
        centerY = height * .5f;

        centerMouse = true;
        Gdx.input.setCursorCatched(centerMouse);
        Gdx.input.setCursorPosition((int) centerX, (int) centerY);

        assetManager = new AssetManager();
        assetManager.load("data/starskybox.g3db", Model.class);

        loading = true;

        velY = 0;

	}

	private void load() {

        skyBox = new ModelInstance(assetManager.get("data/starskybox.g3db", Model.class));

        loading = false;

    }

	@Override
	public void render () {
        if(loading && assetManager.update()) load();
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
        if(camera.position.y > 7) {
            velY -= .25f;
        } else if(camera.position.y <= 7) {
            velY = 0;
            camera.position.y = 7;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && camera.position.y <= 7) {
            velY = 3;
        }
        camera.translate(new Vector3(0, velY, 0));
//        Gdx.graphics.setTitle(camera.position.toString());
        if(camera.position.x  > 23f) {
            camera.position.x = 23f;
        }
        if(camera.position.x < -23f) {
            camera.position.x = -23f;
        }
        if(camera.position.z  > 23f) {
            camera.position.z = 23f;
        }
        if(camera.position.z < -23f) {
            camera.position.z = -23f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            camera.position.y -= 1;
        }
        camera.update();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            centerMouse = !centerMouse;
            Gdx.input.setCursorPosition((int) centerX, (int) centerY);
            Gdx.input.setCursorCatched(centerMouse);
        }

        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.render(wallModelInstance, environment);
//        modelBatch.render(wallModelInstance1, environment);
//        modelBatch.render(wallModelInstance2, environment);
//        modelBatch.render(wallModelInstance3, environment);
//        modelBatch.render(wallModelInstance4, environment);
//        modelBatch.render(wallModelInstance5, environment);
        if(skyBox != null) modelBatch.render(skyBox);
        modelBatch.end();
	}
	
	@Override
	public void dispose () {
        model.dispose();
        wallModel.dispose();
        modelBatch.dispose();
        img.dispose();
	}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
