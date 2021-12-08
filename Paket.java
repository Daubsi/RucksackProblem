public class Paket {
    
    // The parameters
    public String name;
    public int value;
    public int width;
    public int length;
    
    
    // The creation
    public Paket(String name, int value, int width, int length){
        this.value = value;
        this.width = width;
        this.length = length;
        this.name = name;
    }

    public float getValuePerArea(){
        return (float) value / (width * length);
    }

    public String toString(){
        return  "Name: \t"  + name   + "\n" +
                "Wert: \t"  + value  + "\n" +
                "Breite: "  + width  + "\n" +
                "Länge: \t" + length + "\n";
    }

}
