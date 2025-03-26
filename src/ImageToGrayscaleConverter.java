import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Clase principal para convertir imágenes en color a escala de grises usando dos métodos diferentes.
 * Guarda las imágenes resultantes en archivos PNG.
 */
public class ImageToGrayscaleConverter {
    
    /**
     * Método principal que ejecuta el proceso de conversión.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Cargar la imagen de entrada
            BufferedImage colorImage = ImageIO.read(new File("imagen.png"));
            System.out.println("Imagen cargada correctamente");
            
            // Convertir a escala de grises usando ambos métodos
            FingerPrintImage grayscaleA = convertToGrayscaleA(colorImage);
            FingerPrintImage grayscaleB = convertToGrayscaleB(colorImage);
            
            // Convertir de vuelta a BufferedImage para guardar
            BufferedImage outputA = convertToBufferedImage(grayscaleA, 1);
            BufferedImage outputB = convertToBufferedImage(grayscaleB, 1);
            
            // Guardar las imágenes resultantes
            ImageIO.write(outputA, "png", new File("imagen_grises_A.png"));
            ImageIO.write(outputB, "png", new File("imagen_grises_B.png"));
            
            System.out.println("Imágenes en escala de grises guardadas correctamente");
            
        } catch (IOException e) {
            System.err.println("Error al procesar la imagen: " + e.getMessage());
        }
    }
    
    /**
     * Convierte una imagen en color a escala de grises usando el método del promedio simple.
     * @param colorImage Imagen en color de entrada (BufferedImage)
     * @return Imagen en escala de grises (FingerPrintImage)
     */
    public static FingerPrintImage convertToGrayscaleA(BufferedImage colorImage) {
        int width = colorImage.getWidth();
        int height = colorImage.getHeight();
        FingerPrintImage grayscaleImage = new FingerPrintImage(width, height);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Obtener componentes RGB del píxel
                int rgb = colorImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;  // Componente Rojo
                int g = (rgb >> 8) & 0xFF;   // Componente Verde
                int b = rgb & 0xFF;         // Componente Azul
                
                // Calcular valor de gris como promedio de los componentes
                int grayValue = (r + g + b) / 3;
                
                // Asignar el valor al píxel correspondiente
                grayscaleImage.setPixel(x, y, (char)grayValue);
            }
        }
        return grayscaleImage;
    }
    
    /**
     * Convierte una imagen en color a escala de grises usando el método ponderado (luminosidad).
     * Este método sigue la recomendación ITU-R BT.709 para conversión a escala de grises.
     * @param colorImage Imagen en color de entrada (BufferedImage)
     * @return Imagen en escala de grises (FingerPrintImage)
     */
    public static FingerPrintImage convertToGrayscaleB(BufferedImage colorImage) {
        int width = colorImage.getWidth();
        int height = colorImage.getHeight();
        FingerPrintImage grayscaleImage = new FingerPrintImage(width, height);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Obtener componentes RGB del píxel
                int rgb = colorImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;  // Componente Rojo
                int g = (rgb >> 8) & 0xFF;   // Componente Verde
                int b = rgb & 0xFF;         // Componente Azul
                
                // Calcular valor de gris usando coeficientes de luminosidad
                int grayValue = (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);
                
                // Asignar el valor al píxel correspondiente
                grayscaleImage.setPixel(x, y, (char)grayValue);
            }
        }
        return grayscaleImage;
    }
    
    /**
     * Convierte una imagen en escala de grises (FingerPrintImage) a BufferedImage para su almacenamiento.
     * @param grayscaleImage Imagen en escala de grises de entrada
     * @param modo Modo de conversión (0: normalizar a 0-255, 1: usar valores directamente)
     * @return Imagen en formato BufferedImage lista para guardar
     */
    public static BufferedImage convertToBufferedImage(FingerPrintImage grayscaleImage, int modo) {
        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int valor = grayscaleImage.getPixel(x, y);
                
                // Normalizar si es necesario (modo 0)
                if (modo == 0) {
                    valor = valor * 255;
                }
                
                // Crear píxel RGB (los 3 componentes iguales para escala de grises)
                int pixelRGB = (255 << 24) | (valor << 16) | (valor << 8) | valor;
                bufferedImage.setRGB(x, y, pixelRGB);
            }
        }
        return bufferedImage;
    }
}

/**
 * Clase para almacenar imágenes en escala de grises.
 * Utiliza una matriz bidimensional de caracteres para representar los valores de intensidad.
 */
class FingerPrintImage {
    private int width;       // Ancho de la imagen en píxeles
    private int height;      // Alto de la imagen en píxeles
    private char[][] img;    // Matriz de píxeles (valores de gris)
    
    /**
     * Constructor de la clase FingerPrintImage.
     * @param width Ancho de la imagen
     * @param height Alto de la imagen
     */
    public FingerPrintImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.img = new char[width][height];
    }
    
    /**
     * Obtiene el ancho de la imagen.
     * @return Ancho en píxeles
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Obtiene el alto de la imagen.
     * @return Alto en píxeles
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Establece el valor de un píxel en la posición especificada.
     * @param x Coordenada X (columna)
     * @param y Coordenada Y (fila)
     * @param color Valor de intensidad de gris (0-255)
     */
    public void setPixel(int x, int y, char color) {
        img[x][y] = color;
    }
    
    /**
     * Obtiene el valor de un píxel en la posición especificada.
     * @param x Coordenada X (columna)
     * @param y Coordenada Y (fila)
     * @return Valor de intensidad de gris (0-255)
     */
    public char getPixel(int x, int y) {
        return img[x][y];
    }
}