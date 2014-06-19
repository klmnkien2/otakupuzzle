package dovietkien.vn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
 
public class MenuScreen extends AbstractScreen implements InputProcessor
{
    private int G_WIDTH;
    private int G_HEIGHT;
    Sprite spriteGoku;
    Sprite spriteLuffy;
    Sprite spriteNaruto;
    private OrthographicCamera camera;
 
    public MenuScreen( Another game )
    {
        super( game );
    }
    
    @Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}
 
    @Override
    public void resize(int width, int height )
    {
    	G_WIDTH = width;
    	G_HEIGHT = (height-4)/3;
    	
    	camera = new OrthographicCamera(width, height);
        camera.position.set(width / 2f, height / 2f, 0);
        camera.update();
 
    	spriteGoku = new Sprite(atlas.findRegion("goku"));
    	spriteGoku.setPosition(0, height - G_HEIGHT);
    	spriteGoku.setSize(G_WIDTH, G_HEIGHT);
    	
    	spriteLuffy = new Sprite(atlas.findRegion("luffy"));
    	spriteLuffy.setPosition(0, G_HEIGHT + 2);
    	spriteLuffy.setSize(G_WIDTH, G_HEIGHT);
    	
    	spriteNaruto = new Sprite(atlas.findRegion("naruto"));		
    	spriteNaruto.setPosition(0,0);
    	spriteNaruto.setSize(G_WIDTH, G_HEIGHT);
    }
    
    @Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0.9f,0.9f,0.9f,0.9f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		getBatch().setProjectionMatrix(camera.combined);
		getBatch().begin();		
		
		spriteGoku.draw(getBatch());
		spriteLuffy.draw(getBatch());
		spriteNaruto.draw(getBatch());
		
		getBatch().end();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		try {
			//Click control button
			if(spriteGoku.getBoundingRectangle().overlaps(new Rectangle(screenX, G_HEIGHT *3 + 4 - screenY, 1, 1))) {
				game.setScreen(new LevelScreen(game, "goku"));
			}
			
			else if(spriteLuffy.getBoundingRectangle().overlaps(new Rectangle(screenX, G_HEIGHT *3 + 4 - screenY, 1, 1))) {
				game.setScreen(new LevelScreen(game, "luffy"));
			}
			
			else if(spriteNaruto.getBoundingRectangle().overlaps(new Rectangle(screenX, G_HEIGHT *3 + 4 - screenY, 1, 1))) {
				game.setScreen(new LevelScreen(game, "naruto"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
