package dovietkien.vn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
 
public class LevelScreen extends AbstractScreen
{
    // setup the dimensions of the menu buttons
    private static final float BUTTON_WIDTH = 150f;
    private static final float BUTTON_HEIGHT = 40f;
    private static final float BUTTON_SPACING = 10f;
    private String type;
 
    public LevelScreen( Another game , String type)
    {
        super( game );
        this.type = type;
    }
 
    @Override
    public void resize(int width, int height )
    {
        super.resize( width, height );
        final float buttonX = ( width - BUTTON_WIDTH ) / 2;
        float currentY = height - BUTTON_SPACING ;
 
        // label "welcome"
        Label welcomeLabel = new Label( "Make your choice!", new Label.LabelStyle(getFont(), Color.BLACK));
        welcomeLabel.setX(( ( width - welcomeLabel.getWidth() ) / 2 ));
        welcomeLabel.setY(( currentY - welcomeLabel.getPrefHeight()));
        stage.addActor( welcomeLabel );
        currentY = currentY - BUTTON_SPACING - welcomeLabel.getPrefHeight() - BUTTON_HEIGHT;
 
        TextButtonStyle style = new TextButtonStyle();
        style.font = getFont();
        style.fontColor = Color.WHITE;
        style.down = new NinePatchDrawable(new NinePatch(atlas.findRegion("HoverBtn"), 7, 7, 7, 7));
        style.up = new NinePatchDrawable(new NinePatch(atlas.findRegion("NormalBtn"), 7, 7, 7, 7));
        
        // button "start game"
        TextButton easyButton = new TextButton( "Easy", style );
        easyButton.setX(buttonX);
        easyButton.setY(currentY);
        easyButton.setWidth(BUTTON_WIDTH);
        easyButton.setHeight(BUTTON_HEIGHT);
        easyButton.addListener(new DefaultListener() {	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	game.setScreen(new ActionScreen(game, type, 1));
	        }
        });
        stage.addActor( easyButton );
 
        // button "hall of fame"
        TextButton mediumButton = new TextButton( "Medium", style );
        mediumButton.setX(buttonX);
        mediumButton.setY( currentY -= BUTTON_HEIGHT + BUTTON_SPACING );
        mediumButton.setWidth(BUTTON_WIDTH);
        mediumButton.setHeight(BUTTON_HEIGHT);
        mediumButton.addListener(new DefaultListener() {	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	game.setScreen(new ActionScreen(game, type, 2));
	        }
        });
        stage.addActor( mediumButton );
 
        // button "options"
        TextButton hardButton = new TextButton( "Hard", style );
        hardButton.setX(buttonX);
        hardButton.setY( currentY -= BUTTON_HEIGHT + BUTTON_SPACING );
        hardButton.setWidth(BUTTON_WIDTH);
        hardButton.setHeight(BUTTON_HEIGHT);
        hardButton.addListener(new DefaultListener() {	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	game.setScreen(new ActionScreen(game, type, 3));
	        }
        });
        stage.addActor( hardButton );
        
        // button "options"
        TextButton backButton = new TextButton( "Back", style );
        backButton.setX(buttonX);
        backButton.setY( currentY -= BUTTON_HEIGHT + BUTTON_SPACING );
        backButton.setWidth(BUTTON_WIDTH);
        backButton.setHeight(BUTTON_HEIGHT);
        backButton.addListener(new DefaultListener() {	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	game.setScreen(new MenuScreen(game));
	        }
        });
        stage.addActor( backButton );
    }
}
