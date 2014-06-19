package dovietkien.vn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen
{
    protected final Another game;
    protected final Stage stage;
    
    protected final TextureAtlas atlas;
    private BitmapFont font;
    private SpriteBatch batch;
    
    public AbstractScreen( Another game )
    {
        this.game = game;
        this.stage = new Stage( 0, 0, true );
        this.atlas = new TextureAtlas(Gdx.files.internal("data/textures/textures.pack"));
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    public BitmapFont getFont()
    {
        if( font == null ) {
            font = new BitmapFont(Gdx.files.internal("data/viettay.fnt"),
                    Gdx.files.internal("data/viettay.png"), false);
        }
        return font;
    }

    public SpriteBatch getBatch()
    {
        if( batch == null ) {
            batch = new SpriteBatch();
        }
        return batch;
    }

    // Screen implementation

    @Override
    public void show()
    {
        // set the input processor
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(int width, int height )
    {
        // resize and clear the stage
        stage.setViewport( width, height, true );
        stage.clear();
    }

    @Override
    public void render( float delta )
    {
        // (1) process the game logic

        // update the actors
        stage.act( delta );

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(  0.9f,0.9f,0.9f,0.9f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // draw the actors
        stage.draw();
    }

    @Override
    public void hide()
    {
        // dispose the resources by default
        dispose();
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        if( font != null ) font.dispose();
        if( batch != null ) batch.dispose();
    }
}
