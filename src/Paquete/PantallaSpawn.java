



import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;



/**
 * La característica más importante del Editor de niveles. Contiene todas las Cosas que se pueden generar en el juego y tiene la capacidad de agregarlas al juego.
 *
 */
public class PantallaSpawn extends Pantalla {
	
	private boolean visible;
	private int cosaElegida;
	private Heroe heroe;
	private int cosaFlotante;
	private boolean nivelPartida, congelarPartida;
        TextoBoton mensaje1,mensaje2;
        private static final Image ACERCA_DE = new Sprite("Imagenes/acercaDe.png").getImage();
        private static final Image MIS_CONTROLES = new Sprite("Imagenes/Controles.png").getImage();
	
	private TextoBoton cambiarNivel, congelarTiempo, controles;
	
	private static final Color  YELLOW = new Color(255,255,0,120),GREEN = new Color(0,255,0,120),RED = new Color(255,0,0,120);
	
               
	private Thing[] things = {
		new TGoomba(0, 0),
		new TKoopa(0,0),
		new TPirana(),
		new TTuberia(0,0),
		new TBloque(0,0,TBloque.LADRILLO_MARRON,null),
		new TBloque(TBloque.SUELO),
		new TBloque(TBloque.PILAR),
		new TBloque(TBloque.BLOQUE_PREGUNTA_DESACTIVADO),
		null,
		null,
		new TBloque(TBloque.HONGO_IZQUIERDO),
		new TBloque(TBloque.HONGO_MEDIO),
		new TBloque(TBloque.HONGO_DERECHO),
		new TBGBloque(TBloque.HONGO_ARRIBA),
		new TBGBloque(TBloque.HONGO_ABAJO),
                new TEstrella(),
		new TSpawn(),
		new TMeta(),
		new TLimiteHorizontal(),
		new TLimiteVertical(),
		new TEnlace(),
		new TRemover(),
		new TBloquesColores(),
		new AgujeroTierra(),
	};
	
	public PantallaSpawn(Heroe heroe){
		visible = false;
		nivelPartida = false;
		cosaElegida = 0;
		cosaFlotante = -1;
		cambiarNivel = new TextoBoton("CAMBIAR FONDO", JGameMaker.FONT_MEDIO, 160, 160 + 48*(things.length - 1 + 10)/10);		
                congelarTiempo = new TextoBoton("CONGELAR TIEMPO", JGameMaker.FONT_MEDIO, 160, 160 + 48*(things.length - 1 + 10)/10 + 20 + cambiarNivel.getHeight());
               		
		this.heroe = heroe;
            
                controles = new TextoBoton("Utiliza el Mouse para colocar objetos", JGameMaker.FONT_GRANDE, TextoBoton.TITULO,TextoBoton.TITULO);    
               
              
	}

