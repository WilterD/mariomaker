


import java.awt.*;
import java.awt.geom.*;

/**
 * Clase auxiliar utilizada para dibujar cadenas en la pantalla.
 *
 */
public class TextoBoton {
	private String str;
	private Font font;
	private Rectangle limites;
	private Color highlight, color;
	
	/**
	 * colores predeterminados
	 */
	public static final Color	TITULO = Color.RED,TEXT = Color.WHITE,HIGHLIGHT = new Color(180,180,180);
	
	/**
	 * Crea un nuevo TextoBoton con los atributos especificados
* str texto a mostrar
* fuente, fuente del texto
* x la parte más a la izquierda del texto
* y la parte superior del texto
* color, color predeterminado
* highlight el color cuando el mouse se desplaza sobre
	 */
	public TextoBoton(String str, Font font, int x, int y, Color color, Color highlight){
		this.str = str;
		this.color = color;
		this.highlight = highlight;
		this.font = font;
		Rectangle limitesCadena = limitesCadena(str,font);
		limites = new Rectangle(x,y,limitesCadena.width, limitesCadena.height);
	}
	public TextoBoton(String str, Font font, int x, int y){
		this(str,font);
		setPos(x,y);
	}
	
	public TextoBoton(String str, Font font, int x, int y, Color color){
		this(str,font,x,y,color,color);
	}

	public TextoBoton(String str, Font font, Color color, Color highlight){
		this(str,font,0,0,color,highlight);
	}
	public TextoBoton(String str, Font font, Color color){
		this(str,font,color,color);
	}
	public TextoBoton(String str, Font font){
		this(str,font,TEXT,HIGHLIGHT);
	}
	
	/**
	 * determina si p está dentro de los límites de este
* retorna verdadero si p está dentro de los límites de this, falso si no
	 */
	public boolean contains(Point p){
		return limites.contains(p);
	}
	/**
	 * determina si un punto en las coordenadas (x,y) está dentro de los límites de este
* retorna verdadero si (x,y) está dentro de los límites de esto, falso si no
	 */
	public boolean contains(int x, int y){
		return limites.contains(x,y);
	}
	public int getWidth(){
		return limites.width;
	}
	public int getHeight(){
		return limites.height;
	}
	public void setPos(Point p){
		limites.setLocation(p);
	}
	public void setPos(int x, int y){
		limites.setLocation(x,y);
	}
	
	public void setText(String str){
		this.str = str;
		Rectangle limitesCadena = limitesCadena(str,font);
		limites = new Rectangle(limites.x,limites.y,limitesCadena.width, limitesCadena.height);
		
	}
	
	//codigo complicado
	private Rectangle limitesCadena(String str, Font font){
		Graphics g = (new Sprite(1,1)).getBuffer().getGraphics();
		Rectangle2D limitesCadena = g.getFontMetrics(font).getStringBounds(str,g);
		return new Rectangle(0,0,(int)(limitesCadena.getWidth() + 0.5),(int)((limitesCadena.getHeight() + 0.5)/2));
	}
	
	/**
	 * dibuja this hasta g 
	 * mouse coordenadas del mouse
	 */
	public void draw(Graphics g, Point mouse){
		g.setFont(font);
		if(contains(mouse)){
			g.setColor(highlight);
		}else{
			g.setColor(color);
		}
		g.drawString(str, limites.x, limites.y + limites.height);
	}
	/**
	 * dibuja esto a g, las coordenadas del mouse se determinan a partir de ScreenPanel.mouse
	 */
	public void draw(Graphics g){
		draw(g,ScreenManager.mouse);
	}
	
}
