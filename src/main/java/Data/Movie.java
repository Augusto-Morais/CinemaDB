package Data;

import java.util.ArrayList;

public class Movie {
    private String title;
    private String studio;

    private ArrayList<String> plot;

    private String directors;

    private String starring;

    private String poster;

    private String releaseDate;

    private String budget;
    private String boxOffice;
    private String link;


    private int year;

    public Movie(String title, String budget, String boxOffice, String link){
        this.title = title;
        this.budget = budget;
        this.boxOffice = boxOffice;
        this.link = link;
    }

    public Movie(String title, String studio, ArrayList<String> plot, String directors, String starring, String poster,String releaseDate ,
                    String budget, String boxOffice, String link){
        this.title = title;
        this.studio = studio;
        this.plot = plot;
        this.directors = directors;
        this.starring = starring;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.boxOffice = boxOffice;
        this.link = link;
    }


    public String getDirectors() {
        return directors;
    }

    public String getStarring() {
        return starring;
    }

    public ArrayList<String> getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public void setPlot(ArrayList<String> plot) {
        this.plot = plot;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("\"Data.Movie\":{\n" +
                "\t\"Title\": \"%s\"\n" +
                "\t\"Studio\": \"%s\"\n" +
                "\t\"Plot\": \"%s\"\n" +
                "\t\"Directors\": \"%s\"\n" +
                "\t\"Starring\": \"%s\"\n" +
                "\t\"Poster\": \"%s\"\n" +
                "\t\"Release Date\": \"%s\"\n" +
                "\t\"Budget\": \"%s\"\n" +
                "\t\"Box Office\": \"%s\"\n" +
                "\t\"Link\": \"%s\"\n" +

                "}", this.getTitle(),this.getStudio(),this.getPlot(),this.getDirectors(),this.getStarring(),this.getPoster(),
                this.getReleaseDate(),this.getBudget(),this.getBoxOffice(),this.getLink());
    }
}