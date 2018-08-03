package cn.edu.cqupt.websocket;

public class Parameters {
    private String type;
    private String data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}