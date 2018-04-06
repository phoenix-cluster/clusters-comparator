package cn.edu.cqupt.model;


/**
 * ASC: ascending order
 * DES: descending order
 */
public enum Direction {
    ASC("ascent"),
    DES("descent");

    private String direction;
    private Direction(String direction){
        this.direction = direction;
    }

    public String getDirection(){
        return this.direction;
    }
}
