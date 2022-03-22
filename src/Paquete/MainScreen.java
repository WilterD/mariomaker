import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/*
* Clase donde se registran representaciones de salida de pantalla
* de objetos, menu y mas.
*/
public class MainScreen extends Pantalla {
    private Lobby lobby;

    
    private int marioSeleccionado;

    private final BufferedImage[] MARIO_COLORS = {
        null,
        null,
        null,
        null,
        null,
        null
    };

    private TextoBoton editorButton,nuevoJuego,cargarJuego,botonUnico,titulo,errorCargar,salir,controles,acercaDe;

    private static final int ESPACIO_ARRIBA = 150, ESPACIO_ENTRE_TITULOS = 2;

    private static final Font FONT_TITULO = new Font("Courier", Font.PLAIN, 100);

    private boolean editorSeleccionado, cargaFallida;
    static boolean vAcercaDe=false;
    static boolean misControles=false;

    public MainScreen(){ //Constructor
    editorSeleccionado = false;
    cargaFallida = false;

    //Personajes y objetos a utilizar
    for(int i = 0; i < MARIO_COLORS.length; i++){
        Heroe h = new Heroe();
        h.setSpriteColor(i);
        MARIO_COLORS[i] = (h.IMAGEN[0]).getBuffer();
    }
    marioSeleccionado = (int)(Math.random()*6);
    boolean underground = Math.random() > 0.5;
    lobby = new Lobby(underground, -1);
    
   
    lobby.add(new TBloque(32*5,32*4, TBloque.BLOQUE_PREGUNTA));
    lobby.add(new TBloque(32*4,32*2, TBloque.LADRILLOS));
    lobby.add(new TBloque(32*3,32*2, TBloque.LADRILLOS));
    lobby.add(new TBloque(32*5,32*2, TBloque.LADRILLOS));
    lobby.add(new TBloque(32*6,32*2, TBloque.BLOQUE_PREGUNTA_DESACTIVADO));
    lobby.add(new TBloque(32*7,32*2, TBloque.LADRILLOS));
    TTuberia tuberia = new TTuberia();
    tuberia.setPos(-32*7, 32*5);
    TPirana pirana = new TPirana();
    lobby.add(tuberia);
    lobby.add(pirana);
    tuberia.addPirana(pirana);
    lobby.add(new TBloque(-32*5, 0, TBloque.LADRILLOS, null, true));

    //Enunciados del menu 
    nuevoJuego = new TextoBoton("NUEVO JUEGO", JGameMaker.FONT_GRANDE);
    cargarJuego = new TextoBoton("CARGAR JUEGO", JGameMaker.FONT_GRANDE);
    editorButton = new TextoBoton("CREAR MAPA", JGameMaker.FONT_GRANDE);
    botonUnico = new TextoBoton("UN SOLO JUGADOR", JGameMaker.FONT_GRANDE);
    errorCargar = new TextoBoton("ERROR AL CARGAR JUEGO", JGameMaker.FONT_GRANDE, TextoBoton.TITULO);
    titulo = new TextoBoton("JGameMaker", FONT_TITULO, Color.WHITE);
    controles = new TextoBoton("CONTROLES", JGameMaker.FONT_GRANDE);
    acercaDe = new TextoBoton("ACERCA DE", JGameMaker.FONT_GRANDE);
    salir = new TextoBoton("SALIR", JGameMaker.FONT_GRANDE);
    int height = editorButton.getHeight();

    titulo.setPos(0, ESPACIO_ARRIBA - titulo.getHeight() - 50);
    editorButton.setPos(0, ESPACIO_ARRIBA);
    nuevoJuego.setPos(0, ESPACIO_ARRIBA);
    cargarJuego.setPos(nuevoJuego.getWidth() + ESPACIO_ENTRE_TITULOS*20, ESPACIO_ARRIBA);
    botonUnico.setPos(0, ESPACIO_ARRIBA + height + ESPACIO_ENTRE_TITULOS);
    errorCargar.setPos(0,ESPACIO_ARRIBA + (height + ESPACIO_ENTRE_TITULOS) * 2);
    controles.setPos(0, ESPACIO_ARRIBA + (height + ESPACIO_ENTRE_TITULOS)*3);
    acercaDe.setPos(0, ESPACIO_ARRIBA + (height + ESPACIO_ENTRE_TITULOS)*5);
    salir.setPos(0, ESPACIO_ARRIBA + (height + ESPACIO_ENTRE_TITULOS)*7);
    }

