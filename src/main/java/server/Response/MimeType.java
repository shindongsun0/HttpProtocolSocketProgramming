package server.Response;

public enum MimeType {
    CSS("TEXT/css"),
    HTML("TEXT/html"),
    JAVASCRIPT("TEXT/javascript"),
    JSON("Application/json"),
    XML("Application/xml"),
    TXT("TEXT/plain");

    String type;

    MimeType(String type) {
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}
