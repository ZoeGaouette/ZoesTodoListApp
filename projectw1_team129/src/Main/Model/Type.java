package Model;

public enum Type {
    REGULARTASK("Regular"),
    ACADEMICTASK("Academic"),
    EVENT("Event");


    private String type;

    Type(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
