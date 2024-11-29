package com.angrybirds.com;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;
public class GameScreen implements Screen, InputProcessor, ContactListener {
    // Add these constants after the other class-level constants
    private static final float PPM = 100f; // Pixels Per Meter - for Box2D physics scaling
    private static final float BLOCK_WIDTH = 10f; // Width of blocks in pixels
    private static final float BLOCK_HEIGHT = 10f; // Height of blocks in pixels
    private static final float PIG_RADIUS = 25f; // Radius of pig collision circle in pixels;
    private Main game;
    private SpriteBatch batch;
    private Stage stage;
    private ImageButton pauseButton;
    private Texture pauseButtonTexture;
    private Texture gameScreenTexture;
    private World world;
    private BitmapFont font;
    private Box2DDebugRenderer debugRenderer;
    private static final float TIME_STEP = 1/60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private static final float PIXELS_TO_METERS = 100f;
    private Array<Body> bodiesToDestroy = new Array<>();
    private Array<Pig> pigsToRemove = new Array<>();
    private OrthographicCamera camera;
    private Viewport viewport;
    private Array<Body> bodiesToMakeActive;
    private Array<Block> blocksToActivate;
    public static final float VIRTUAL_WIDTH = 1280;
    public static final float VIRTUAL_HEIGHT = 720;
    private Catapult catapult;
    private RedBird redBird;
    private BlueBird blueBird;
    private YellowBird yellowBird;
    private Bird currentBird;
    private List<BlockPiece> blockPieces;
    private boolean showTrajectory = true;
    private ShapeRenderer shapeRenderer;
    private static final int TRAJECTORY_POINTS = 12;
    private static final float TRAJECTORY_VELOCITY_SCALE = 0.1f;
    private static final float TIME_STEP_TRAJECTORY = 0.1f;
    private static final float DOT_SIZE = 5f;
    private ArrayList<Pig> pigs;
    private BlackBird blackBird;
    private boolean isScreenShaking = false;
    private float shakeTime = 0f;
    private static final float SHAKE_DURATION = 0.5f;
    private static final float SHAKE_INTENSITY = 5f;
    private int score = 0;
    // Should typically be:

    private ProjectileEquation projectileEquation;
    private WoodBlock woodBlock;
    private GlassBlock glassBlock;
    private StoneBlock stoneBlock;
    private Array<Pig> pigsToDestroy = new Array<>(); // Add this as a class field
    private SmallPig smallPig;
    private SmallPig smallPig2;
    private SmallPig smallPig3;
    private MediumPig mediumPig;
    private MediumPig mediumPig3;
    private MediumPig mediumPig2;
    private YellowBird yellowBird3;
    private SmallPig smallPig4, smallPig5;
    private MediumPig mediumPig4, mediumPig5;
    private Array<Vector2> trajectoryDots;
    private static final int NUM_TRAJECTORY_DOTS = 8;
    private static final float TRAJECTORY_DOT_SIZE = 5f;
    private static final Color TRAJECTORY_DOT_COLOR = new Color(1, 1, 1, 0.7f);
    private static final float RIGHT_WALL_X = VIRTUAL_WIDTH / PIXELS_TO_METERS;
    private static final float RIGHT_WALL_HEIGHT = VIRTUAL_HEIGHT / PIXELS_TO_METERS;
    private static final float WALL_THICKNESS = 0.05f; // thickness in meters
    private boolean isDragging = false;

    private Vector2 dragStart = new Vector2();
    private Vector2 dragCurrent = new Vector2();
    private final float MAX_DRAG_DISTANCE = 150f;
    private final Vector2 SLINGSHOT_POSITION = new Vector2();
    private static final float LAUNCH_SPEED_MULTIPLIER = 2.5f;
    private static final int TRAJECTORY_DOTS = 20;
    private static  final float DOT_RADIUS = 3f;
    private static final float TRAJECTORY_TIME_STEP = 0.3f; // Increased time step for more spread out dots
    private static final float BIRD_SWITCH_DELAY = 3f;
    private float birdSwitchTimer = 3;
    private boolean shouldSwitchBird = false;
    private boolean hasCollided = false;
    private static final float MIN_VELOCITY_THRESHOLD = 0.05f;
    private static final float CHECK_VELOCITY_DELAY =0.51f;
    private float velocityCheckTimer = 0;
    private static final float SCREEN_BOUNDARY_X = VIRTUAL_WIDTH * 1.5f;  // Boundary beyond screen width
    private static final float SCREEN_BOUNDARY_Y = VIRTUAL_HEIGHT * 1.5f; // Boundary beyond screen height
    private boolean birdLeftScreen = false;
    private Array<Vector2> trajectoryPoints;
    public Array<Block> blocks;
    private Array<Block> blocksToRemove;
    private Array<Bird> birdsToRemove;
    private int currentLevel;
    private Bird[] additionalBirds;
    private RedBird redBird2, redBird3;
    private BlueBird blueBird2;
    private YellowBird yellowBird2;
    public float[] blockPositions;

