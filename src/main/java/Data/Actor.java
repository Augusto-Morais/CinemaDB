package Data;

import java.util.ArrayList;

public class Actor {
    private String name;
    private String born;
    private String image_link;

    private ArrayList<String> general_info;

    private String filmography;



    public Actor(String name, String born, String image_link, ArrayList<String> general_info, String filmography) {
        this.name = name;
        this.born = born;
        this.image_link = image_link;
        this.general_info = general_info;
        this.filmography = filmography;
    }

    public Actor(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public ArrayList<String> getGeneral_info() {
        return general_info;
    }

    public void setGeneral_info(ArrayList<String> general_info) {
        this.general_info = general_info;
    }

    public String getFilmography() {
        return filmography;
    }

    public void setFilmography(String filmography) {
        this.filmography = filmography;
    }

    @Override
    public String toString() {
        return String.format("""
                actor: {
                \tname: %s
                \tborn: %s
                \timage_link: %s
                \tgeneral_info %s
                \tfilmography: %s
                }
                """, this.getName(),this.getBorn(),this.getImage_link(),this.getGeneral_info(),this.getFilmography());
    }
}
