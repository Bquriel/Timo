package timotei;

//Class represents Item wrapper
public abstract class Package {
    private final float     maxSizeX;
    private final float     maxSizeY;
    private final float     maxSizeZ;
    private final float     weightLimit;
    private final int       distanceLimit;
    private final boolean   hasMaxSize;
    
    
    public Package( float x, float y, float z, float wLimit, int dLimit ){
        maxSizeX = x;
        maxSizeY = y;
        maxSizeZ = z;
        weightLimit = wLimit;
        distanceLimit = dLimit;
        
        //Database cannot return null, so we check against zero
        hasMaxSize = !(maxSizeX == 0 && maxSizeY == 0 && maxSizeZ == 0);
    }
    
    public boolean canSendItem( Item i, int distance ){
        return doesWeightFit( i ) && doesSizeFit( i ) && isAvailableDistance( distance );
    }
    
    private boolean doesWeightFit( Item i ){
        if( weightLimit != 0 ){
            if( i.getWeight() > weightLimit ){
                return false;
            }
        }
        return true;
    }
    
    private boolean doesSizeFit( Item i ){
        if( hasMaxSize ){
            if( i.getSizeX() > maxSizeX && i.getSizeY() > maxSizeY && 
                    i.getSizeZ() > maxSizeZ ){
                return false;
            }
        }
        return true;
    }
    
    private boolean isAvailableDistance( int distance ){
        if( distanceLimit != 0 ){
            return ( distance <= distanceLimit );    
        } 
        return true;
    }
    
    public int getCategory(){ return 0; }
    
}

class PackageClass1 extends Package{
    
    public PackageClass1( float x, float y, float z, float wLimit, int dLimit ){
        super( x, y, z, wLimit, dLimit );
    }
    
    @Override
    public String toString(){
        return "1st class package";
    }
    
    @Override
    public int getCategory(){ return 1; }
}

class PackageClass2 extends Package{
    
    public PackageClass2( float x, float y, float z, float wLimit, int dLimit ){
        super( x, y, z, wLimit, dLimit );
    }
    
    @Override
    public String toString(){
        return "2nd class package";
    }
    
    @Override
    public int getCategory(){ return 2; }
}

class PackageClass3 extends Package{
    
    public PackageClass3( float x, float y, float z, float wLimit, int dLimit ){
        super( x, y, z, wLimit, dLimit );
    }
    
    @Override
    public String toString(){
        return "3rd class package";
    }
    
    @Override
    public int getCategory(){ return 3; }
}