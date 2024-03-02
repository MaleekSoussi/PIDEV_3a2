package Models;

public class Workshop
{
    private int id_W;
    private String resources,description;
    private float duration;
    private int id_C;
    private String nameW;
    //private int userid;



    public Workshop() {
    }


///constructor with userid
   /* public Workshop(int id_W, String resources, String description, float duration, int id_C, String nameW, int userid) {
        this.id_W = id_W;
        this.resources = resources;
        this.description = description;
        this.duration = duration;
        this.id_C = id_C;
        this.nameW = nameW;
        this.userid = userid;
    }*/

    public Workshop(int id_W, String nameW, String resources, String description, float duration, int id_C) {
        this.id_W = id_W;
        this.nameW = nameW;
        this.resources = resources;
        this.description = description;
        this.duration = duration;
        this.id_C = id_C;

    }

    public Workshop(String nameW, String resources, String description, float duration)
    {
        this.resources = resources;
        this.description = description;
        this.duration = duration;
        this.nameW = nameW;
    }

    public Workshop(String nameW , String resources, String description, float duration, int id_C) {
        this.nameW = nameW;
        this.resources = resources;
        this.description = description;
        this.duration = duration;
        this.id_C = id_C;
    }

/*
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }*/

    public int getId_W() {
        return id_W;
    }

    public void setId_W(int id_W) {
        this.id_W = id_W;
    }

    public String getNameW() {return nameW;}

    public void setNameW(String nameW) {this.nameW = nameW;}

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getId_C() { return id_C;}

    public void setId_C(int id_C) { this.id_C = id_C;}


    @Override
    public String toString() {
        return "Workshop{" +
                "id_W=" + id_W +
                ", nameW='" + nameW + '\'' +
                ", resources='" + resources + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", id_C=" + id_C +

                '}';
    }
}
