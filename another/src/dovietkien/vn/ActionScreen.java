package dovietkien.vn;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class ActionScreen extends AbstractScreen implements InputProcessor {

	private OrthographicCamera camera;
	private Piece[][] pieces;
	private int level;
	private String type;
	private int gameCols;
	private int gameRows;
	private int freeCol;
	private int freeRow;
	private boolean canmove = true; //for accept input one
	private int wrongPositions;
	float paddingLeft;
	NinePatch menuDiv;
	Sprite spriteCross;
	Sprite spriteTick;
	Sprite spriteHelp;
	Sprite spriteMedal;
	Sprite spriteRefesh;
	Sprite spriteSound;
	Sound sound;
	Sound winsound;
	private long startGameTime;
	private String winTime = "";
	private boolean playSound = true;
	private boolean renderHelp = false;
	private boolean renderScore = false;
	private boolean winMessage = false;
	private boolean flagSaveScore = true;
	
	ProfileService profileService;
	Profile profile;
	
	public static float GAME_VIEWPORT_WIDTH = 400.0F; 
	public static float GAME_VIEWPORT_HEIGHT = 700.0F; 
	public static float CELL_WIDTH = 100.0F; 
	public static float CELL_HEIGHT = 100.0F;
	
    public ActionScreen(Another game, String type, int level) {
		super(game);
		this.type = type;
		if(level == 1) {
			gameCols = 3;
	        gameRows = 5;
		} else if(level == 2) {
			gameCols = 3;
	        gameRows = 5;
		} else if(level == 3) {
			gameCols = 4;
	        gameRows = 5;
		}
		this.level = level;		
	}
    
    @Override
    public void resize(int width, int height )
    {
    	GAME_VIEWPORT_WIDTH = width;
    	GAME_VIEWPORT_HEIGHT = height;
    	CELL_WIDTH = GAME_VIEWPORT_WIDTH/gameCols;
		CELL_HEIGHT = GAME_VIEWPORT_HEIGHT/(gameRows + 1);
		paddingLeft = (GAME_VIEWPORT_WIDTH - gameCols * CELL_WIDTH)/2;
		
		camera = new OrthographicCamera(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
        camera.position.set(GAME_VIEWPORT_WIDTH / 2f, GAME_VIEWPORT_HEIGHT / 2f, 0);
        camera.update();

        wrongPositions = gameCols * gameRows - 1;
        startGameTime = System.currentTimeMillis();
        
        freeCol = 0;
        freeRow = gameRows;
        pieces = new Piece[gameRows+1][gameCols];
        for(int i=0; i<gameCols; i++) pieces[gameRows][i] = null;
		
		TextureRegion region = atlas.findRegion(type + level);
		TextureRegion[][] tiles = region.split(region.getRegionWidth()/gameCols, region.getRegionHeight()/gameRows);
		
		Array<Integer> random = new Array<Integer>();
		for(int i=0; i< gameCols*gameRows; i++) {
			if(i!=gameCols * gameRows-gameCols)	random.add(i);
		}
		
		Sprite sprite;
		int currentY;
		int currentX;		
		
		for(int i=0; i<gameRows; i++) {
			for(int j=0; j<gameCols; j++) {
				sprite = new Sprite(tiles[i][j]);
				if(j==0 && 0==i) {
					currentY = gameRows-1;
					currentX = 0;
				} else {
//					currentY = gameRows-1-i;
//					currentX = j;
					int randomNum = random.random();				
					random.removeValue(randomNum, false);
					currentY = randomNum/gameCols;
					currentX = randomNum % gameCols;
				}
				
				sprite.setSize(CELL_WIDTH - 2, CELL_HEIGHT - 2);
				sprite.setPosition(paddingLeft + CELL_WIDTH * currentX + 1, CELL_HEIGHT * currentY + 1);
				Piece piece = new Piece(sprite, currentX, currentY, j, gameRows-i-1);
				if(currentX == j && currentY == gameRows-i-1) wrongPositions --;
				pieces[currentY][currentX] = piece;
			}
		}
		
		this.menuDiv = new NinePatch(this.atlas.findRegion("menuskin"), 7, 7, 7, 7);
		spriteCross = new Sprite(atlas.findRegion("cross"));
		spriteTick = new Sprite(atlas.findRegion("tick"));
		spriteHelp = new Sprite(atlas.findRegion("help"));
		spriteMedal = new Sprite(atlas.findRegion("medal"));
		spriteRefesh = new Sprite(atlas.findRegion("refesh"));
		spriteSound = new Sprite(atlas.findRegion("sound"));
		
		spriteCross.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft -32 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		spriteRefesh.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 2 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		spriteSound.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 3 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		spriteMedal.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 4 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		spriteHelp.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 5 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
    }
    
	@Override
	public void show() {		
		profileService = new ProfileService();
		profile = profileService.retrieveProfile();		
		sound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/move.mp3", FileType.Internal));
		winsound = Gdx.audio.newSound(Gdx.files.getFileHandle("data/win.mp3", FileType.Internal));
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
    public void dispose() {
		super.dispose();
		profileService.persist();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0.84f, 0.92f, 0.93f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );        
		
		getBatch().setProjectionMatrix(camera.combined);
		getBatch().begin();
		
		if(renderHelp) {
			renderHelp();
		}
		else if(renderScore) {
			renderHighScore();
		} else {

			for (int i = 0; i <= this.gameRows; i++) {
				for (int j = 0; j < this.gameCols; j++) {
					if (this.pieces[i][j] != null) {
						this.pieces[i][j].getItem().draw(getBatch());
					}
				}
			}
			
			this.menuDiv.draw(getBatch(), CELL_WIDTH, GAME_VIEWPORT_HEIGHT - CELL_HEIGHT, CELL_WIDTH * (this.gameCols - 1), CELL_HEIGHT);

			this.spriteCross.draw(getBatch());
			this.spriteRefesh.draw(getBatch());
			this.spriteSound.draw(getBatch());
			this.spriteMedal.draw(getBatch());
			this.spriteHelp.draw(getBatch());

			long passTime = (System.currentTimeMillis() - this.startGameTime) / 1000L;
			String minute = String.valueOf(passTime / 60L);
			String second = String.valueOf(passTime % 60L);

			if (minute.length() == 1) minute = "0" + minute;
			if (second.length() == 1) second = "0" + second;
			String text = minute + ":" + second;

			if (!this.winTime.equals("")) text = this.winTime;
			getFont().setColor(Color.RED);
			getFont().draw(getBatch(), text, GAME_VIEWPORT_WIDTH - this.paddingLeft - getFont().getBounds(text).width - 5.0F, GAME_VIEWPORT_HEIGHT - 10.0F - 32.0F);

			if (this.wrongPositions == 0 && flagSaveScore){
				this.winTime = (minute + ":" + second);
				profile.notifyScore(type + "." + level, winTime);
				flagSaveScore = false;
				
				winMessage = true;
			}
			
			if(winMessage) {
				winMessage();
			}
		}
		
		getBatch().end();		
	}

	private void setupSound() {
		playSound = !playSound;
		if(playSound) {
			spriteSound = new Sprite(atlas.findRegion("sound"));
			spriteSound.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 3 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		} else {
			spriteSound = new Sprite(atlas.findRegion("mute"));
			spriteSound.setPosition(GAME_VIEWPORT_WIDTH - paddingLeft - 32 * 3 - 5, GAME_VIEWPORT_HEIGHT - 5 - 32);
		}
	}

	private void help()
	{
		this.renderHelp = (!this.renderHelp);
	}

	private void renderHelp()
	{
		menuDiv.draw(getBatch(), 0, 0, GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
		this.spriteTick.setPosition(GAME_VIEWPORT_WIDTH/2 - 32/2, CELL_HEIGHT - 40);
		this.spriteTick.draw(getBatch());
		
		Sprite help = new Sprite(this.atlas.findRegion(this.type + this.level));
		help.setSize(GAME_VIEWPORT_WIDTH - 10, GAME_VIEWPORT_HEIGHT - CELL_HEIGHT - 5);
		help.setPosition(5, CELL_HEIGHT);
		help.draw(getBatch());
	}

	private void highScore()
	{
		this.renderScore = (!this.renderScore);
	}

	private void renderHighScore()
	{
		menuDiv.draw(getBatch(), 0, 0, GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
		
		Array<String> textLst = profile.getHighScore(type + "." + level);
		getFont().setColor(Color.BLACK);
		String text = "HIGHSCORE";
		getFont().draw(getBatch(), text, GAME_VIEWPORT_WIDTH/2 - getFont().getBounds(text).width /2, GAME_VIEWPORT_HEIGHT - 20f);
		for(int i=0; i<Profile.number; i++) {
			text = (i+1) + ". " + textLst.get(Profile.number - 1 - i);
			getFont().draw(getBatch(), text, GAME_VIEWPORT_WIDTH/2 - getFont().getBounds(text).width /2, GAME_VIEWPORT_HEIGHT - 20f - (10f + getFont().getBounds(text).height) * (i+1));
		}		

		this.spriteTick.setPosition(GAME_VIEWPORT_WIDTH/2 - 32/2, GAME_VIEWPORT_HEIGHT - 20f - 32f - (10f + getFont().getCapHeight()) * (Profile.number + 1));
		this.spriteTick.draw(getBatch());
	}
	
	private void winMessage()
	{
		if(playSound) {winsound.play(1f);}
		
		float heightFr = 20f + 32f + (10f + getFont().getCapHeight()) * 2;
		menuDiv.draw(getBatch(), 0, 0, GAME_VIEWPORT_WIDTH, heightFr);
		
		getFont().setColor(Color.RED);
		String text = "YOU WIN";
		getFont().draw(getBatch(), text, GAME_VIEWPORT_WIDTH/2 - getFont().getBounds(text).width /2, heightFr - 20f);
		
		text = "Time" + winTime;
		getFont().draw(getBatch(), text, GAME_VIEWPORT_WIDTH/2 - getFont().getBounds(text).width /2, heightFr - 20f - (10f + getFont().getBounds(text).height));
		
		this.spriteTick.setPosition(GAME_VIEWPORT_WIDTH/2 - 32/2, heightFr - 20f - 32f - (10f + getFont().getCapHeight()) * 2);
		this.spriteTick.draw(getBatch());
	}

	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return true;
	}

	@Override
	public boolean keyTyped(char character) {		
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		try {
		//Click control button
		if(spriteCross.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1, 1))) {
			game.setScreen(new LevelScreen(game, type));
		}		
		else if(spriteRefesh.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1, 1))) {
			game.setScreen(new ActionScreen(game, type, level));
		}
		else if(spriteSound.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1, 1))) {
			setupSound();
		}
		else if(spriteMedal.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1, 1))) {
			highScore();
		}
		else if(spriteHelp.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1, 1))) {
			help();
		}
		else if (
			this.spriteTick.getBoundingRectangle().overlaps(new Rectangle(x, GAME_VIEWPORT_HEIGHT - y, 1.0F, 1.0F))
			&& (renderHelp || renderScore || winMessage)
		) {
			if(renderHelp) renderHelp = false;
			if(renderScore) renderScore = false;
			if(winMessage) winMessage = false;
		}
		else {
			int posY = (int)((GAME_VIEWPORT_HEIGHT - y)/CELL_HEIGHT), posX = (int)((x-paddingLeft)/CELL_WIDTH);
			if(x > paddingLeft && pieces[posY][posX] != null) {
				
				if(posX+1 == freeCol && posY==freeRow && canmove) {
					canmove = false;if(playSound) {sound.play(1f);}
					Piece tmp = pieces[posY][posX];
					tmp.setCurrentX(freeCol);
					tmp.getItem().setPosition(paddingLeft + CELL_WIDTH * tmp.getCurrentX() + 1, CELL_HEIGHT * tmp.getCurrentY() + 1);
					pieces[freeRow][freeCol] = tmp;
					if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()) wrongPositions --;
					else if(tmp.getCurrentX()==tmp.getRightX()+1 && tmp.getCurrentY()==tmp.getRightY()) wrongPositions ++;
					pieces[posY][posX] = null;
					freeCol = freeCol-1;
				}
				
				if(posX-1 == freeCol && posY==freeRow && canmove) {
					canmove = false;if(playSound) {sound.play(1f);}
					Piece tmp = pieces[posY][posX];
					tmp.setCurrentX(freeCol);
					tmp.getItem().setPosition(paddingLeft + CELL_WIDTH * tmp.getCurrentX() + 1, CELL_HEIGHT * tmp.getCurrentY() + 1);
					pieces[freeRow][freeCol] = tmp;
					if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()) wrongPositions --;
					else if(tmp.getCurrentX()==tmp.getRightX()-1 && tmp.getCurrentY()==tmp.getRightY()) wrongPositions ++;
					pieces[posY][posX] = null;
					freeCol = freeCol+1;
				}
				
				if(posY-1 == freeRow && posX==freeCol && canmove) {
					canmove = false;if(playSound) {sound.play(1f);}
					Piece tmp = pieces[posY][posX];
					tmp.setCurrentY(freeRow);
					tmp.getItem().setPosition(paddingLeft + CELL_WIDTH * tmp.getCurrentX() + 1, CELL_HEIGHT * tmp.getCurrentY() + 1);
					pieces[freeRow][freeCol] = tmp;
					if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()) wrongPositions --;
					else if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()-1) wrongPositions ++;
					pieces[posY][posX] = null;
					freeRow = freeRow+1;
				}
				
				if(posY+1 == freeRow && posX==freeCol && canmove) {
					canmove = false;if(playSound) {sound.play(1f);}
					Piece tmp = pieces[posY][posX];
					tmp.setCurrentY(freeRow);
					tmp.getItem().setPosition(paddingLeft + CELL_WIDTH * tmp.getCurrentX() + 1, CELL_HEIGHT * tmp.getCurrentY() + 1);
					pieces[freeRow][freeCol] = tmp;
					if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()) wrongPositions --;
					else if(tmp.getCurrentX()==tmp.getRightX() && tmp.getCurrentY()==tmp.getRightY()+1) wrongPositions ++;
					pieces[posY][posX] = null;
					freeRow = freeRow-1;
				}
			} 
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		canmove= true;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		return false;
	}
	

}
