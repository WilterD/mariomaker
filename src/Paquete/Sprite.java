import java.awt.*;
import java.awt.image.*;
import javax.swing.ImageIcon;

/*
 * Esta es una clase auxiliar para manipular imágenes.
 */
public class Sprite{
    private BufferedImage imgB;

    private BufferedImage transparencia;

    private Image img;
    //Ancho y largo
    int width,height;

    /*
    * Crea un nuevo Sprite a partir de un camino.
    * donde filename Representación de cadena de la ruta a la imagen.
    */
    public Sprite(String filename){
        this(filename,-1, -1);
    }

    /*
    * Crea un nuevo Sprite a partir de una ruta y lo escala al ancho y alto especificados
    * ademas de filaneme, usaremos ancho y la altura (width y height)
    */
    public Sprite(String filename, int width, int height){
        img = (new ImageIcon(filename)).getImage();
        if(width == -1)
            this.width = img.getWidth(null);  
        else
            this.width = width;

        if(height == -1)
            this.height = img.getHeight(null);
        else
            this.height = height;

        bufferImage();
    }

    // Crea un nuevo Sprite a partir de una BufferedImage existente
    public Sprite(BufferedImage image){
        this(image,-1,-1);
    }

    /*
    * Crea un nuevo Sprite a partir de un BufferdImage existente 
    * y lo escala al ancho y alto especificados
    */
    public Sprite(BufferedImage image, int width, int height){
        if(width == -1)
            this.width = image.getWidth();  
        else
            this.width = width;

        if(height == -1)
            this.height = image.getHeight();
        else
            this.height = height;
        img = Toolkit.getDefaultToolkit().createImage(image.getSource()); 
        bufferImage();
    }

    //Crea un sprite en blanco con el ancho y alto especificado
    public Sprite(int width, int height){
        this(new BufferedImage(width <= 0? 1 : width,height <= 0? 1 : height,BufferedImage.TYPE_INT_ARGB));
    }

    private void bufferImage(){
        imgB = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        renew();
    }

    /*
    * Deshace cualquier cambio realizado en la imagen por esta clase
    */
    public void renew(){
        Graphics g = imgB.createGraphics();
        g.drawImage(img, 0, 0, width,height, null);
        g.dispose();
    }

    private BufferedImage blank(){
        return new BufferedImage(width, height,  imgB.getType());
    }

    /*
    * Reemplaza todos los colores que se encuentran en la imagen en find 
    * con el color respectivo de replace
    * find Color para buscar y reemplazar
    * replace Color para reemplazar el Color respectivo en find
    */
    public void replaceColors(Color[] find, Color[] replace){
        if(find[0].equals(replace[0]))return;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int rgb = imgB.getRGB(x,y);
                for(int i = 0; i < find.length; i++){
                    if(rgb == find[i].getRGB()){
                        setPixel(x,y,replace[i]);
                    }
                }
            }
        }
    }

    public String toString(){
        StringBuffer s = new StringBuffer();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                s.append(getPixel(x,y));
            }
        }
        return s.toString();
    }

    /*
    * Crea una imagen almacenada en búfer que es un giro horizontal de la 
    * imagen actual y la devuelve
    */
    public BufferedImage flipX(){
        BufferedImage r = blank();
        Graphics2D g2d = (Graphics2D)(r.createGraphics());
        g2d.drawImage(imgB, 0, 0, width, height, width, 0, 0, height, null);
        return r;  
    }

    /*
    * Crea una imagen almacenada en búfer que es un giro vertical de
    * la imagen actual y la devuelve
    */
    public BufferedImage flipY(){
        BufferedImage r = blank();
        Graphics2D g2d = (Graphics2D)(r.createGraphics());
        g2d.drawImage(imgB, 0, 0, width, height, 0, height, width, 0, null);
        return r;  
    }

    /*
    * Crea una imagen almacenada en búfer que es un giro horizontal y vertical 
    * de la imagen actual y la devuelve
    */
    public BufferedImage flipXY(){
        BufferedImage x = flipX();
        BufferedImage r = blank();
        Graphics2D g2d = (Graphics2D)(r.createGraphics());
        g2d.drawImage(x, 0, 0, width, height, 0, height, width, 0, null);
        return r;  
    }

    /*
    * Obtiene el color del píxel en la imagen en las coordenadas especificadas
    * donde 'x' y 'y' son coordenada del píxel
    * este retornara un nuevo Color que es la representación del Color de 
    * la imagen en el píxel
    */
    public Color getPixel(int x, int y){
        int rgb = imgB.getRGB(x,y);
        return new Color((rgb & 0x00ff0000) >> 16, (rgb & 0x0000ff00) >> 8, rgb & 0x000000ff, (int)((rgb & 0xFF000000)>>>24));
    }

    //establece el color de un píxel en la imagen en las coordenadas especificadas
    public void setPixel(int x, int y, Color c){
        Graphics g = imgB.createGraphics();
        g.setColor(c);
        g.fillRect(x,y,1,1);
        g.dispose();
    }

    public Image getImage(){
        return img;
    }

    public BufferedImage getBuffer(){
        return imgB;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}