    private class BlockPiece {
        Body body;
        Texture texture;
        float width, height;
        float rotation;
        public BlockPiece(World world, float x, float y, float width, float height, Texture texture) {
            this.width = width;
            this.height = height;
            this.texture = texture;

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);
            body = world.createBody(bodyDef);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / (2 * PIXELS_TO_METERS), height / (2 * PIXELS_TO_METERS));

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.1f;
            body.createFixture(fixtureDef);
            shape.dispose();
            body.setTransform(body.getPosition(), MathUtils.random(-MathUtils.PI, MathUtils.PI));
            body.applyLinearImpulse(
                MathUtils.random(-1f, 1f),
                MathUtils.random(0f, 2f),
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
            );
        }
        public void render(SpriteBatch batch) {
            Vector2 position = body.getPosition();
            float angle = body.getAngle() * MathUtils.radiansToDegrees;
            batch.draw(texture,
                position.x * PIXELS_TO_METERS - width/2,
                position.y * PIXELS_TO_METERS - height/2,
                width/2, height/2,
                width, height,
                1, 1,
                angle,
                0, 0,
                texture.getWidth(), texture.getHeight(),
                false, false);
        }
    }
    public GameScreen(Main game, int level) {

        this.game = game;
        this.currentLevel = level;
        this.batch = game.getBatch();
        projectileEquation = new ProjectileEquation();
        projectileEquation.gravity = -9.81f * PIXELS_TO_METERS;
        trajectoryDots = new Array<>(NUM_TRAJECTORY_DOTS);
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        bodiesToMakeActive = new Array<>();
        blocksToActivate = new Array<>();
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(this);
        debugRenderer = new Box2DDebugRenderer();
        createGround();
        stage = new Stage(viewport, batch);
        blockPieces = new ArrayList<>();
        pigs = new ArrayList<Pig>();

        shapeRenderer = new ShapeRenderer();
        trajectoryPoints = new Array<>(TRAJECTORY_POINTS);
        blocks = new Array<>();
        blocksToRemove = new Array<>();
        birdsToRemove = new Array<>();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        createRightBoundary();
        loadTextures();
        initializeGameObjects();
        setupButtons();
    }

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;  // Make ground static
        groundBodyDef.position.set(VIRTUAL_WIDTH / (2 * PIXELS_TO_METERS),
            (VIRTUAL_HEIGHT * 0.24f) / PIXELS_TO_METERS);  // Adjust ground position
        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(VIRTUAL_WIDTH / (2 * PIXELS_TO_METERS), 10 / PIXELS_TO_METERS);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }


    private void loadTextures() {
        gameScreenTexture = new Texture("play_game.png");
        gameScreenTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        pauseButtonTexture = new Texture("pause_button.png");
        pauseButtonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    private void initializeGameObjects() {
        float catapultX = VIRTUAL_WIDTH * 0.15f;
        float catapultY = VIRTUAL_HEIGHT * 0.24f;
        catapult = new Catapult(catapultX, catapultY);
        SLINGSHOT_POSITION.set(
            catapultX + catapult.getWidth() * 0.3f,
            catapultY + catapult.getHeight() * 0.7f
        );

        if (currentLevel == 1) {
            initializeLevel1();
        } else if (currentLevel == 2) {
            initializeLevel2();
        }
        else if (currentLevel == 3) {
            initializeLevel3();
        }

    }
    private void initializeLevel1(){
        redBird = new RedBird(world, SLINGSHOT_POSITION.x+50, SLINGSHOT_POSITION.y+20);
        redBird.getBody().setType(BodyDef.BodyType.StaticBody); // Keep it static until launch
        float birdWaitingX = SLINGSHOT_POSITION.x - 50;
        float birdWaitingY = SLINGSHOT_POSITION.y-80;
        blueBird = new BlueBird(world, birdWaitingX - 50, birdWaitingY);
        blueBird.getBody().setType(BodyDef.BodyType.StaticBody);
        yellowBird = new YellowBird(world, birdWaitingX - 100, birdWaitingY);
        yellowBird.getBody().setType(BodyDef.BodyType.StaticBody);
        blackBird = new BlackBird(world, birdWaitingX - 140, birdWaitingY);
        blackBird.getBody().setType(BodyDef.BodyType.StaticBody);



        currentBird = redBird;
        float blockBaseY = VIRTUAL_HEIGHT * 0.24f;  // Ground level
        float structureX = VIRTUAL_WIDTH * 0.75f; // Right side of screen
        float groundLevel = VIRTUAL_HEIGHT * 0.24f; // Ground level
        woodBlock = new WoodBlock(world, structureX-20, blockBaseY + 90);
        woodBlock.getBody().setType(BodyDef.BodyType.StaticBody); // Keep static until hit
        glassBlock = new GlassBlock(world, structureX + 200, blockBaseY + 100);
        glassBlock.getBody().setType(BodyDef.BodyType.StaticBody);
        stoneBlock = new StoneBlock(world, structureX+80 , blockBaseY + 200);
        stoneBlock.getBody().setType(BodyDef.BodyType.StaticBody);
        blocks.clear();
        blocks.add(woodBlock);
        blocks.add(glassBlock);
        blocks.add(stoneBlock);
        smallPig = new SmallPig(world, structureX + 30, blockBaseY + 5);
        smallPig.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig = new MediumPig(world, structureX + 90, blockBaseY + 5);
        mediumPig.getBody().setType(BodyDef.BodyType.DynamicBody);
    }

    private void initializeLevel2(){
        redBird = new RedBird(world, SLINGSHOT_POSITION.x+50, SLINGSHOT_POSITION.y+20);
        redBird.getBody().setType(BodyDef.BodyType.StaticBody);

        float birdWaitingX = SLINGSHOT_POSITION.x - 50;
        float birdWaitingY = SLINGSHOT_POSITION.y-80;

        blueBird = new BlueBird(world, birdWaitingX - 50, birdWaitingY);
        blueBird.getBody().setType(BodyDef.BodyType.StaticBody);

        yellowBird = new YellowBird(world, birdWaitingX - 100, birdWaitingY);
        yellowBird.getBody().setType(BodyDef.BodyType.StaticBody);

        // Additional birds for Level 2
        blueBird2 = new BlueBird(world, birdWaitingX - 150, birdWaitingY);
        blueBird2.getBody().setType(BodyDef.BodyType.StaticBody);

        yellowBird2 = new YellowBird(world, birdWaitingX - 200, birdWaitingY);
        yellowBird2.getBody().setType(BodyDef.BodyType.StaticBody);

        currentBird = redBird;

        // Level 2 structure with more blocks and pigs
        float blockBaseY = VIRTUAL_HEIGHT * 0.24f;
        float structureX = VIRTUAL_WIDTH * 0.75f;

        // First tower
        woodBlock = new WoodBlock(world, structureX-20, blockBaseY + 90);
        woodBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        WoodBlock woodBlock2 = new WoodBlock(world, structureX-20, blockBaseY + 290);
        woodBlock2.getBody().setType(BodyDef.BodyType.StaticBody);

        // Second tower
        glassBlock = new GlassBlock(world, structureX + 200, blockBaseY + 100);
        glassBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        GlassBlock glassBlock2 = new GlassBlock(world, structureX + 200, blockBaseY + 300);
        glassBlock2.getBody().setType(BodyDef.BodyType.StaticBody);

        // Third tower
        stoneBlock = new StoneBlock(world, structureX+80, blockBaseY + 200);
        stoneBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        StoneBlock stoneBlock2 = new StoneBlock(world, structureX+80, blockBaseY + 400);
        stoneBlock2.getBody().setType(BodyDef.BodyType.StaticBody);

        blocks.clear();
        blocks.add(woodBlock);
        blocks.add(woodBlock2);
        blocks.add(glassBlock);
        blocks.add(glassBlock2);
        blocks.add(stoneBlock);
        blocks.add(stoneBlock2);

        // Level 2 pigs
        smallPig = new SmallPig(world, structureX + 30, blockBaseY + 5);
        smallPig.getBody().setType(BodyDef.BodyType.DynamicBody);

        smallPig2 = new SmallPig(world, structureX + 30, blockBaseY + 180);
        smallPig2.getBody().setType(BodyDef.BodyType.DynamicBody);

        smallPig3 = new SmallPig(world, structureX + 30, blockBaseY + 5);
        smallPig3.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig = new MediumPig(world, structureX +90, blockBaseY + 5);
        mediumPig.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig2 = new MediumPig(world, structureX + 90, blockBaseY + 180);
        mediumPig2.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig3 = new MediumPig(world, structureX + 90, blockBaseY + 5);
        mediumPig3.getBody().setType(BodyDef.BodyType.DynamicBody);




    }
    private void initializeLevel3() {
        // Initial bird setup
        redBird = new RedBird(world, SLINGSHOT_POSITION.x+50, SLINGSHOT_POSITION.y+20);
        redBird.getBody().setType(BodyDef.BodyType.StaticBody);

        float birdWaitingX = SLINGSHOT_POSITION.x - 50;
        float birdWaitingY = SLINGSHOT_POSITION.y-80;

        // Setup all birds
        blueBird = new BlueBird(world, birdWaitingX - 50, birdWaitingY);
        blueBird.getBody().setType(BodyDef.BodyType.StaticBody);

        yellowBird = new YellowBird(world, birdWaitingX - 100, birdWaitingY);
        yellowBird.getBody().setType(BodyDef.BodyType.StaticBody);


        // Additional birds for Level 2
        blueBird2 = new BlueBird(world, birdWaitingX - 150, birdWaitingY);
        blueBird2.getBody().setType(BodyDef.BodyType.StaticBody);

        yellowBird2 = new YellowBird(world, birdWaitingX - 200, birdWaitingY);
        yellowBird2.getBody().setType(BodyDef.BodyType.StaticBody);

        // Additional birds for Level 3
        redBird2 = new RedBird(world, birdWaitingX - 250, birdWaitingY);
        redBird2.getBody().setType(BodyDef.BodyType.StaticBody);

        yellowBird3 = new YellowBird(world, birdWaitingX - 300, birdWaitingY);
        yellowBird3.getBody().setType(BodyDef.BodyType.StaticBody);

        currentBird = redBird;
        // Level 3 structure setup
        float blockBaseY = VIRTUAL_HEIGHT * 0.24f;
        float structureX = VIRTUAL_WIDTH * 0.75f;

        // Setup blocks (same as Level 2)
        woodBlock = new WoodBlock(world, structureX-20, blockBaseY + 90);
        woodBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        WoodBlock woodBlock2 = new WoodBlock(world, structureX-20, blockBaseY + 290);
        woodBlock2.getBody().setType(BodyDef.BodyType.StaticBody);

        glassBlock = new GlassBlock(world, structureX + 200, blockBaseY + 100);
        glassBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        GlassBlock glassBlock2 = new GlassBlock(world, structureX + 200, blockBaseY + 300);
        glassBlock2.getBody().setType(BodyDef.BodyType.StaticBody);

        stoneBlock = new StoneBlock(world, structureX+80, blockBaseY + 200);
        stoneBlock.getBody().setType(BodyDef.BodyType.StaticBody);

        WoodBlock woodBlock3 = new WoodBlock(world, structureX-20, blockBaseY + 490);
        woodBlock3.getBody().setType(BodyDef.BodyType.StaticBody);

        StoneBlock stoneBlock2 = new StoneBlock(world, structureX+80, blockBaseY + 400);
        stoneBlock2.getBody().setType(BodyDef.BodyType.StaticBody);
        GlassBlock glassBlock3 = new GlassBlock(world, structureX + 200, blockBaseY + 500);
        glassBlock3.getBody().setType(BodyDef.BodyType.StaticBody);





        blocks.clear();
        blocks.add(woodBlock);
        blocks.add(woodBlock2);
        blocks.add(glassBlock);
        blocks.add(glassBlock2);
        blocks.add(stoneBlock);
        blocks.add(stoneBlock2);
        blocks.add(woodBlock3);
        blocks.add(glassBlock3);

        smallPig = new SmallPig(world, structureX + 30, blockBaseY + 5);
        smallPig.getBody().setType(BodyDef.BodyType.DynamicBody);

        smallPig2 = new SmallPig(world, structureX + 30, blockBaseY + 180);
        smallPig2.getBody().setType(BodyDef.BodyType.DynamicBody);

        smallPig3 = new SmallPig(world, structureX + 30, blockBaseY + 5);
        smallPig3.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig = new MediumPig(world, structureX +90, blockBaseY + 5);
        mediumPig.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig2 = new MediumPig(world, structureX + 90, blockBaseY + 180);
        mediumPig2.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig3 = new MediumPig(world, structureX + 90, blockBaseY + 5);
        mediumPig3.getBody().setType(BodyDef.BodyType.DynamicBody);
        // Initialize 10 pigs for Level 3

        smallPig4 = new SmallPig(world, structureX + 30, blockBaseY + 350);
        smallPig4.getBody().setType(BodyDef.BodyType.DynamicBody);
        smallPig5 = new SmallPig(world, structureX + 30, blockBaseY + 750);
        smallPig5.getBody().setType(BodyDef.BodyType.DynamicBody);


        mediumPig4 = new MediumPig(world, structureX + 90, blockBaseY + 350);
        mediumPig4.getBody().setType(BodyDef.BodyType.DynamicBody);

        mediumPig5 = new MediumPig(world, structureX + 90, blockBaseY + 750);
        mediumPig5.getBody().setType(BodyDef.BodyType.DynamicBody);

    }


    private void setupButtons() {
        Skin skin = new Skin();
        float buttonSize = VIRTUAL_HEIGHT * 0.1f;
        float buttonPadding = VIRTUAL_HEIGHT * 0.02f;

        skin.add("pauseButton", pauseButtonTexture);
        pauseButton = new ImageButton(skin.getDrawable("pauseButton"));
        pauseButton.setSize(buttonSize, buttonSize);
        pauseButton.setPosition(buttonPadding, VIRTUAL_HEIGHT - buttonSize - buttonPadding);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setCurrentGameScreen(GameScreen.this);
                game.setScreen(new PauseScreen(game, currentLevel));
            }
        });
        stage.addActor(pauseButton);
    }
    private void calculateTrajectoryDots(Vector2 launchVelocity) {
        if (trajectoryDots == null) {
            trajectoryDots = new Array<>(NUM_TRAJECTORY_DOTS);
        }
        trajectoryDots.clear();
        float x = SLINGSHOT_POSITION.x;
        float y = SLINGSHOT_POSITION.y;
        float vx = launchVelocity.x * LAUNCH_SPEED_MULTIPLIER / PIXELS_TO_METERS;
        float vy = launchVelocity.y * LAUNCH_SPEED_MULTIPLIER / PIXELS_TO_METERS;
        float timeStep = 0.1f;
        float gravity = -9.81f;
        for (int i = 0; i < NUM_TRAJECTORY_DOTS; i++) {
            float t = i * timeStep;
            float predictedX = x + vx * t * PIXELS_TO_METERS;
            float predictedY = y + (vy * t + 0.5f * gravity * t * t) * PIXELS_TO_METERS;
            trajectoryDots.add(new Vector2(predictedX, predictedY));
        }
    }
    @Override
    public void render(float delta) {
        if (smallPig != null && smallPig.shouldDestroy()) {
            smallPig.destroyBody();
            smallPig = null;
            checkPigStatus();
        }
        if (mediumPig != null && mediumPig.shouldDestroy()) {
            mediumPig.destroyBody();
            mediumPig = null;
            checkPigStatus();
        }
        if (currentBird != null && currentBird.isLaunched && !birdLeftScreen) {
            Vector2 birdPosition = currentBird.getPosition();
            if (birdPosition.x > SCREEN_BOUNDARY_X || birdPosition.x < -SCREEN_BOUNDARY_X ||
                birdPosition.y > SCREEN_BOUNDARY_Y || birdPosition.y < -SCREEN_BOUNDARY_Y) {
                birdLeftScreen = true;
                shouldSwitchBird = true;
                hasCollided = true;
                birdSwitchTimer = 0;
            }
            if (shouldSwitchBird) {
                birdSwitchTimer += delta;
                if (birdSwitchTimer >= BIRD_SWITCH_DELAY) {
                    switchToNextBird();
                    shouldSwitchBird = false;
                    birdSwitchTimer = 0;
                    hasCollided = false;
                    velocityCheckTimer = 0;
                }
            }
        }
        if (currentBird != null && currentBird.isLaunched) {
            if (!hasCollided) {
                velocityCheckTimer += delta;
                if (velocityCheckTimer >= CHECK_VELOCITY_DELAY) {
                    Vector2 velocity = currentBird.getBody().getLinearVelocity();
                    if (velocity.len() < MIN_VELOCITY_THRESHOLD) {
                        shouldSwitchBird = true;
                        hasCollided = true;
                    }
                    velocityCheckTimer = 0;
                }
            }
            if (shouldSwitchBird) {
                birdSwitchTimer += delta;
                if (birdSwitchTimer >= BIRD_SWITCH_DELAY) {
                    switchToNextBird();
                    shouldSwitchBird = false;
                    birdSwitchTimer = 0;
                    hasCollided = false;
                    birdLeftScreen = false;
                    velocityCheckTimer = 0;
                }
            }
        }
        if (currentBird == null && redBird == null && blueBird == null && yellowBird == null) {
            checkPigStatus();
        }

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        processDeferredCollisions();
        processDeferredDestructions();
        processPhysicsChanges();
        checkPigStatus();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        removeDestroyedObjects();
        batch.begin();

        batch.draw(gameScreenTexture, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        catapult.render(batch);
        if (redBird != null) redBird.render(batch);
        if (blueBird != null) blueBird.render(batch);
        if (yellowBird != null) yellowBird.render(batch);
        if (currentLevel == 1) {
            if (blackBird != null) blackBird.render(batch);
        }
        if (currentLevel == 2) {
            // Additional birds for level 2
            if (blueBird2 != null) blueBird2.render(batch);
            if (yellowBird2 != null) yellowBird2.render(batch);
        }

        if (currentLevel == 3) {
            if (blueBird2 != null) blueBird2.render(batch);
            if (yellowBird2 != null) yellowBird2.render(batch);
            if (redBird2 != null) redBird2.render(batch);
            if (yellowBird3 != null) yellowBird3.render(batch);
        }

        for (Block block : blocks) {
            block.render(batch);
        }
        for (BlockPiece piece : blockPieces) {
            piece.render(batch);
        }
        shakeScreen(delta);
        if (currentLevel == 1) {
            if (smallPig != null && smallPig.isAlive()) {
                Vector2 position = smallPig.getBody().getPosition();

                smallPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);

            }
            if (mediumPig != null && mediumPig.isAlive()) {
                Vector2 position = mediumPig.getBody().getPosition();
                mediumPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
        } else if (currentLevel == 2) {
            // Additional pigs for level 2
            if (smallPig != null && smallPig.isAlive()){
                Vector2 position = smallPig.getBody().getPosition();
                smallPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
            if (smallPig2 != null && smallPig2.isAlive()) {
                Vector2 position = smallPig2.getBody().getPosition();
                smallPig2.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
            if (smallPig3 != null && smallPig3.isAlive()) {
                Vector2 position = smallPig3.getBody().getPosition();
                smallPig3.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
            if (mediumPig != null && mediumPig.isAlive()) {
                Vector2 position = mediumPig.getBody().getPosition();
                mediumPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
            if (mediumPig2 != null && mediumPig2.isAlive()) {
                Vector2 position = mediumPig2.getBody().getPosition();
                mediumPig2.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
            if (mediumPig3 != null && mediumPig3.isAlive()) {
                Vector2 position = mediumPig3.getBody().getPosition();
                mediumPig3.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            };
        }

        if (currentLevel == 3) {
            // Small pigs
            if (smallPig != null && smallPig.isAlive()) {
                Vector2 position = smallPig.getBody().getPosition();
                smallPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (smallPig2 != null && smallPig2.isAlive()) {
                Vector2 position = smallPig2.getBody().getPosition();
                smallPig2.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (smallPig3 != null && smallPig3.isAlive()) {
                Vector2 position = smallPig3.getBody().getPosition();
                smallPig3.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (smallPig4 != null && smallPig4.isAlive()) {
                Vector2 position = smallPig4.getBody().getPosition();
                smallPig4.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (smallPig5 != null && smallPig5.isAlive()) {
                Vector2 position = smallPig5.getBody().getPosition();
                smallPig5.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            // Medium pigs
            if (mediumPig != null && mediumPig.isAlive()) {
                Vector2 position = mediumPig.getBody().getPosition();
                mediumPig.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (mediumPig2 != null && mediumPig2.isAlive()) {
                Vector2 position = mediumPig2.getBody().getPosition();
                mediumPig2.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (mediumPig3 != null && mediumPig3.isAlive()) {
                Vector2 position = mediumPig3.getBody().getPosition();
                mediumPig3.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (mediumPig4 != null && mediumPig4.isAlive()) {
                Vector2 position = mediumPig4.getBody().getPosition();
                mediumPig4.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
            if (mediumPig5 != null && mediumPig5.isAlive()) {
                Vector2 position = mediumPig5.getBody().getPosition();
                mediumPig5.render(batch, position.x * PIXELS_TO_METERS, position.y * PIXELS_TO_METERS);
            }
        }
        font.draw(batch," "+ score, VIRTUAL_WIDTH - 120, VIRTUAL_HEIGHT - 20);
        batch.end();
        if (isDragging && showTrajectory) {
            renderTrajectory();
        }
        cleanupDeadPigs();
//        debugRenderer.render(world, camera.combined.cpy().scl(PIXELS_TO_METERS));
        stage.act(delta);
        stage.draw();

    }
    private void savePigStates(GameState gameState){

        gameState.pigStates.clear();
        if (smallPig != null && smallPig.isAlive()) {
            addPigToState(gameState,smallPig, "SMALL", 1,smallPig.getHitCount());
        }
        if (smallPig2 != null && smallPig2.isAlive()) {
            addPigToState(gameState,smallPig2, "SMALL", 2,smallPig2.getHitCount());
        }
        if (smallPig3 != null && smallPig3.isAlive()) {
            addPigToState(gameState,smallPig3, "SMALL", 3,smallPig3.getHitCount());
        }
        if (smallPig4 != null && smallPig4.isAlive()) {
            addPigToState(gameState,smallPig4, "SMALL", 4,smallPig4.getHitCount());
        }
        if (smallPig5 != null && smallPig5.isAlive()) {
            addPigToState(gameState,smallPig5, "SMALL", 5,smallPig5.getHitCount());
        }
        if (mediumPig != null && mediumPig.isAlive()) {
            addPigToState(gameState,mediumPig, "MEDIUM", 1,mediumPig.getHitCount());
        }
        if (mediumPig2 != null && mediumPig2.isAlive() ){
            addPigToState(gameState,mediumPig2, "MEDIUM", 2,mediumPig2.getHitCount());
        }
        if (mediumPig3 != null&& mediumPig3.isAlive() ){
            addPigToState(gameState,mediumPig3, "MEDIUM", 3,mediumPig3.getHitCount());
        }
        if (mediumPig4 != null && mediumPig4.isAlive()) {
            addPigToState(gameState,mediumPig4, "MEDIUM", 4,mediumPig4.getHitCount());
        }
        if (mediumPig5 != null && mediumPig5.isAlive()) {
            addPigToState(gameState,mediumPig5, "MEDIUM", 5,mediumPig5.getHitCount());
        }
    }
    private void addPigToState(GameState gameState,Pig pig, String type, int number,int hit) {
        GameState.PigData pigData = new GameState.PigData();
        Vector2 position = pig.getBody().getPosition();
        pigData.x = position.x * PIXELS_TO_METERS;
        pigData.y = position.y * PIXELS_TO_METERS;
        pigData.isAlive = pig.isAlive();
        pigData.pigType = type;
        pigData.pigNumber = number;
        pigData.hitcount = hit;

        gameState.pigStates.add(pigData);
    }

    private void loadPigStates(GameState gameState) {
        // First clear ALL existing pigs
        clearExistingPigs();

        for (GameState.PigData pigData : gameState.pigStates) {
            if (pigData.isAlive) {
                // Convert saved coordinates back to screen coordinates
                float screenX = pigData.x;  // Already in screen coordinates
                float screenY = pigData.y;

                switch(pigData.pigType) {
                    case "SMALL":
                        switch(pigData.pigNumber) {
                            case 1:
                                smallPig = new SmallPig(world, screenX, screenY);
                                smallPig.hitCount = pigData.hitcount;
                                break;
                            case 2:
                                smallPig2 = new SmallPig(world, screenX, screenY);
                                smallPig2.hitCount = pigData.hitcount;
                                break;
                            case 3:
                                smallPig3 = new SmallPig(world, screenX, screenY);
                                smallPig3.hitCount = pigData.hitcount;
                                break;
                            case 4:
                                smallPig4 = new SmallPig(world, screenX, screenY);
                                smallPig4.hitCount = pigData.hitcount;
                                break;
                            case 5:
                                smallPig5 = new SmallPig(world, screenX, screenY);
                                smallPig5.hitCount = pigData.hitcount;
                                break;
                        }
                        break;

                    case "MEDIUM":
                        switch(pigData.pigNumber) {
                            case 1:
                                mediumPig = new MediumPig(world, screenX, screenY);
                                mediumPig.hitCount = pigData.hitcount;
                                break;
                            case 2:
                                mediumPig2 = new MediumPig(world, screenX, screenY);
                                mediumPig2.hitCount = pigData.hitcount;
                                break;
                            case 3:
                                mediumPig3 = new MediumPig(world, screenX, screenY);
                                mediumPig3.hitCount = pigData.hitcount;
                                break;
                            case 4:
                                mediumPig4 = new MediumPig(world, screenX, screenY);
                                mediumPig4.hitCount = pigData.hitcount;
                                break;
                            case 5:
                                mediumPig5 = new MediumPig(world, screenX, screenY);
                                mediumPig5.hitCount = pigData.hitcount;
                                break;
                        }
                        break;
                }
            }
        }
    }

    private void clearExistingPigs() {
        // Clear existing pig bodies
        Pig[] allPigs = {
            smallPig, smallPig2, smallPig3, smallPig4, smallPig5,
            mediumPig, mediumPig2, mediumPig3, mediumPig4, mediumPig5
        };

        for (Pig pig : allPigs) {
            if (pig != null) {
                destroyPigBody(pig);
            }
        }

        // Reset all pig references
        smallPig = smallPig2 = smallPig3 = smallPig4 = smallPig5 = null;
        mediumPig = mediumPig2 = mediumPig3 = mediumPig4 = mediumPig5 = null;

        // ... repeat for other pigs


    }
    private void destroyPigBody(Pig pig) {
        if (pig.getBody() != null) {
            world.destroyBody(pig.getBody());
        }
    }


    public void saveGameState() {
        try {
            GameState state = new GameState();
            state.currentLevel = currentLevel;
            state.score = score;

            // Save which bird should be next
            if (currentBird == redBird) {
                state.nextBirdType = "BlueBird";
            } else if (currentBird == blueBird) {
                state.nextBirdType = "YellowBird";
            } else if (currentBird == yellowBird && currentLevel >= 2) {
                state.nextBirdType = "BlueBird2";
            } else if (currentBird == blueBird2) {
                state.nextBirdType = "YellowBird2";
            } else if (currentBird == yellowBird2 && currentLevel == 3) {
                state.nextBirdType = "RedBird2";
            } else if (currentBird == redBird2) {
                state.nextBirdType = "YellowBird3";
            }

            // Save pig states


            state.blockPositions = new float[16];
            state.blockAngles = new float[8];
            if (currentLevel==3){
            for (int i = 0; i < 8; i++) {
                Vector2 pos = blocks.get(i).getBody().getPosition();
                state.blockPositions[i*2] = pos.x;
                state.blockPositions[i*2 + 1] = pos.y;
                state.blockAngles[i] = blocks.get(i).getBody().getAngle();
            }}
            else if (currentLevel==2){
                for (int i = 0; i < 6; i++) {
                    Vector2 pos = blocks.get(i).getBody().getPosition();
                    state.blockPositions[i*2] = pos.x;
                    state.blockPositions[i*2 + 1] = pos.y;
                    state.blockAngles[i] = blocks.get(i).getBody().getAngle();
                }
            }
            else if (currentLevel==1){
                for (int i = 0; i < 3; i++) {
                    Vector2 pos = blocks.get(i).getBody().getPosition();
                    state.blockPositions[i*2] = pos.x;
                    state.blockPositions[i*2 + 1] = pos.y;
                    state.blockAngles[i] = blocks.get(i).getBody().getAngle();
                }
            }




            savePigStates(state);

            // Save remaining birds status
            state.hasRedBird = redBird != null && !redBird.isLaunched;
            state.hasBlueBird = blueBird != null && !blueBird.isLaunched;
            state.hasYellowBird = yellowBird != null && !yellowBird.isLaunched;
            state.hasBlueBird2 = blueBird2 != null && !blueBird2.isLaunched;
            state.hasYellowBird2 = yellowBird2 != null && !yellowBird2.isLaunched;
            state.hasRedBird2 = redBird2 != null && !redBird2.isLaunched;
            state.hasYellowBird3 = yellowBird3 != null && !yellowBird3.isLaunched;

            // Save to file
            FileOutputStream fileOut = new FileOutputStream("gamestate.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(state);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            Gdx.app.error("GameScreen", "Error saving game state: " + e.getMessage());
        }
    }

    void loadGameState() {
        try {
            cleanupBirdBodies();
            FileInputStream fileIn = new FileInputStream("gamestate.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameState state = (GameState) in.readObject();
            in.close();
            fileIn.close();

            // Restore game state
            currentLevel = state.currentLevel;
            score = state.score;

            float catapultX = SLINGSHOT_POSITION.x + 50;
            float catapultY = SLINGSHOT_POSITION.y + 20;


            // Restore current bird position if exists
            if (state.nextBirdType != null) {

                switch(state.nextBirdType) {
                    case "BlueBird":
                        currentBird = new BlueBird(world, catapultX, catapultY);
                        blueBird = (BlueBird) currentBird;
                        break;
                    case "YellowBird":
                        currentBird = new YellowBird(world, catapultX, catapultY);
                        yellowBird = (YellowBird) currentBird;
                        break;
                    case "BlueBird2":
                        currentBird = new BlueBird(world, catapultX, catapultY);
                        blueBird2 = (BlueBird) currentBird;
                        break;
                    case "YellowBird2":
                        currentBird = new YellowBird(world, catapultX, catapultY);
                        yellowBird2 = (YellowBird) currentBird;
                        break;
                    case "RedBird2":
                        currentBird = new RedBird(world, catapultX, catapultY);
                        redBird2 = (RedBird) currentBird;
                        break;
                    case "YellowBird3":
                        currentBird = new YellowBird(world, catapultX, catapultY);
                        yellowBird3 = (YellowBird) currentBird;
                        break;
                }

                if (currentBird != null) {
                    currentBird.getBody().setType(BodyDef.BodyType.StaticBody);
                    currentBird.isLaunched = false;

                    // Explicitly set position
                    currentBird.setPosition(catapultX, catapultY);
                }
            }

            float birdWaitingX = SLINGSHOT_POSITION.x - 50;
            float birdWaitingY = SLINGSHOT_POSITION.y - 80;
            int offset = 50;

            // Only create birds that haven't been launched yet
            if (state.hasYellowBird) {
                yellowBird = new YellowBird(world, birdWaitingX - offset, birdWaitingY);
                yellowBird.getBody().setType(BodyDef.BodyType.StaticBody);
                offset += 50;
            }

            if (currentLevel >= 2) {
                if (state.hasBlueBird2) {
                    blueBird2 = new BlueBird(world, birdWaitingX - offset, birdWaitingY);
                    blueBird2.getBody().setType(BodyDef.BodyType.StaticBody);
                    offset += 50;
                }
                if (state.hasYellowBird2) {
                    yellowBird2 = new YellowBird(world, birdWaitingX - offset, birdWaitingY);
                    yellowBird2.getBody().setType(BodyDef.BodyType.StaticBody);
                    offset += 50;
                }
            }

            if (currentLevel == 3) {
                if (state.hasRedBird2) {
                    redBird2 = new RedBird(world, birdWaitingX - offset, birdWaitingY);
                    redBird2.getBody().setType(BodyDef.BodyType.StaticBody);
                    offset += 50;
                }
                if (state.hasYellowBird3) {
                    yellowBird3 = new YellowBird(world, birdWaitingX - offset, birdWaitingY);
                    yellowBird3.getBody().setType(BodyDef.BodyType.StaticBody);
                }
            }

            loadPigStates(state);
           if (currentLevel==3) { // Load block positions
               for (int i = 0; i < 8; i++) {
                   blocks.get(i).getBody().setTransform(state.blockPositions[i * 2], state.blockPositions[i * 2 + 1], state.blockAngles[i]);
               }
           }
            else if (currentLevel==2) { // Load block positions
                for (int i = 0; i < 6; i++) {
                    blocks.get(i).getBody().setTransform(state.blockPositions[i * 2], state.blockPositions[i * 2 + 1], state.blockAngles[i]);
                }
            }

            else if (currentLevel==1) { // Load block positions
                for (int i = 0; i < 3; i++) {
                    blocks.get(i).getBody().setTransform(state.blockPositions[i * 2], state.blockPositions[i * 2 + 1], state.blockAngles[i]);
                }
            }
           // Reset bird to catapult

        } catch (IOException | ClassNotFoundException e) {
            Gdx.app.error("GameScreen", "Error loading game state: " + e.getMessage());
        }


    }

    private boolean isGameWon() {
        // Implement your logic to check if the game is won
        // For example, check if all pigs are destroyed
        for (Pig pig : pigs) {
            if (pig.isAlive()) {
                return false;
            }
        }
        return true;
    }
    public void addPigToStates(Pig pig,GameState state) {
        if (pig != null) {
            GameState.PigData pigData = new GameState.PigData();
            Vector2 position = pig.getBody().getPosition();
            pigData.x = position.x;
            pigData.y = position.y;
            pigData.isAlive = pig.isAlive();
            pigData.pigType = pig instanceof SmallPig ? "SMALL" : "MEDIUM";
            state.pigStates.add(pigData);
        }
    }
    private void cleanupBirdBodies() {
        // Clean up current bird
        if (currentBird != null && currentBird.getBody() != null) {
            world.destroyBody(currentBird.getBody());
            currentBird = null;
        }

        // Clean up all bird bodies
        if (redBird != null && redBird.getBody() != null) {
            world.destroyBody(redBird.getBody());
            redBird = null;
        }
        if (blueBird != null && blueBird.getBody() != null) {
            world.destroyBody(blueBird.getBody());
            blueBird = null;
        }
        if (yellowBird != null && yellowBird.getBody() != null) {
            world.destroyBody(yellowBird.getBody());
            yellowBird = null;
        }
        if (blueBird2 != null && blueBird2.getBody() != null) {
            world.destroyBody(blueBird2.getBody());
            blueBird2 = null;
        }
        if (yellowBird2 != null && yellowBird2.getBody() != null) {
            world.destroyBody(yellowBird2.getBody());
            yellowBird2 = null;
        }
        if (redBird2 != null && redBird2.getBody() != null) {
            world.destroyBody(redBird2.getBody());
            redBird2 = null;
        }
        if (yellowBird3 != null && yellowBird3.getBody() != null) {
            world.destroyBody(yellowBird3.getBody());
            yellowBird3 = null;
        }
    }
    private void processDeferredDestructions() {
        for (Pig pig : pigsToDestroy) {
            try {
                if (pig != null && pig.getBody() != null) {
                    if (pig == smallPig) {
                        pig.destroyBody();
                        smallPig = null;
                    } else if (pig == mediumPig) {
                        pig.destroyBody();
                        mediumPig = null;
                    }
                }
            } catch (Exception e) {
                Gdx.app.error("GameScreen", "Error destroying pig: " + e.getMessage());
            }
        }
        pigsToDestroy.clear();
        for (Block block : blocksToRemove) {
            try {
                if (block != null && block.getBody() != null) {
                    blocks.removeValue(block, true);
                    world.destroyBody(block.getBody());
                }
            } catch (Exception e) {
                Gdx.app.error("GameScreen", "Error removing block: " + e.getMessage());
            }
        }
        blocksToRemove.clear();
    }
    private void createRightBoundary() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(RIGHT_WALL_X, RIGHT_WALL_HEIGHT / 2);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WALL_THICKNESS, RIGHT_WALL_HEIGHT / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
    private void processPhysicsChanges() {
        for (Body body : bodiesToMakeActive) {
            if (body != null && body.getType() != BodyDef.BodyType.DynamicBody) {
                body.setType(BodyDef.BodyType.DynamicBody);
            }
        }
        bodiesToMakeActive.clear();
        for (Block block : blocksToActivate) {
            if (block != null && block.getBody() != null) {
                block.getBody().setType(BodyDef.BodyType.DynamicBody);
                float randomX = (float)(Math.random() * 0.2f - 0.1f);
                float randomY = (float)(Math.random() * 0.2f);
                block.getBody().applyLinearImpulse(
                    randomX,
                    randomY,
                    block.getBody().getWorldCenter().x,
                    block.getBody().getWorldCenter().y,
                    true
                );
            }
        }
        blocksToActivate.clear();
    }
    private void renderTrajectory() {
        if (!isDragging || currentBird == null || currentBird.isLaunched) return;
        Vector2 dragVector = new Vector2(
            dragCurrent.x - SLINGSHOT_POSITION.x,
            dragCurrent.y - SLINGSHOT_POSITION.y
        );
        if (dragVector.len() > MAX_DRAG_DISTANCE) {
            dragVector.setLength(MAX_DRAG_DISTANCE);
        }
        dragVector.scl(-1);
        calculateTrajectoryDots(dragVector);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(TRAJECTORY_DOT_COLOR);
        for (int i = 0; i < trajectoryDots.size; i++) {
            Vector2 dot = trajectoryDots.get(i);
            float dotSize = TRAJECTORY_DOT_SIZE * (1f - (float)i / NUM_TRAJECTORY_DOTS * 0.5f);
            shapeRenderer.circle(dot.x, dot.y, dotSize);
        }
        shapeRenderer.end();
    }
    private void processDeferredCollisions() {
        removeDestroyedObjects();
    }
    private void removeDestroyedObjects() {
        for (Block block : blocksToRemove) {
            blocks.removeValue(block, true);
            world.destroyBody(block.getBody());
        }
        blocksToRemove.clear();
    }
    @Override
    public void endContact(Contact contact) {
    }
    @Override
    public void preSolve(Contact contact, Manifold manifold) {
    }
    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    }
    @Override
    public void beginContact(Contact contact) {
        try {
            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();
            if (bodyA == null || bodyB == null) return;
            Object userDataA = bodyA.getUserData();
            Object userDataB = bodyB.getUserData();
            if (userDataA == null || userDataB == null) return;
            if ((userDataA instanceof Bird && userDataB instanceof Pig) ||
                (userDataA instanceof Pig && userDataB instanceof Bird)) {
                Bird bird = (Bird) (userDataA instanceof Bird ? userDataA : userDataB);
                Pig pig = (Pig) (userDataA instanceof Pig ? userDataA : userDataB);
                handleBirdPigCollision(bird, pig);
                score += 100;
            }
            if ((userDataA instanceof Bird && userDataB instanceof Block) ||
                (userDataA instanceof Block && userDataB instanceof Bird)) {
                Bird bird = (Bird) (userDataA instanceof Bird ? userDataA : userDataB);
                Block block = (Block) (userDataA instanceof Block ? userDataA : userDataB);
                deferCollisionHandling(bird, block);
                score += 200;
            }
            if ((userDataA instanceof Block && userDataB instanceof Pig) ||
                (userDataA instanceof Pig && userDataB instanceof Block)) {
                Block block = (Block) (userDataA instanceof Block ? userDataA : userDataB);
                Pig pig = (Pig) (userDataA instanceof Pig ? userDataA : userDataB);
                handleBlockPigCollision(block, pig);

            }
            if (userDataA instanceof Block && userDataB instanceof Block) {
                blocksToActivate.add((Block)userDataA);
                blocksToActivate.add((Block)userDataB);
            }
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error in collision handling: " + e.getMessage());
        }
    }
    private void handleBirdPigCollision(Bird bird, Pig pig) {
        if (!pig.isAlive() || (hasCollided && bird != currentBird)) return;
        Vector2 velocity = bird.getBody().getLinearVelocity();
        float impactForce = velocity.len();
        if (impactForce > 1.0f) {
            float damage = impactForce * 2;
            if (bird instanceof YellowBird) {
                damage *= 1.5f;
            }
            pig.takeDamage(damage);
            if (!pig.isAlive()) {
                pigsToDestroy.add(pig);
                float bounceBackX = -velocity.x * 0.3f;
                float bounceBackY = -velocity.y * 0.3f;
                bird.getBody().applyLinearImpulse(
                    bounceBackX,
                    bounceBackY,
                    bird.getBody().getWorldCenter().x,
                    bird.getBody().getWorldCenter().y,
                    true
                );
            }
            if (bird == currentBird && impactForce > 2.0f) {
                shouldSwitchBird = true;
                hasCollided = true;
                birdSwitchTimer = 0;
            }
        }
    }
    private void checkPigStatus() {
        boolean allPigsDestroyed = (smallPig == null || !smallPig.isAlive()) &&
            (mediumPig == null || !mediumPig.isAlive());

        if (currentLevel >= 2) {
            allPigsDestroyed = allPigsDestroyed &&
                (smallPig2 == null || !smallPig2.isAlive()) &&
                (smallPig3 == null || !smallPig3.isAlive()) &&
                (mediumPig2 == null || !mediumPig2.isAlive()) &&
                (mediumPig3 == null || !mediumPig3.isAlive());
        }

        if (currentLevel == 3) {
            allPigsDestroyed = allPigsDestroyed &&
                (smallPig4 == null || !smallPig4.isAlive()) &&
                (smallPig5 == null || !smallPig5.isAlive()) &&
                (mediumPig4 == null || !mediumPig4.isAlive()) &&
                (mediumPig5 == null || !mediumPig5.isAlive());
        }
        boolean noBirdsLeft = currentBird == null &&
            redBird == null && blueBird == null && yellowBird == null &&
            (currentLevel >= 2 ? (blueBird2 == null && yellowBird2 == null) : true) &&
            (currentLevel == 3 ? (redBird2 == null && yellowBird3 == null) : true);
        if (allPigsDestroyed) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        game.setScreen(new WinScreen(game, currentLevel));
                    });
                }
            }, 1.0f);
        } else if (noBirdsLeft && !allPigsDestroyed) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        game.setScreen(new LoseScreen(game));
                    });
                }
            }, 1.0f);
        }
    }
    private void   deferCollisionHandling(Bird bird, Block block) {
        if (bird == currentBird && !shouldSwitchBird) {
            Vector2 velocity = bird.getBody().getLinearVelocity();
            float impactForce = velocity.len();
            Vector2 hitPoint = block.getBody().getPosition();
            float activationRadius = 4.0f;
            if (impactForce > 0.5f) {
                float damage = impactForce * 4;

                // Adjust damage based on block type
                if (block instanceof StoneBlock) {
                    damage *= 0.5f; // Stone takes less damage
                    if (bird instanceof YellowBird) {
                        damage *= 1.5f; // Yellow birds do more damage to stone
                    }
                } else if (block instanceof GlassBlock) {
                    damage *= 1.5f; // Glass takes more damage
                }
                hasCollided = true;
                shouldSwitchBird = true;
                birdSwitchTimer = 0;
                for (Block nearbyBlock : blocks) {
                    if (nearbyBlock.getBody().getType() != BodyDef.BodyType.DynamicBody) {
                        blocksToActivate.add(nearbyBlock);
                    }
                }

                block.takeDamage(damage);

                if (block.isDestroyed()) {
                    blocksToRemove.add(block);
                }
            }
        }
    }
    private void handleBlockPigCollision(Block block, Pig pig) {
        if (!pig.isAlive()) return;
        Vector2 blockVelocity = block.getBody().getLinearVelocity();
        float impactForce = blockVelocity.len();
        if (impactForce > 1.0f) {
            float damage = impactForce * 2;
            pig.takeDamage(damage);
            checkPigStatus();
        }
    }
    private void switchToNextBird() {
        try {
            if (currentBird != null && currentBird.getBody() != null) {
                world.destroyBody(currentBird.getBody());
            }
            Bird nextBird = null;

            if (currentLevel == 1) {
                if (currentBird == redBird) {
                    redBird = null;
                    nextBird = blueBird;
                } else if (currentBird == blueBird) {
                    blueBird = null;
                    nextBird = yellowBird;
                } else if (currentBird == yellowBird) {
                    yellowBird = null;
                    nextBird = blackBird;
                }
                else if (currentBird == blackBird) {
                    blackBird = null;
                    nextBird = null;
                }
            } else if (currentLevel == 2) {
                if (currentBird == redBird) {
                    redBird = null;
                    nextBird = blueBird;
                } else if (currentBird == blueBird) {
                    blueBird = null;
                    nextBird = yellowBird;
                } else if (currentBird == yellowBird) {
                    yellowBird = null;
                    nextBird = blueBird2;
                } else if (currentBird == blueBird2) {
                    blueBird2 = null;
                    nextBird = yellowBird2;
                } else if (currentBird == yellowBird2) {
                    yellowBird2 = null;
                    nextBird = null;
                }
            }

            if (currentLevel == 3) {
                if (currentBird == redBird) {
                    redBird = null;
                    nextBird = blueBird;
                } else if (currentBird == blueBird) {
                    blueBird = null;
                    nextBird = yellowBird;
                } else if (currentBird == yellowBird) {
                    yellowBird = null;
                    nextBird = blueBird2;
                } else if (currentBird == blueBird2) {
                    blueBird2 = null;
                    nextBird = yellowBird2;
                } else if (currentBird == yellowBird2) {
                    yellowBird2 = null;
                    nextBird = redBird2;
                } else if (currentBird == redBird2) {
                    redBird2 = null;
                    nextBird = yellowBird3;
                } else if (currentBird == yellowBird3) {
                    yellowBird3 = null;
                    nextBird = null;
                }
            }

            currentBird = nextBird;
            hasCollided = false;
            birdLeftScreen = false;

            if (currentBird != null) {
                float nextBirdX = SLINGSHOT_POSITION.x + 50;
                float nextBirdY = SLINGSHOT_POSITION.y + 20;
                Body birdBody = currentBird.getBody();
                if (birdBody != null) {
                    birdBody.setTransform(
                        nextBirdX / PIXELS_TO_METERS,
                        nextBirdY / PIXELS_TO_METERS,
                        0
                    );
                    birdBody.setType(BodyDef.BodyType.StaticBody);
                    birdBody.setLinearVelocity(0, 0);
                    birdBody.setAngularVelocity(0);
                    currentBird.isLaunched = false;
                }
            }
            checkPigStatus();
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error in switchToNextBird: " + e.getMessage());
            currentBird = null;
            checkPigStatus();
        }
    }

    private void createBlock(float x, float y, String type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        // Create more precise polygon shape instead of rectangle
        PolygonShape shape = new PolygonShape();
        if (type.equals("WOOD")) {
            shape.setAsBox((BLOCK_WIDTH/2) / PPM, (BLOCK_HEIGHT/2) / PPM);
        } else if (type.equals("GLASS")) {
            shape.setAsBox((BLOCK_WIDTH/2) / PPM, (BLOCK_HEIGHT/2) / PPM);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    private void cleanupDeadPigs() {
        Array<Body> bodiesToDestroy = new Array<>();

        // Check all pigs
        if (smallPig != null && !smallPig.isAlive()) {
            bodiesToDestroy.add(smallPig.getBody());
            smallPig = null;
        }
        if (mediumPig != null && !mediumPig.isAlive()) {
            bodiesToDestroy.add(mediumPig.getBody());
            mediumPig = null;
        }

        if (currentLevel == 2) {
            if (smallPig2 != null && !smallPig2.isAlive()) {
                bodiesToDestroy.add(smallPig2.getBody());
                smallPig2 = null;
            }
            if (smallPig3 != null && !smallPig3.isAlive()) {
                bodiesToDestroy.add(smallPig3.getBody());
                smallPig3 = null;
            }
            if (mediumPig2 != null && !mediumPig2.isAlive()) {
                bodiesToDestroy.add(mediumPig2.getBody());
                mediumPig2 = null;
            }
            if (mediumPig3 != null && !mediumPig3.isAlive()) {
                bodiesToDestroy.add(mediumPig3.getBody());
                mediumPig3 = null;
            }
        }
        if (currentLevel == 3) {
            if (smallPig2 != null && !smallPig2.isAlive()) {
                bodiesToDestroy.add(smallPig2.getBody());
                smallPig2 = null;
            }
            if (smallPig3 != null && !smallPig3.isAlive()) {
                bodiesToDestroy.add(smallPig3.getBody());
                smallPig3 = null;
            }
            if (mediumPig2 != null && !mediumPig2.isAlive()) {
                bodiesToDestroy.add(mediumPig2.getBody());
                mediumPig2 = null;
            }
            if (mediumPig3 != null && !mediumPig3.isAlive()) {
                bodiesToDestroy.add(mediumPig3.getBody());
                mediumPig3 = null;
            }
            if (smallPig4 != null && !smallPig4.isAlive()) {
                bodiesToDestroy.add(smallPig4.getBody());
                smallPig4 = null;
            }
            if (smallPig5 != null && !smallPig5.isAlive()) {
                bodiesToDestroy.add(smallPig5.getBody());
                smallPig5 = null;
            }
            if (mediumPig4 != null && !mediumPig4.isAlive()) {
                bodiesToDestroy.add(mediumPig4.getBody());
                mediumPig4 = null;
            }
            if (mediumPig5 != null && !mediumPig5.isAlive()) {
                bodiesToDestroy.add(mediumPig5.getBody());
                mediumPig5 = null;
            }

        }

        // Add other level 2 pigs here


        // Destroy bodies safely
        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
        }
    }

    // Update pig creation to use circle shape
    private void createPig(float x, float y, String type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(PIG_RADIUS / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPoint = viewport.unproject(new Vector2(screenX, screenY));

        if (currentBird != null && !currentBird.isLaunched) {
            Vector2 birdPos = currentBird.getPosition();
            if (worldPoint.dst(birdPos.x, birdPos.y) < 50) {
                isDragging = true;
                dragStart.set(worldPoint);
                dragCurrent.set(worldPoint);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging && currentBird != null && !currentBird.isLaunched) {
            Vector2 worldPoint = viewport.unproject(new Vector2(screenX, screenY));
            Vector2 dragVector = new Vector2(
                worldPoint.x - SLINGSHOT_POSITION.x,
                worldPoint.y - SLINGSHOT_POSITION.y
            );
            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.setLength(MAX_DRAG_DISTANCE);
                worldPoint.set(
                    SLINGSHOT_POSITION.x + dragVector.x,
                    SLINGSHOT_POSITION.y + dragVector.y
                );
            }
            dragCurrent.set(worldPoint);
            currentBird.setPosition(worldPoint.x, worldPoint.y);
            return true;
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging && currentBird != null && !currentBird.isLaunched) {
            Vector2 worldPoint = viewport.unproject(new Vector2(screenX, screenY));
            Vector2 launchVector = new Vector2(
                SLINGSHOT_POSITION.x - worldPoint.x,
                SLINGSHOT_POSITION.y - worldPoint.y
            );

            if (launchVector.len() > MAX_DRAG_DISTANCE) {
                launchVector.setLength(MAX_DRAG_DISTANCE);
            }
            float impulseX = (launchVector.x * LAUNCH_SPEED_MULTIPLIER) / PIXELS_TO_METERS;
            float impulseY = (launchVector.y * LAUNCH_SPEED_MULTIPLIER) / PIXELS_TO_METERS;
            currentBird.getBody().setType(BodyDef.BodyType.DynamicBody);
            currentBird.getBody().setLinearVelocity(0, 0);
            currentBird.getBody().setAngularVelocity(0);

            currentBird.getBody().applyLinearImpulse(
                impulseX,
                impulseY,
                currentBird.getBody().getWorldCenter().x,
                currentBird.getBody().getWorldCenter().y,
                true
            );

            currentBird.isLaunched = true;
            isDragging = false;
            return true;
        }
        return false;
    }
    @Override
    public void dispose() {
        saveGameState();
        cleanupBirdBodies();
        gameScreenTexture.dispose();
        pauseButtonTexture.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        catapult.dispose();
        if (redBird != null) redBird.dispose();
        if (blueBird != null) blueBird.dispose();
        if (yellowBird != null) yellowBird.dispose();
        if (blackBird != null) {
            blackBird.dispose();
        }
        if (trajectoryDots != null) {
            trajectoryDots.clear();
        }
        for (Block block : blocks) {
            block.dispose();
        }

        for (BlockPiece piece : blockPieces) {
            piece.texture.dispose();
            world.destroyBody(piece.body);
        }
        blockPieces.clear();
        smallPig.dispose();
        mediumPig.dispose();
        world.dispose();
        font.dispose();
        debugRenderer.dispose();
    }
    private void shakeScreen(float delta) {
        if (isScreenShaking) {
            shakeTime += delta;
            if (shakeTime <= SHAKE_DURATION) {
                float currentIntensity = SHAKE_INTENSITY * (1 - shakeTime / SHAKE_DURATION);
                float offsetX = (float)(Math.random() * 2 - 1) * currentIntensity;
                float offsetY = (float)(Math.random() * 2 - 1) * currentIntensity;
                camera.position.set(
                    camera.viewportWidth / 2 + offsetX,
                    camera.viewportHeight / 2 + offsetY,
                    0
                );
                camera.update();
            } else {
                isScreenShaking = false;
                shakeTime = 0;
                camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
                camera.update();
            }
        }
    }
    @Override public void show() {InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);
    }
    @Override public void hide() {}
    @Override public void pause() {
        saveGameState();
    }
    @Override public void resume() {}
    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean keyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.SPACE) {
            if (currentBird != null && currentBird instanceof BlueBird && currentBird.isLaunched) {
                currentBird.useSpecialAbility();
                return true;
            }
            if (currentBird instanceof BlackBird && !isScreenShaking) {
                currentBird.useSpecialAbility();
                isScreenShaking = true;
                shakeTime = 0;
                return true;
            }
        }
        return false;
    }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}

