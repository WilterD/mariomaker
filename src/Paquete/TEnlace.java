import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Une objetos para que puedan interactuar. 
 * Por ejemplo, esto une tuberías para que el jugador pueda teletransportarse de una a otra.
 */
public class TEnlace extends TTool {
	
	Thing enlace;
	private Sprite preview = new Sprite("Imagenes/sprites/tools/enlace.png");
	
	public TEnlace(){
		super();
		enlace = null;
		//isInWorld = false;
	}
	public BufferedImage preview(){
		return preview.getBuffer();
	}
	
	/**
	 * 
	 * @retorna el enlace que está en la cola, si no hay ninguno entonces nulo
	 */
	public Thing getEnlace(){
		return enlace;
	}
	
	public boolean colocarEnlace(Thing t){
		return enlace != null && enlace.colocarEnlace(t);
	}
	
	public void enContacto(Thing t){
		if(t.colocarEnlace(enlace)){
			if(enlace == null){
				enlace = t;
			}else{
				t.enlace(enlace);
				enlace.enlace(t);
			}
		}
	}
	
	public void draw(Graphics g, ImageObserver o, Heroe heroe){
		super.draw(g,o,heroe);
		if(enlace == null || !enlace.inPlayerView(heroe))return;
		int[] c = enlace.getDrawCoords(heroe);
		g.setColor(Color.YELLOW);
		g.fillRect(c[0],c[1],c[2],c[3]);
	}
	
}
