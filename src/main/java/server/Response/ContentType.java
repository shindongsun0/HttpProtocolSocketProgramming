package server.Response;

public enum ContentType {
    CSS("TEXT/css"), HTML("TEXT/html"), JAVASCRIPT("TEXT/javascript"), JSON("Application/json"), XML("Application/xml");

    String type;

    ContentType(String type) {
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}
