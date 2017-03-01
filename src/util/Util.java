/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author felipe.kimio
 */
public class Util {
    /***
     * Abre uma janela para o usuário escolher um determinado arquivo em sua maquina
     * @param stage
     * @param title
     * @param isMultipleFiles
     * @return 
     */
    public static List<File> openFileChooser(Stage stage,String title,boolean isMultipleFiles){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        List<File> list = new ArrayList();
        if(isMultipleFiles){
            list = fileChooser.showOpenMultipleDialog(stage);
        }else{
            File file = fileChooser.showOpenDialog(stage);
            list.add(file);
        }
        return list;
    }
    /***
     * Lê dados de um determinado arquivo
     * @param fileAdress endereço do arquivo
     * @return retorna a String do arquivo
     */
    public static String readFile(String fileAdress){
        try {
            return new String(Files.readAllBytes(Paths.get(fileAdress)));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
    /***
     * Retorna a String de um arquivo informando o File
     * @param file File do arquivo que deseja ler
     * @return 
     */
    public static String readFile(File file){
        return readFile(file.getAbsolutePath());
    }
    /***
     * Escreve algum conteudo no arquivo desejado
     * @param fileAddress Endereço completo do arquivo
     * @param content Conteúdo do Arquivo
     * @return retorna true/false
     */
    public static boolean writeFile(String fileAddress,String content){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileAddress))) {
            bw.write(content);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static void writeTextInImage(File imageFile,ImageText imageText) throws IOException{
        BufferedImage image = ImageIO.read(imageFile);
        Graphics g = image.getGraphics();
        g.setFont(imageText.font);
        g.setColor(imageText.color);
        g.drawString(imageText.value, imageText.posX, imageText.posY);
        g.dispose();

        ImageIO.write(image, "png", imageFile);
    
    }
    public static class ImageText {

        public ImageText() {
            font = new Font("Arial Black", Font.PLAIN, 18);
            posX = 0;
            posY = 0;
            color = Color.BLACK;
        }
        public static String value;
        public static Font font;
        public static int posX;
        public static int posY;
        public static Color color;
    }
    public static void showAlertInformation(String title,String subtitle,String content){
        Alert dialogoInfo = new Alert(Alert.AlertType.INFORMATION);
        dialogoInfo.setTitle(title);
        dialogoInfo.setHeaderText(subtitle);
        dialogoInfo.setContentText(content);
        dialogoInfo.showAndWait();
    }
}
