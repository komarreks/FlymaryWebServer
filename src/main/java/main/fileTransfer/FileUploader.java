package main.fileTransfer;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class FileUploader {
    private static final String rootPath = "img/";

    private static void createResourse(String res){
        File root = new File(rootPath);
        if (!root.exists()){
            root.mkdir();
        }

        File catalogDir = new File(rootPath+res);
        if (!catalogDir.exists()){
            catalogDir.mkdir();
        }
    }

    private static String getCorrectFileName(String name){
        return name.replaceAll(" ","_").toLowerCase();
    }

    public static String safeImage(String body, String name, String ext, String catalog){
        String filePath = rootPath + catalog + "/" + getCorrectFileName(name) + "."+ext;

        File file = new File(filePath);

        createResourse(catalog);

        try {
            if (!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] imgArrayByte = Base64.getDecoder().decode(body.replaceAll("\r\n",""));
            fileOutputStream.write(imgArrayByte);
            fileOutputStream.flush();

            return filePath;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public static String safeFile(String body, String name, String ext, String catalog){
        String filePath = rootPath + catalog + "/" + name + "."+ext;

        File file = new File(filePath);

        createResourse(catalog);

        try {
            if (!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fis = new FileOutputStream(file);

            fis.write(body.getBytes());

            fis.flush();

            return filePath;
        }catch (Exception e){
            return e.getMessage();
        }

    }
}
