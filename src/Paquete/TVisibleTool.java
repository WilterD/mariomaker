import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Una herramienta que no se elimina instantáneamente del lobby después de la creación.
 * Por defecto tiene un ancho y alto de 32.
 */
public class TVisibleTool extends TTool {
	

	public TVisibleTool(){
		this(32,32);
	}
	
	public TVisibleTool(int width, int height){
		this(width,height,null);
	}
	
	public TVisibleTool(int width, int height, Color color){
		super(width,height,color);
	}
	
	public void think(){
		boolean muerte = this.asesinado();
		super.think();
		if(!muerte){
			revive();
		}
	}
	
}
