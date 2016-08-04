package timotei;

//Represents single post entity
public class SmartPost {
    private final String      name;
    private final String      adress;
    private final String      city;
    private final String      areaCode;
    private String            openData;
    private GeoPoint          location;
    private final TimoteiGuy  worker;
    
    public SmartPost( String name, String openData, double longitude, double latitude,
    String city, String adress, String areaCode ){
        this.name = name;
        this.openData = openData;
        this.city = city;
        this.adress = adress;
        this.areaCode = areaCode;
        location = new GeoPoint( longitude, latitude );
        worker = new TimoteiGuy();
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    //Returns info for GUI.
    public String getInfo(){
        openData = openData.replace( ", ", "\n");
        String info = name + "\n\n" + city + "  " + adress + ", " + areaCode
                + "\n\n" + openData + "\nYou are beinge serviced by: " + worker.getName();
        return info;
    }
    
    //Replaces newlines with html breaklines so getInfo() works with google API.
    public String getInfoForMap(){
        String info = getInfo();
        info = info.replaceAll( "\n", "<br \\>" );
        return info;
    }
    
    //Checks the success of sending item.
    public boolean sendPackage( Item i, Package p, int distance ){
        boolean success;
        switch (p.getCategory()){
            case 1:
                success = i.getBreakableAsInt() != 1;
                break;
            case 2:
                success = true;
                break;
            default:
                //The smaller item is and the more stressed worker - the easier item breaks;
                success = worker.getStressLevel() * i.getRelativeSize() <= 160;
                break;
        }
        return success;
    }

    public double getLongitude(){ return location.getLongitude(); }
    public double getLatitude(){ return location.getLatitude(); }
    
}
