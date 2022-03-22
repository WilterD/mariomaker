


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Esta clase contiene todas las pantallas que se mostrarán en el juego y también cambia desde ellas cuando es necesario. 
 * Al crear una nueva pantalla, establece su ScreenController al mismo que este, por lo que para cambiar cualquier cosa en ScreenManager 
 * simplemente se llama a los métodos en ScreenController y funciona
 *
 */
public class ScreenManager implements MouseListener, MouseMotionListener,KeyListener{
	
	private GameScreen juego;
	private Pantalla menu;
	
	private boolean pausado;
	private ScreenController controller;
	
	/**
	 * posición del ratón en la pantalla
	 */
	public static Point mouse = new Point();

	private ArrayList<Integer> teclaPresionada;
	
	
	private boolean thinked = true;
	
	/**
	 * crea un nuevo ScreenManager con un ScreenController con un FileOpener como se especifica
*           el FileOpener que se colocará dentro del ScreenController creado
	 */
	public ScreenManager(FileOpener opener){
		teclaPresionada = new ArrayList<Integer>();
		controller = new ScreenController(this,opener);
		renew();
	}
	
	/**
	 * actualiza todo a donde es como si acabara de abrir el programa
	 */
	public void renew(){
		juego = null;
		menu = new MainScreen();
		menu.controller = controller;
		pausado = true;
		if(AePlayWave.iMusica==false){
                AePlayWave.fondoMusica = new AePlayWave("Sonidos/fondo.wav");
                AePlayWave.fondoMusica.start(); // inicia musica principal del juego de fondo
                AePlayWave.iMusica=true;
                }else{
                         AePlayWave.fondoMusica.finalizarMusica();
                         AePlayWave.fondoMusica = new AePlayWave("Sonidos/fondo.wav");
                         AePlayWave.fondoMusica.start(); // apagar musica de fondo, y reproducir musica principal del juego de fondo
                         
                        }
		
		
	}
	
	/**
	 * corrige la sincronización con el dibujo y el pensamiento
	 */
	public boolean didThink(){
		return thinked;
	}
	
	/**
	 * dibuja el menú Pantalla y juego GameScreen
*           g los Gráficos a ser dibujados
	 */
	public void draw(Graphics g){
		//System.out.println("dibuja");
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		

		if(!pausado && juego != null){
			juego.draw(g);
		}else{
			if(menu instanceof PauseScreen)
				juego.draw(g);
			menu.draw(g);
		}
		thinked = false;
		
	}
	
	/**
	 * inicia el modo editor de nivel (archivo nuevo)
	 * marioColor el color que debe tener el personaje
	 */
	public void nivelEditor(int marioColor){
		juego = new NivelEditor();
		menu = new PauseScreen(true);
		juego.controller = controller;
		menu.controller = controller;
		juego.init(marioColor);
		
	}
	
	/**
	 * inicia el modo editor de nivel (abrir archivo)
* marioColor, el color que debe tener el personaje
* f el archivo a abrir que contiene los datos de nivel
	 */
	public void nivelEditor(int marioColor, File f){
		juego = new NivelEditor();
		boolean init;
		try{
			init = juego.init(marioColor, f);
		}catch(Exception ex){
			if(menu instanceof MainScreen)
				((MainScreen)menu).cargaFallida();
			juego = null;
			return;
		}
		if(!init){
			juego = null;
			return;
		}
		pausado = false;
		menu = new PauseScreen(true);
		juego.controller = controller;
		menu.controller = controller;
		
	}
	
	/**
	 * guarda el nivel, si lo hay, en el archivo
*       f el archivo en el que se escribirá
*       retorna verdadero si se escribió con éxito, falso si no
	 */
	public boolean guardarJuego(File f){
		if(juego != null){
			return juego.guardarJuego(f);
		}
		return false;
	}
	

	/**
	 * inicia el modo de un solo jugador
*           f el archivo a abrir y leer
	 */
	public void unJugador(int marioColor, File f){
		if(juego == null || !(juego instanceof UnJugador))
			juego = new UnJugador();
		boolean init;
		juego.cargando = true;
		try{
			init = juego.init(marioColor, f);
		}catch(Exception ex){
			if(menu instanceof MainScreen)
				((MainScreen)menu).cargaFallida();
			juego = null;
			return;
		}
		if(!init){
			juego = null;
			return;
		}
		pausado = false;
		menu = new PauseScreen(false);
		juego.controller = controller;
		menu.controller = controller;
	}
	
	/**
	 * llamó a cada cuadro, las actualizaciones piensan en GameScreen y el menú de la Pantalla.
	 */
	public void think(){
		//System.out.println("Think ---");
		if(!pausado){
			juego.think();
		}else{
			menu.think();
		}
		thinked = true;
	}
	
	/**
	 * pausa/reanuda el juego, si uno está en sesión
* pause si es verdadero, el juego se detiene. si es falso, se reactiva.
	 */
	public void pause(boolean pause){
		pausado = pause;
	}
	
	/**
	 * se llama cuando se presiona una tecla
* ejecuta KeyEvent de keyPressed/keyReleased
* Es verdadero si la tecla fue presionada, falso si no
	 */
	public void key(KeyEvent event, boolean pressed){
		if(!pausado){
			juego.key(event, pressed);
		}else{
			menu.key(event, pressed);
		}
	}

	/**
	 * se llama cuando se presiona un botón del mouse
* ejectuta evento del mouse, si se presiona un clic
* si tecla del mouse es presionado es verdadero, es falso si no
	 */
	public void mouse(MouseEvent event, boolean pressed){
		if(pausado){
			menu.mouse(event, pressed);
		}else{
			juego.mouse(event,pressed);
		}
	}
	
	public void keyPressed(KeyEvent event){
		int code = event.getKeyCode();
		if(teclaPresionada.size() != 0 && teclaPresionada.get(teclaPresionada.size()-1).equals(code) || teclaPresionada.indexOf(code) != -1)
			return;
		teclaPresionada.add(code);
		key(event,true);
	}
	public void keyReleased(KeyEvent event){
		int code = event.getKeyCode();
		int index = teclaPresionada.indexOf(code);
		if(index != -1)
			teclaPresionada.remove(index);
		key(event,false);
	}
	
	
	public void keyTyped(KeyEvent event){}
	
	public void mouseClicked(MouseEvent event){}
	public void mousePressed(MouseEvent event){
		mouse(event,true);
	}
	public void mouseReleased(MouseEvent event){
		mouse(event,false);
	}
	public void mouseExited(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	
	public void mouseMoved(MouseEvent event){
		mouse.setLocation(event.getX(),event.getY());	
	}
	public void mouseDragged(MouseEvent event){
		mouseMoved(event);
	}
	
}