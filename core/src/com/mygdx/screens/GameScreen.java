package com.mygdx.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.blackjack.Blackjack;
import com.mygdx.gameobjects.Card;
import com.mygdx.gameobjects.Deck;
import com.mygdx.managers.GameManager;

public class GameScreen implements Screen, InputProcessor {
    // public static final float MINIMUM_VIEWPORT = 13f;
    public static OrthographicCamera camera;
    private final GameManager gameManager;
    private final InputMultiplexer inputMultiplexer;

    // public static final float aspectRatio = (float) Blackjack.HEIGHT / Blackjack.WIDTH;
    Stage stage;
    Group handGroup;

    public static TextureAtlas atlas;
    Deck deck;

    Card selectedCard;
    Vector2 temp = new Vector2();

    static float buttonWidth = 100f;
    static float buttonHeight = 30f;
    TextButton restartButton;
    TextButton hitButton;
    Label endGameResult;
    Label scoreText;

    public GameScreen(final Blackjack game) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Blackjack.WIDTH, Blackjack.HEIGHT);
        // stage = new Stage(new ScreenViewport(camera));
        stage = new Stage(new FitViewport(Blackjack.WIDTH, Blackjack.HEIGHT, camera));

        atlas = new TextureAtlas("Decks/card-deck.atlas");
        deck = new Deck(atlas, 0);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        handGroup = new Group();

        gameManager = new GameManager(deck);
        gameManager.spawnCards();

        addCards(gameManager.currentHand);
        stage.addActor(handGroup);

        endGameResult = new Label("", new Skin(Gdx.files.internal("orange/skin/uiskin.json")), "title-white");
        endGameResult.setWidth(Blackjack.WIDTH);
        endGameResult.setY(Blackjack.HEIGHT * .75f);
        endGameResult.setAlignment(Align.center);
        endGameResult.setVisible(false);
        stage.addActor(endGameResult);

        scoreText = new Label("", new Skin(Gdx.files.internal("orange/skin/uiskin.json")), "title-white");
        scoreText.setWidth(Blackjack.WIDTH);
        scoreText.setY(Blackjack.HEIGHT * .25f);
        scoreText.setAlignment(Align.center);
        scoreText.setVisible(false);
        stage.addActor(scoreText);


        restartButton = createTextButton("Restart", 10, Blackjack.HEIGHT - 10 - buttonHeight);
        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                restartGame();
            }
        });
        stage.addActor(restartButton);

        hitButton = createTextButton("Hit", 10, 10);


        hitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                gameManager.hit();
                addCards(gameManager.currentHand);
            }
        });
        stage.addActor(hitButton);
    }

    private void restartGame() {
        for (int i = handGroup.getChildren().size - 1; i >= 0; i--) {
            Actor a = handGroup.getChildren().get(i);
            if (a instanceof Card) {
                a.remove();
            }
        }
        gameManager.restart();
        endGameResult.setVisible(false);

        scoreText.setVisible(false);
        hitButton.setTouchable(Touchable.enabled);
        addCards(gameManager.currentHand);
    }

    private TextButton createTextButton(String text, float x, float y) {
        TextButton button = new TextButton(text, Blackjack.gameSkin);
        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(x, y);
        return button;
    }

    public void addCards(ObjectSet<Card> hand) {
        int i = 0;
        for (Card card : hand) {
            float x = Blackjack.WIDTH / 2f - ((card.front.getWidth() + 20*(hand.size - 1)) / 2f - i*20);
            float y = Blackjack.HEIGHT / 2f - card.front.getHeight() / 2f;
            card.addAction(Actions.moveTo(x, y, 0.5f));
            handGroup.addActor(card);
            i++;
        }
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.6f, 0, 1);
        camera.update();
        if (deck.isEmpty()) {
            restartButton.remove();
        }
        if (gameManager.currentScore >= 21) {
            if (gameManager.currentScore == 21) {
                endGameResult.setText("You Win");
            } else {
                endGameResult.setText("You Lose");
            }
            hitButton.setTouchable(Touchable.disabled);
            endGameResult.setVisible(true);
            endGameResult.setColor(1, 1, 1, 0);
            endGameResult.addAction(Actions.fadeIn(1));

            scoreText.setText("Score: " + gameManager.currentScore);
            scoreText.setVisible(true);
            scoreText.setColor(1, 1, 1, 0);
            scoreText.addAction(Actions.fadeIn(1));
        }
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
        stage.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 coords = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitActor = stage.hit(coords.x, coords.y, true);

        if (hitActor instanceof Card) {
            selectedCard = (Card) hitActor;
            temp.set(coords);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectedCard != null) {
            temp.sub(stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY)));
            if (temp.x == 0 && temp.y == 0)
                selectedCard.turn();
            selectedCard = null;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 coords = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));

        if (selectedCard != null) {
            selectedCard.toFront();
            selectedCard.setPosition(coords.x, coords.y);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if ((keycode == Input.Keys.SPACE || keycode == Input.Keys.H) && gameManager.currentScore < 21) {
            gameManager.hit();
            addCards(gameManager.currentHand);
        }

        if (keycode == Input.Keys.ESCAPE) {
            restartGame();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}
