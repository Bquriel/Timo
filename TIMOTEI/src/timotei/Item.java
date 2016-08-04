package timotei;

//Represents single Item
public class Item{
    private int             id;
    private final String    name;
    private final String    description;
    private final float     weight;
    private final float     sizeX;
    private final float     sizeY;
    private final float     sizeZ;
    private final boolean   breakable;
    
    public Item( String name, String description, float weight, float sizeX, 
            float sizeY, float sizeZ, boolean breakable ){
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.breakable = breakable;
    }
    
    public void setId( int id ){
        this.id = id;
    }
    
    
    @Override
    public String toString(){
        return name;
    }    
    
    //Returns info for GUI.
    public String getInfo(){
        String br;
        br = breakable ? "Breakable" : "Unbreakable";
        String info = name + "\n\nDescription:\n" + description + "\n\nInfo:\n" 
                + "X: " + sizeX + " cm\nY: " + sizeY + " cm\nZ: " + sizeZ 
                + " cm\nWeight: " + weight + " kg\n\n" + br;
        return info;
    }

    //Returns size category, 1 for large, 2 for medium and 3 for small
    public int getRelativeSize(){
        if( sizeX + sizeY + sizeZ < 1.5 ){
            return 3;
        } else if( sizeX + sizeY + sizeZ < 5.5 ){
            return 2;
        } 
        return 1;
    }
    
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public float getWeight(){ return weight; }
    public float getSizeX(){ return sizeX; }
    public float getSizeY(){ return sizeY; }
    public float getSizeZ(){ return sizeZ; }
    public int getBreakableAsInt(){ return breakable ? 1 : 0; }
    
}
