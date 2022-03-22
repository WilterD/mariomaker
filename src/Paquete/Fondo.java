


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;


import java.awt.Color;

/**
 * Representa el Fondo en el juego
 *
 */
public class Fondo{
	
	private static final Image
		NUBES = (new ImageIcon("Imagenes/nubes.gif")).getImage(),
		LADRILLOS = (new ImageIcon("Imagenes/bloqueOscuro.gif")).getImage(),
		SUELO = (new ImageIcon("Imagenes/suelo.jpg")).getImage();
	
	private boolean underground;
	
	public static final Color
		COLOR_NUBES = new Color(63,191,255),
		UNDERGROUND_COLOR = Color.BLACK;
	
	private static final double
		NUBES_MULTI = 0.1,
		TIERRA_MULTI = 1.0;
	
	/**
	 * crear nuevo fondo
	 *  verdadero si debe ser un tema underground, falso si no
	 */
	public Fondo(boolean underground){
		this.underground = underground;
    }
	
	/**
	 * determina si el tema es underground
*           retorna verdadero si el tema es underground
	 */
	public boolean isUnderground(){
		return underground;
	}
    /**
     * dibuja el fondo a la pantalla
     * @usando g
     * @usando o
     *la clase heroe que se usará para verificar el desplazamiento de parallax 
     */
    public void draw(Graphics g, java.awt.image.ImageObserver o, Heroe heroe){
		double viewX = heroe.viewX();
		double viewY = heroe.viewY();
		if(underground)
			g.setColor(UNDERGROUND_COLOR);
		else
			g.setColor(Fondo.COLOR_NUBES);
		g.fillRect(0, 0, JGameMaker.screenWidth, JGameMaker.screenHeight);
    	Image ground;
    	int groundHeight,groundWidth;
    	if(!underground){
    		ground = SUELO;
	    	groundHeight = ground.getHeight(o);
	    	groundWidth = ground.getWidth(o);
	    	int skyHeight = NUBES.getHeight(o)*3,
	    		skyWidth = NUBES.getWidth(o)*3;
	    		
	    	
	    	double x = -viewX*NUBES_MULTI;
	    	double y = (viewY - (JGameMaker.screenHeight - JGameMaker.NIVEL_SUELO))*NUBES_MULTI;
	    	while(x > 0)
	    		x -= skyWidth;
	    	((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
	    	for(; x < JGameMaker.screenWidth; x+=skyWidth){
	    		g.drawImage(NUBES, JGameMaker.scaleW(x),JGameMaker.scaleH(y),JGameMaker.scaleW(skyWidth),JGameMaker.scaleH(skyHeight),o);
	    	}
	    	((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


    	}else{
    		ground = LADRILLOS;
	    	groundHeight = ground.getHeight(o)*2;
	    	groundWidth = ground.getWidth(o)*2;
    	}
    	
    	
    	for(double y = viewY; y < JGameMaker.screenHeight; y+=groundHeight){
    		double x = viewX;
	    	x *= -TIERRA_MULTI;
	    	while(x > 0)
	    		x -= groundWidth;
	    		
	    	for(; x < JGameMaker.screenWidth; x+=groundWidth){
	    		g.drawImage(ground, JGameMaker.scaleW(x),JGameMaker.scaleH(y),JGameMaker.scaleW(groundWidth),JGameMaker.scaleH(groundHeight),o);
	    	}
    	}
    }
	
	
}