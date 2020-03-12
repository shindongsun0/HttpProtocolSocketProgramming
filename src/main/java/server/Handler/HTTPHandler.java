package server.Handler;

import server.Response.ResponseGenerator;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class HTTPHandler {
    protected File rootDirectory;
    protected String requestSHeader;
    protected String responseHeader;
    protected Socket clientSocket;
    protected File requestedFile;
    protected ResponseGenerator responseGenerator;

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

    protected String getPathFromHeader() throws IndexOutOfBoundsException{
        String rootPath = rootDirectory.getAbsolutePath();
        String path;
        String[] splitHeader = requestSHeader.split("\\s");
        if(splitHeader.length < 2)
            throw new IndexOutOfBoundsException();
        path = splitHeader[1];
        if(path.charAt(path.length() - 1 ) != '/' && Files.isDirectory(Paths.get(rootPath + path)))
            path += '/';
        if(Files.isDirectory(Paths.get(rootPath + path))){
            if(Files.exists(Paths.get(rootPath + path + "index.html")))
                return path + "index.html";
            return path + "index.html";
        }
        return path;
    }

    protected  void generateResponseHeader(){
        responseHeader = responseGenerator.getResponseHeader();
    }
    protected void writeFileToStream(File file){
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            OutputStream out;
            byte[] buffer = new byte[1024];
            out = clientSocket.getOutputStream();

            int readBytes = 0;
            while(fileInputStream.available() > 0 && readBytes != -1){
                readBytes = fileInputStream.read(buffer);
                if(readBytes > 0){
                    out.write(buffer);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Can't find file");
        } catch(IOException e){
            System.err.println("Can't write to stream");
        }finally{
            try{
                fileInputStream.close();
            }catch(IOException e){
                System.err.println("Can't close file reader");
            }
        }

    }
}
