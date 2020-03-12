package server.Response;

public enum StatusCodes {
  OK(200, "200 OK"), SERVER_ERROR(500, "500 Internal Server Error"), FOUND(302, "302 Found");
    private int code;
  private String status;

  StatusCodes(int code, String status){
      this.code = code;
      this.status = status;
  }
  public int getCode() {
      return this.code;
  }
  public String getStatus(){
      return this.status;
  }
}
