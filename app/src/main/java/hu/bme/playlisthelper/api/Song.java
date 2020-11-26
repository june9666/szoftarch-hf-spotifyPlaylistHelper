package hu.bme.playlisthelper.api;

public class Song {

    private String id;
    private String name;
    private int popular =1;
    private String uri;

    public Song(String id, String name) {
        this.name = name;
        this.id = id;
        popular =1 ;
    }

    public String getId() {
        return id;
    }

    public void setUri(String uri){this.uri=uri;}
    public String getUri(){return uri;}
    public void setPop(){popular+=1;}

    public int getPop(){return popular;}

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}