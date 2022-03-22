import java.awt.*;
import java.awt.event.*;

/*
 * Esto representa una Pantalla en el juego. Como un menú, o una pantalla de pausa, o el propio juego.
 * Estos son cambios entre sí principalmente en ScreenManager, pero las pantallas pueden
 * contener y dibujar otras pantallas si la situación lo requiere.
 */
public abstract class Pantalla { 
        //controla el ScreenManager principal
	public ScreenController controller;
        
        // Dibuja esta pantalla para los Gráficos para dibujar
	public abstract void draw(Graphics g);
        
	/*
        * llamado cuando se presiona o suelta una tecla
        * e evento de key
        * down verdadero si se presiona, falso si no se presiona
        */
	public abstract void key(KeyEvent e, boolean down);
        
	/**
        * se llama cuando se presiona o suelta un botón del mouse
        * e evento del mouse
        * down verdadero si se presiona, falso si no se presiona
        */
	public abstract void mouse(MouseEvent e, boolean down);
        
	//llamó a cada marco (frame)
	public abstract void think();
}
