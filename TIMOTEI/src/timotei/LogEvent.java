package timotei;

//Parent class for events. Makes timestamp a requirement.
public abstract class LogEvent {
    private final String timeStamp;
    private int          id;
    
    public LogEvent( String d ){
        timeStamp = d;
    }
    
    public void setID( int i ){ id = i; }
    public int getID(){ return id; }
    public String getTimeStamp(){ return timeStamp; }
}
