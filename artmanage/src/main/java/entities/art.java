package entities;

public class art {
    public int id_art;
    public double height;
    public double width;
    public String title;
    public String materials;
    public String type;
    public String city;
    public String description;

    public art(){}

    public art(String title, String materials,double height, double width, String type, String city, String description) {
        this.title = title;
        this.materials = materials;
        this.height = height;
        this.width = width;
        this.type = type;
        this.city = city;
        this.description = description;
    }
    public art(int id_art, String title, String materials,double height, double width, String type, String city, String description) {
        this.id_art = id_art;
        this.title = title;
        this.materials = materials;
        this.height = height;
        this.width = width;
        this.type = type;
        this.city = city;
        this.description = description;
    }



    public  int getId_art() {
        return id_art;
    }

    public void setId_art(int id_art) {
        this.id_art = id_art;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public  String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "art{" +
                "id_art=" + id_art +
                ", title='" + title + '\'' +
                ", materials='" + materials + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", type='" + type + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                '}' +"\n";
    }

}