	public void draw(Graphics g) {
           // mensaje1.draw(g); 
            // mensaje2.draw(g);
            if(MainScreen.vAcercaDe == true){
                g.drawImage(ACERCA_DE, (JGameMaker.screenWidth - ACERCA_DE.getWidth(null))/2, (JGameMaker.screenHeight - ACERCA_DE.getHeight(null))/2, null);
            }else if(MainScreen.misControles == true){
                g.drawImage(MIS_CONTROLES, (JGameMaker.screenWidth - MIS_CONTROLES.getWidth(null))/2, (JGameMaker.screenHeight - MIS_CONTROLES.getHeight(null))/2, null);
            }

           
		if(visible){
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                        
			for(int i = 0; i < things.length; i++){
                            
				if(things[i] == null)continue;
				BufferedImage img = things[i].preview();
				int x = 160+(i%10)*48;
				int y = 160+(i/10)*48;
				if(i == cosaFlotante)
					((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				int width = 32, height = 32*img.getHeight()/img.getWidth();
				if(i == cosaElegida){
					x -= width/2;
					y -= height/2;
					width *= 2;
					height *= 2;
				}
				
				g.drawImage(img,x,y,width,height,null);
				if(i == cosaFlotante)
					((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			}
		}else if(cosaElegida != -1){
		
			Thing t = things[cosaElegida];
			//Hace que el objeto gráfico se dibuje de forma opaca
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			t.draw((Graphics2D)g,null,heroe);
		}
		//se vuelve a la normalidad
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if(visible){
			cambiarNivel.draw(g);
			congelarTiempo.draw(g);
                        controles.draw(g);
                        
                        
                        
                        
		}
                
	}
	/**
	 * Devuelve verdadero si esto se puede ver en pantalla
	 */
	public boolean isVisible(){
		return visible;
	}

	public void key(KeyEvent e, boolean down) {
		int code = e.getKeyCode();
		switch(code){
			//hacer que el menú sea visible en estos botones
			case KeyEvent.VK_SHIFT:
				visible = down;
			break;
		}
		if(down){
			switch(code){
				//botones de acceso directo de conveniencia
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_R:
					cosaElegida =25; //remover indice
				break;
				case KeyEvent.VK_G:
					cosaElegida = 0; //goomba indice
				break;
				case KeyEvent.VK_K:
					cosaElegida = 1; //koopa indice
				break;
				case KeyEvent.VK_P:
					cosaElegida = 3; //tuberia indice
				break;
				case KeyEvent.VK_B:
					
					if(cosaElegida < 4 || cosaElegida > 6)
						cosaElegida = 4; //q bloque indice
					else
						cosaElegida++;
				break;
				case KeyEvent.VK_C:
					if(cosaElegida == 26){
						TBloquesColores.cycleColors();
						things[cosaElegida].init();
					}else{
						cosaElegida = 26;
					}
				break;
			}
		}
	}
	/**
	 * devuelve verdadero si esto debería cambiar si todas las Cosas en el juego deben dejar de moverse o no
	 */
	public boolean shouldToggleFreeze(){
		boolean temp = congelarPartida;
		congelarPartida = false;
		return temp;
	}
	
	/**
	 * devuelve verdadero si esto debería cambiar el panorama
	 */
	public boolean shouldToggleLevel(){
		boolean temp = nivelPartida;
		nivelPartida = false;
		return temp;
	}
	
	
	/**
	 * devuelve el color t debe estar resaltado (usado para herramientas)
             @param t lo que se compara
* @retorna a color si se debe resaltar t, nulo si no se debe
	 */
	public Color highlightColor(Thing t){
		Thing chosen = things[cosaElegida];
		if(!(chosen.tocando(t) && t.tocando(chosen))){
			return null;
		}
		if(chosen instanceof TRemover){
			return RED;
		}else if(chosen instanceof TEnlace){
			TEnlace linker = (TEnlace)chosen;
			if(t.colocarEnlace(linker.getEnlace())){
				return YELLOW;
			}
		}else if(chosen instanceof TPirana){
			if(t instanceof TTuberia && ((TTuberia)t).getPirana() == null){
				return YELLOW;
			}
		}else if(chosen instanceof TItem && t instanceof TBloque && ((TBloque)t).canAcceptItem()){
			return GREEN;
		}
		return null;
	}
	
	/**
	 * obtiene la Cosa que debería generarse y la devuelve. La Cosa devuelta se elimina de PantallaSpawn y se crea una nueva instancia para ocupar su lugar.
* @return the Thing para ser generado
	 */
	public Thing getSpawn(){
		if(visible)return null;
		Thing temp = things[cosaElegida];
		if(temp instanceof TEnlace && ((TEnlace)temp).getEnlace() == null){
			//no hagas uno nuevo.
		}else{
			try {
				things[cosaElegida] = temp.getClass().newInstance();
			} catch (Exception e) {
				things[cosaElegida] = null;
				e.printStackTrace();
			}
		}	
		things[cosaElegida].init(temp.serialize());
		return temp;
	}
	/**
	 * devuelve la Cosa que está seleccionada en la PantallaSpawn
* @retorna la Cosa que está seleccionada en la PantallaSpawn
	 */
	public Thing pasarSpawn(){
		return things[cosaElegida];
	}

	public void mouse(MouseEvent e, boolean down) {
		int x = e.getX(), y = e.getY();
		if(down && visible){
			int temp = getIndex(x, y);
			if(temp == cosaElegida){
				if(things[cosaElegida] instanceof TBloquesColores){
					TBloquesColores.cycleDirections();
					things[cosaElegida].init();
				}
			}else if(temp != -1){
				cosaElegida = temp;
			}
			if(cambiarNivel.contains(x,y)){
				nivelPartida = true;
			}else if(congelarTiempo.contains(x,y)){
				congelarPartida =  true;
			}
		}
	}

	public void think() {
		Thing t = things[cosaElegida];
		t.setSpawnPos(ScreenManager.mouse.x +heroe.pos.x - (JGameMaker.screenWidth/2.0 + heroe.xOffset()),JGameMaker.screenHeight + heroe.pos.y + 32 - (ScreenManager.mouse.y + heroe.yOffset()));
		t.vel.x = 0;
		t.vel.y = 0;
		t.acc.x = 0;
		t.acc.y = 0;
		t.think();
		if(!visible)return;
		cosaFlotante = getIndex(ScreenManager.mouse.x, ScreenManager.mouse.y);
                
	}
	private int getIndex(int x, int y){
		for(int i = 0; i < things.length; i++){
			if(things[i] == null)continue;
			int tx = 160+(i%10)*48;
			int ty = 160+(i/10)*48;
			Rectangle mensaje1 = new Rectangle(tx,ty,32,32);
			if(mensaje1.contains(x,y)){
				return i;
			}
		}
		return -1;
	}

}
