import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

/*
 * Dibuja cualquier juego actual que esté pasando. Tiene un héroe y una variedad de 
 * habitaciones que se pueden dibujar e interactuar.
 */
public class GameScreen extends Pantalla{
    //Las Habitaciones en el juego
    public Vector<Lobby> lobbys;
    // El Héroe que se mostrará e interactuará con las Cosas en cada Lobby
    public Heroe heroe;
    // El índice del Lobby actual en los vestíbulos
    public int lobbyIndex;
    //verdadero si el otro nivel esta cargando, falso si no
    public boolean cargando;

    private TextoBoton etiquetaCarga;

    public GameScreen(){ //Constructor
        lobbys = new Vector<Lobby>();
        heroe = new Heroe();
        etiquetaCarga = new TextoBoton("CARGANDO", JGameMaker.FONT_GRANDE, Color.WHITE);
        etiquetaCarga.setPos((JGameMaker.screenWidth - etiquetaCarga.getWidth())/2, (JGameMaker.screenHeight - etiquetaCarga.getHeight())/2);
        cargando = false;
    }

    //Devuelve el lobby actual en el que se encuentra el Héroe
    public Lobby lobbyActual(){
        return lobbys.get(lobbyIndex);
    }

    public void draw(Graphics g) {  //Dibujar
        if(cargando){
                g.setColor(Color.BLACK);
            g.fillRect(0,0,JGameMaker.WIDTH,JGameMaker.HEIGHT);
            etiquetaCarga.draw(g);
        }else{
            lobbyActual().draw(g, null, heroe);
        }

    }

    //hace que el Héroe fije su posición donde sea que esté un TSpawn
    public void setSpawn(){
        for(int i = 0; i < lobbys.size(); i++){
            if(lobbys.get(i).setSpawn(heroe)){
                lobbyIndex = i;
                break;
            }
        }
    }

    /*
    * Inicializa el juego cargando un archivo
    * nos retornara verdadero si tiene éxito, falso si no
    */
    public boolean init(File f) throws Exception{
        return init(-1, f);
    }

    /*
    * Inicializa el juego cargando un archivo y cambiando el color del Héroe
    * nos retornara verdadero si tiene éxito, falso si no
    */
    public boolean init(int marioColor, File f) throws Exception{
        if(marioColor != -1){
            init(marioColor);
        }else{
            init();
        }
        if(f == null)return false;
        Vector<Vector<Serializer>> serializers;
        Object o = Serializer.fromFile(f);
        if(o == null || !(o instanceof Vector))return false;
        serializers = (Vector<Vector<Serializer>>)o;
        Vector<Serializer> s1 = serializers.get(0);
        Vector<Serializer> s2 = serializers.get(1);
        for(Serializer s: s1){
            Thing t = (Thing)s.getInstance().newInstance();
            t.init(s);
            lobbys.get(0).add(t, false);
        }
        for(Serializer s: s2){
            Thing t = (Thing)s.getInstance().newInstance();
            t.init(s);
            lobbys.get(1).add(t, false);
        }
        reiniciar();
        JGameMaker.actualizarTiempo();
        return true;
    }


    /*
    * guarda el nivel actual en un archivo
    * retornara verdadero si tiene éxito, falso si no tiene éxito
    */
    public boolean guardarJuego(File f){
        Vector<Vector<Serializer>> serializers = new Vector<Vector<Serializer>>();
        serializers.add(lobbys.get(0).serialize());
        serializers.add(lobbys.get(1).serialize());
        return Serializer.toFile(f, serializers);
    }

    //inicializa el juego (sin apertura de archivos)
    public void init(){
        heroe.init();
        lobbys = new Vector<Lobby>();
        Lobby overworld = new Lobby(false, lobbys.size());
        lobbys.add(overworld);
        Lobby underground = new Lobby(true, lobbys.size());
        lobbys.add(underground);
        lobbyIndex = 0;
        reiniciar();
        cargando = false;
        if(AePlayWave.fondoMusica != null){
            AePlayWave.fondoMusica.finalizarMusica();// quitar musica del lobby si se ha jugado una partida  
        }
        AePlayWave.fondoMusica = new AePlayWave("Sonidos/fondo2.wav");
        AePlayWave.fondoMusica.start(); // iniciar musica del Lobby
    }

    //inicializa el juego (sin abrir archivos) mientras configura el color del héroe
    public void init(int marioColor) {
        init();
        heroe.setSpriteColor(marioColor);
    }

    public void key(KeyEvent e, boolean pressed){
        if(cargando)return;
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_ESCAPE && pressed){
            controller.pause(true);
        }else if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            if(pressed){
                heroe.move(true);
            }else{
                heroe.stop(true);
            }
        }else if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            if(pressed){
                heroe.move(false);
            }else{
                heroe.stop(false);
            }
        }else if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP || code == KeyEvent.VK_SPACE){
            heroe.saltar(pressed);
        }else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
            heroe.crouch(pressed);
        }else if(code == KeyEvent.VK_M && AePlayWave.iMusica == true && AePlayWave.fondoMusica != null){ // si la musica esta encendida, la apaga
			AePlayWave.fondoMusica.finalizarMusica();// quitar musica si se presiona M 
                        AePlayWave.iMusica = false; // apago musica bool
	} // bug a corregir
//        else if(code == KeyEvent.VK_R && AePlayWave.iMusica == false){ // si la musica esta apagada, la enciende,presionando R
//                        AePlayWave.fondoMusica.start(); // iniciar musica del nivel
//                        AePlayWave.iMusica=true; // encender musica con bool
//                        
//		}
    }

    //Llamado después de la inicialización y cuando el Héroe muere
    public void reiniciar(){	
    }

    //Comportamiento del mouse 
    public void mouse(MouseEvent e, boolean pressed){	
    }

    //Pensamiento del juego
    public void think(){
        if(heroe.tuberiado()){
            lobbyIndex = heroe.getRoomAndSetNewPosition();	
        }
        if(heroe.vMuerte()){
            reiniciar();
        }
    }
}
