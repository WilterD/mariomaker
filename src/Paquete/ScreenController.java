import java.io.File;

/**
 * Esta clase controla el ScreenManager. 
 *
 */
public class ScreenController {
	private ScreenManager pantallaPanel;
	private FileOpener opener;
	private File nivel;
	private File nivelActual;
	private int marioImagen;
	/**
	 * Crea una nueva instancia de ScreenController, el administrador de control y el uso de opener 
         * como abridor de archivos.
          el ScreenManager a controlar
          el FileOpener que se usará al abrir archivos
	 */
	public ScreenController(ScreenManager manager, FileOpener opener){
		this.pantallaPanel = manager;
		this.opener = opener;
		nivel = null;
		marioImagen = -1;
	}
	/**
	 * pausa/despausa el juego
         verdadero si quiere hacer una pausa, falso si quiere reanudar
	 */
	public void pause(boolean pause){
		pantallaPanel.pause(pause);
	}
	/**
	 * crea un nuevo Editor de niveles
*       marioImagen el color del Héroe
	 */
	public void nivelEditor(int marioImagen){
		pantallaPanel.nivelEditor(marioImagen);
		pause(false);
	}
	
	/**
	 * crea un nuevo jugador de niveles
*       marioImagen el color del Héroe
	 */
	public void unJugador(int marioImagen){
		nivel = opener.openFile();
		nivelActual = nivel;
		this.marioImagen = marioImagen;
		pantallaPanel.unJugador(marioImagen, nivel);
	}
	
	/**
	 * se llama cuando se presiona Reiniciar Nivel en PauseScreen en UnJugador. Restablece el nivel actual de nuevo a lo que era originalmente para que se pueda jugar de nuevo */
	public void reiniciarUnJugador(){
		pantallaPanel.unJugador(marioImagen, nivelActual);
	}
	
	/**
	 * retorna el menuPrincipal
	 */
	public void menuPrincipal(){
		pantallaPanel.renew();
	}
	
	/**
	 * Si está en el modo de editor de niveles, solicita al usuario que seleccione un archivo y guarda el nivel en ese archivo
* retorna verdadero si el juego fue guardado, falso si no
	 */
	public boolean guardarJuego(){
		return pantallaPanel.guardarJuego(opener.saveFile());
	}
	
	/**
	 * Pide al usuario que seleccione un archivo y abre el editor de niveles para editar ese archivo
	 */
	public void cargarNivelEditor(int marioImagen){
		pantallaPanel.nivelEditor(marioImagen,opener.openFile());
	}
	
	/**
	 * se llama cuando en UnJugador el Héroe llega a la meta. Intenta abrir el siguiente nivel usando el .wcfg. Si no se encuentra, vuelve al menú principal.
	 */
	public void proximoNivel(){
		
		String lvl = nivel.getPath();
		String path, filename;
		{
			String[] split = lvl.split("/");
			filename = split[split.length - 1];
			if(split.length > 1){
				StringBuffer buffer = new StringBuffer();
				for(int i = 0; i < split.length - 1; i++){
					buffer.append(split[i]);
					buffer.append('/');
				}
				path = buffer.toString();
			}else{
				path = "";
			}
		}
		String file = path + filename.substring(0,filename.length() - "jgamemaker".length()) + "wcfg";
		boolean loaded = false;
		if(opener.readFile(file)){
			String line;
			while((line = opener.readLine()) != null){
				if(line.equals(nivelActual.getName())){
					line = opener.readLine();
					if(line == null){
						break;
					}
					try {
						File fcLevel = new File(path + line);
						if(!fcLevel.getPath().equals(nivelActual.getPath()) && opener.readFile(fcLevel)){	
							nivelActual = fcLevel;
							pantallaPanel.unJugador(marioImagen, nivelActual);
							loaded = true;
							break;
						}else{
							continue;
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
						loaded = false;
						break;
					}
				}
			}
		}
		if(!loaded){
			menuPrincipal();
		}
		
	}
}
