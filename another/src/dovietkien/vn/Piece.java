package dovietkien.vn;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Piece {
	
	Sprite item;
	int currentX;
	int currentY;
	int rightX;
	int rightY;
	
	
	public Piece(Sprite item, int currentX, int currentY, int rightX, int rightY) {
		super();
		this.item = item;
		this.currentX = currentX;
		this.currentY = currentY;
		this.rightX = rightX;
		this.rightY = rightY;
	}
	
	public Sprite getItem() {
		return item;
	}
	public void setItem(Sprite item) {
		this.item = item;
	}
	public int getCurrentX() {
		return currentX;
	}
	public void setCurrentX(int currentX) {		
		this.currentX = currentX;
	}
	public int getCurrentY() {
		return currentY;
	}
	public void setCurrentY(int currentY) {
		this.currentY = currentY;		
	}
	public int getRightX() {
		return rightX;
	}
	public void setRightX(int rightX) {
		this.rightX = rightX;
	}
	public int getRightY() {
		return rightY;
	}
	public void setRightY(int rightY) {
		this.rightY = rightY;
	}
		
	
}
