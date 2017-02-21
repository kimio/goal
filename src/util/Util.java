/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
}
