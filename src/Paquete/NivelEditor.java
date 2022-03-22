import java.awt.*;
import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/*
 * El Editor de Niveles parte del juego. Contiene un PantallaSpawn que se usa para crear y 
 * manipular Cosas en el juego.
 */
public class NivelEditor extends GameScreen{
    private PantallaSpawn pantallaSpawn;
    private boolean mouseAbajo;
    private Vector<TGridded> arrastreSpawn;
    private boolean congelar;

    //Constructor del Editor
    public NivelEditor(){
        super();
        pantallaSpawn = new PantallaSpawn(heroe);
        mouseAbajo = false;
        arrastreSpawn = null;
        congelar = false;
    }

    //Reinicio de editor de niveles
    public void reiniciar(){
        heroe.init();
        heroe.setMuertePos();
        heroe.startInvulnerable();
        setSpawn();
    }

    //Ejcucion o proceso en accion (opcion 1)
    public boolean init(int marioColor, File f) throws Exception{
        boolean b = super.init(marioColor, f);
        congelar = true;
        return b;
    }

     //Ejcucion o proceso en accion (opcion 2)
    public void init(int marioColor){
        super.init(marioColor);
        congelar = false;
    }

    //Dibujar por pantalla
    public void draw(Graphics g) {
        lobbyActual().draw(g, null, heroe, pantallaSpawn);
        pantallaSpawn.draw(g);	
    }

    public void key(KeyEvent e, boolean pressed) {
            super.key(e,pressed);
            pantallaSpawn.key(e,pressed);	
    }

    //Comportamiento del mouse 
    public void mouse(MouseEvent e, boolean down) {
        pantallaSpawn.mouse(e,down);
        if(pantallaSpawn.shouldToggleLevel()){
            lobbyIndex++;
            if(lobbyIndex == lobbys.size()){
                lobbyIndex = 0;
            }
            heroe.init();
            heroe.startInvulnerable();
        }else if(pantallaSpawn.shouldToggleFreeze()){
            congelar = !congelar;
        }
        if(down){
            mouseAbajo = true;
            Thing spawn = pantallaSpawn.getSpawn();
            if(spawn != null){
                lobbyActual().add(spawn);
                if(spawn instanceof TGridded){
                    arrastreSpawn = new Vector<TGridded>();
                    arrastreSpawn.add((TGridded)spawn);
                }
            }
        }else{
            mouseAbajo = false;
            arrastreSpawn = null;
        }
    }

    //Pensamiento del juego
    public void think() {
        super.think();
        pantallaSpawn.think();
        lobbyActual().think(heroe,true,congelar);
        Class spawn = lobbyActual().debeRemoverSpawnOtrosLobbys();
        if(spawn != null){
            for(Lobby lobby: lobbys){
                if(lobby != lobbyActual()){
                    lobby.removerSpawns(spawn);
                }
            }
        }

        if(mouseAbajo && arrastreSpawn != null && arrastreSpawn.size() > 0 && pantallaSpawn.pasarSpawn() != null && pantallaSpawn.pasarSpawn() instanceof TGridded){
            TGridded peek = (TGridded)pantallaSpawn.pasarSpawn();
            boolean touchingSomething = false;
            for(TGridded grid: arrastreSpawn){
                if(peek.representation().contains(grid.representation())){
                    touchingSomething = true;
                    break;
                }
            }
            if(!touchingSomething){
                arrastreSpawn.add(peek);
                lobbyActual().add(pantallaSpawn.getSpawn());
            }
        }
        if(mouseAbajo && pantallaSpawn.pasarSpawn() != null && pantallaSpawn.pasarSpawn() instanceof TRemover){
            lobbyActual().add(pantallaSpawn.pasarSpawn());
        }	
    }
}