    public void cargaFallida(){ //Carga de fallo
        cargaFallida = true;
        editorSeleccionado = false;
    }

    public void draw(Graphics g) { //Dibujar 
        lobby.draw(g,null,null);
        titulo.draw(g);
        if(!editorSeleccionado){
        editorButton.draw(g);
        }else{
            cargarJuego.draw(g);
            nuevoJuego.draw(g);
        }
        if(cargaFallida){
            errorCargar.draw(g);
        }
        botonUnico.draw(g); // dibujar botones por pantalla UnJugador
        controles.draw(g); // dibujar botones por pantalla controles
        acercaDe.draw(g); // dibujar botones por pantalla AcercaDe
        salir.draw(g); // dibujar botones por pantalla Salir

        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        for(int i = 0; i < 6; i++){
            int x = i, y= 0;
            if(i > 2){
                x -= 3;
                y = 1;
            }

            if(i == marioSeleccionado)
                ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            //dibujar el propio mario
            g.drawImage(
                MARIO_COLORS[i],
                428 + x*128,
                100 + y*80,
                80,
                80,
                null
            );
            if(i == marioSeleccionado)
                ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        }
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    public void key(KeyEvent e, boolean down) {
        int code = e.getKeyCode();

        if(!down) return; //continúa solo si estás presionando la tecla hacia abajo

        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE){
            controller.nivelEditor(marioSeleccionado);
        }
        return; //continúe solo si está escribiendo la IP del servidor para conectarse
    }

    public void mouse(MouseEvent e, boolean down) { //Comportamiento del mouse
        if(down)return;
        int x = e.getX(), y = e.getY();
        if(salir.contains(x,y)){
            UnJugador.sumarEstadistica(7,"Partidas Abandonadas: ,"); // le envio el numero de posicion de opcion, de lo que debe sumar y el nombre de la cantidad a sumar             
            System.exit(0);
        }
        if(!editorSeleccionado){
            if(editorButton.contains(x,y)){
                editorSeleccionado = true;
            }
    }else{
            if(cargarJuego.contains(x,y)){
                cargaFallida = false;
                controller.cargarNivelEditor(marioSeleccionado);
            }else if(nuevoJuego.contains(x,y)){
                controller.nivelEditor(marioSeleccionado);
                vAcercaDe=false; // desactivar instrucciones
            }
        }
        if(botonUnico.contains(x,y)){
            cargaFallida = false;
            controller.unJugador(marioSeleccionado);
        }
        if(acercaDe.contains(x,y)){ // ir al menu de ayuda
                controller.nivelEditor(marioSeleccionado);
                vAcercaDe=true; // activar instrucciones
        }
        if(controles.contains(x,y)){ // ir al menu de ayuda
                controller.nivelEditor(marioSeleccionado);
                misControles=true; // desactivar instrucciones 
        }

        for(int i = 0; i < 6; i++){
            int xpos = i, ypos= 0;
            if(i > 2){
                xpos -= 3;
                ypos = 1;
            }
            if(x > 428 + xpos*128 && x < 508 + xpos*128 && y > 100 + ypos*80 && y < 180 + ypos*80){
                marioSeleccionado = i;
                break;
            }
        }
    }

    public void think() { //Pensamiento
        lobby.think(null,true);
    }

}
