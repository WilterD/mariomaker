import java.awt.*;
import java.awt.event.MouseEvent;

// Modificar archivos, import
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/*
 * Esta clase en realidad juega a través de los niveles que se crean con NivelEditor.
 */
public class UnJugador extends GameScreen{

	TextoBoton mensaje1,mensaje2;
	
	private boolean ganador;
	
	private boolean debeCargarNivel;
	
	private static final Image HAS_GANADO = new Sprite("Imagenes/marioganador.gif").getImage();
	
	private double tiempoCelebracion;
	
	public UnJugador(){  //Mensajes dentro del juego (Constructor)
            super();
            mensaje1 = new TextoBoton("CUIDA TU AMBIENTE",JGameMaker.FONT_GRANDE, 10,10, java.awt.Color.WHITE);
            mensaje2 = new TextoBoton("RECICLA LOS MATERIALES REUTILIZABLES",JGameMaker.FONT_GRANDE, 10, 12 + mensaje1.getHeight(), java.awt.Color.WHITE);
	}

	public void init(int marioImagen){ //En carga o ejecucion 
            super.init(marioImagen);
            ganador = false;
            debeCargarNivel = false;
            tiempoCelebracion = 0;
            
            sumarEstadistica(3,"Partidas Jugadas: ,"); // le envio el numero de posicion de opcion, de lo que debe sumar y el nombre de la cantidad a sumar
             
            
	}
	
	public void draw(Graphics g) { //Dibujar
            super.draw(g);
            if(cargando)return;
            mensaje1.draw(g);
            mensaje2.draw(g);
            if(ganador)
                g.drawImage(HAS_GANADO, (JGameMaker.screenWidth - HAS_GANADO.getWidth(null))/2, (JGameMaker.screenHeight - HAS_GANADO.getHeight(null))/2, null);
            
	}
        
	public void think() { //Pensamiento del juego
            super.think();
            lobbyActual().think(heroe,false);
            if(heroe.ganador() && !ganador){
                ganador = true;
                tiempoCelebracion = 6500/15.0;
                AePlayWave.fondoMusica.finalizarMusica(); // acabar musica de fondo

                AePlayWave.fondoMusica = new AePlayWave("Sonidos/ganador.wav"); 
                AePlayWave.fondoMusica.start(); // colocar musica de ganador de fondo
                // sumar estadisticas
                sumarEstadistica(1,"Partidas Ganadas: ,"); // le envio el numero de posicion de opcion, de lo que debe sumar y el nombre de la cantidad a sumar
            }
            if(debeCargarNivel){
                debeCargarNivel = false;
                controller.proximoNivel();

                cargando = false;
            }
            if(cargando){
                debeCargarNivel = true;
            }
            if(ganador && tiempoCelebracion > 0){
                tiempoCelebracion -= JGameMaker.time();
                if(tiempoCelebracion <= 0){
                    cargando = true;
                    ganador = false;
                }
            }
	}
	
        //Guardar nivel creado
	public boolean guardarJuego(File f) {
            return false;
	}
	
        //Iniciar de nuevo nivel
	public void reiniciar() {
            if(heroe == null)return;
            if(heroe.vMuerte() && !ganador){
                sumarEstadistica(5,"Partidas Perdidas: ,"); // le envio el numero de posicion de opcion, de lo que debe sumar y el nombre de la cantidad a sumar
                controller.pause(true);
                
            }else{
                setSpawn();
            }
	}
        
        // Modificar Contenido de Archivo
        
        static void modificarArchivo(String filePath, String oldCadena, String newCadena) {
        File archivoModificar = new File(filePath);

        String oldContenido = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(archivoModificar));

            //Leer todas las líneas del archivo de texto de entrada en oldContenido
            String line = reader.readLine();

            while (line != null) {
                oldContenido = oldContenido + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Reemplazo de oldString con newString en el contenido antiguo
            String newContent = oldContenido.replaceAll(oldCadena, newCadena);

            //Reescribiendo el archivo de texto de entrada con newContenido
            writer = new FileWriter(archivoModificar);

            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
        
    static void sumarEstadistica(int opc, String nombreEstadistica){
        
            //opc
            // partidas ganadas =1
            // partidas jugadas =3
            // partidas perdidas =5
            // partidas abandonadas =7
            
                String rutaArchivo="C:\\Users\\DELL\\Documents\\NetBeansProjects\\MarioMakerGithub\\mariomaker\\src\\Paquete\\estadisticas.txt";
            
            // Sumar Puntos
            
            Path archivo = Paths.get(rutaArchivo);

        try {
            String contenido = Files.readString(archivo);
            
            String miPalabra = ""; // inicializar
            
            for (int n = 0; n < contenido.length(); n++) {
                char c = contenido.charAt(n);

                c = contenido.charAt(n); // obtengo el caracter siguiente de la cadena
                miPalabra = miPalabra + String.valueOf(c); // obtener palabra completa

                String[] CadenaSeparada = miPalabra.replaceAll("\\[", "").replaceAll("]", "").split(",");

                int[] intArray = new int[CadenaSeparada.length];
                
                for (int i = 0; i < CadenaSeparada.length; i++) {

                    try {
                        intArray[i] = Integer.parseInt(CadenaSeparada[i]);

                    } catch (Exception e) {
                        // System.out.println("error " + e.getMessage());
                    }
                }
                
                if(n+1 == contenido.length()){ // ultima vuelta
                    int numeroSuma=intArray[opc]+1; // aumento valor
                    modificarArchivo(rutaArchivo,nombreEstadistica+intArray[opc]+",",nombreEstadistica+numeroSuma+",");

                }
                
             //   System.out.println(contenido); // mostrar contenido de Estadisticas.txt
            
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
                //fin 
        
    }

}
