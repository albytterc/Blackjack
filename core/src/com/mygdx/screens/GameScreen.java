package com.mygdx.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.blackjack.Blackjack;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;
import com.mygdx.gameobjects.Card;
import com.mygdx.gameobjects.Deck;
import com.mygdx.managers.GameManager;
import org.w3c.dom.Text;

public class GameScreen implements Screen, InputProcessor {
    // public static final float MINIMUM_VIEWPORT = 13f;
    public static OrthographicCamera camera;
    private GameManager gameManager;
    private InputMultiplexer inputMultiplexer;

    // public static final float aspectRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
    Stage stage;

    SpriteBatch batch;
    public static TextureAtlas atlas;
    Deck deck;

    private final Blackjack game;

    Card selectedCard;
    Vector2 temp = new Vector2();

    static float buttonWidth = 100f;
    static float buttonHeight = 30f;
    TextButton restartButton;
    TextButton hitButton;
    Label endGameResult;

    public GameScreen(final Blackjack game) {
        this.game = game;
        camera = new OrthographicCamera();
        // camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        batch = new SpriteBatch();
        atlas = new TextureAtlas("Decks/card-deck.atlas");
        deck = new Deck(atlas, 0);
        stage = new Stage(new ScreenViewport());
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);

        gameManager = new GameManager(game, deck);
        gameManager.spawnCards();

        addCards(gameManager.currentHand);

        endGameResult = new Label("", new Skin(Gdx.files.absolute("C:\\Users\\albyt\\Downloads\\gdx-skins-master\\gdx-skins-master\\orange\\skin\\uiskin.json")), "title-white");
        endGameResult.setWidth(Gdx.graphics.getWidth());
        endGameResult.setY(Gdx.graphics.getHeight() * .75f);
        endGameResult.setAlignment(Align.center);
        endGameResult.setVisible(false);
        endGameResult.setColor(1, 1, 1, 0);
        stage.addActor(endGameResult);



        restartButton = createTextButton("Restart", 10, Gdx.graphics.getHeight() - 10 - buttonHeight);
        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                for (int i = stage.getActors().size - 1; i >= 0; i--) {
                    Actor a = stage.getActors().get(i);
                    if (a instanceof Card) {
                        a.remove();
                    }
                }
                gameManager.restart();
                endGameResult.setVisible(false);
                endGameResult.setColor(1, 1, 1, 0);
                hitButton.setTouchable(Touchable.enabled);
                addCards(gameManager.currentHand);
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

    private TextButton createTextButton(String text, float x, float y) {
        TextButton button = new TextButton(text, Blackjack.gameSkin);
        button.setSize(buttonWidth, buttonHeight);
        button.setPosition(x, y);
        return button;
    }

    public void addCards(ObjectSet<Card> hand) {
        int i = 0;
        for (Card card : hand) {
            // TODO: Calculate x for how ever many cards in the hand to make it centered
            float x = Gdx.graphics.getWidth() / 2f - card.front.getWidth() / 2f + i * 20 - hand.size*5;
            float y = Gdx.graphics.getHeight() / 2f - card.front.getHeight() / 2f;
            // card.setPosition(, );
            card.addAction(Actions.moveTo(x, y, 0.5f));
            stage.addActor(card);
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
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
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
            // endGameResult.addAction(Actions.moveTo(10, 10, 1));
        }
        batch.end();
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
        batch.dispose();
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
        return false;
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
