package dovietkien.vn;

import com.badlogic.gdx.Game;


public class Another extends Game {
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
