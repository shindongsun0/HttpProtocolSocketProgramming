package server.Handler;

import lombok.extern.slf4j.Slf4j;
import server.Response.ResponseGenerator;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public abstract class HTTPHandler {
    protected File rootDirectory;
    protected String requestSHeader;
    protected String responseHeader;
    protected Socket clientSocket;
    protected File requestedFile;
    protected ResponseGenerator responseGenerator;
    protected String requestType;

    public abstract void handle();

    protected File getFile(String filePath) throws FileNotFoundException{
        File file = new File(filePath);

        if(!file.exists()){
            throw new FileNotFoundException();
        }
        return file;
    }

    protected String getFileType(String input){
        String[] splitString = input.split("\\.");
        return splitString[splitString.length - 1];
    }

    protected String getPathFromHeader(){
        String path;
        String[] splitHeader = requestSHeader.split("\\s");
        if(splitHeader.length < 2)
            throw new IndexOutOfBoundsException();
        path = splitHeader[1];
        return path;
    }

    protected String validatePath() throws IndexOutOfBoundsException{
        String rootPath = rootDirectory.getAbsolutePath();
        String path = getPathFromHeader();
        if(path.length() == 0){
            return path + "mainPage/hello.html";
        }
        if (path.charAt(path.length() - 1) != '/' && Files.isDirectory(Paths.get(rootPath + "/" + path)))
            path += '/';

        if(Files.isDirectory(Paths.get(rootPath + "/" + path))){
            return path + "hello.html";
        }
        return path;
    }

    protected void generateResponseHeader(){
        this.responseHeader = responseGenerator.getResponseHeader();
    }

    protected void writeMessageBody(File file){
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            OutputStream outputStream = clientSocket.getOutputStream();
            byte[] buffer = new byte[1024];

            int readBytes = 0;
            while(fileInputStream.available() > 0 && readBytes != -1){
                readBytes = fileInputStream.read(buffer);
                if(readBytes > 0){
                    outputStream.write(buffer);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("can't find file {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        } catch(IOException e){
            log.error("can't read file {}", e.toString());
            log.error(Arrays.toString(e.getStackTrace()));
        }finally{
            try{
                fileInputStream.close();
            }catch(IOException e){
                log.error("can't close stream {}", e.toString());
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }

    }
}
