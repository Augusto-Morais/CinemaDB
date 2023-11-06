package DB.TablesSetUp;

import Data.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MovieSetup {
    private static final String dir = System.getProperty("user.dir");

    private static int count = 0;
    private static final String sqlFilesDir = dir + "\\src\\main\\java\\DB\\SQLFiles\\Movie\\";

    private static ArrayList<Movie> scrapeMovies(){
        ArrayList<Movie> moviesList = new ArrayList<>();
        Document doc;

        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_highest-grossing_films")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .header("Accept-Language","*")


                    .get();


            Element table = doc.select(".wikitable.plainrowheaders").get(2);
//            System.out.println(table.size());
            Elements body = table.select("tbody");
            Elements rows = body.select("tr");
            rows.remove(0);
//            System.out.println(rows.size());
//            Elements titles = rows.select("i");
//            int size = rows.size();

//            System.out.println(table);

//            int d = 1;
//            int rowSpanInt = 0;
            for (Element row: rows){
                String title = row.select("i").text();

                String link = "https://en.wikipedia.org" + row.select("i").select("a").attr("href");

                Document innerDoc = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .header("Accept-Language","*")


                        .get();

                Document document = Jsoup.parse(innerDoc.html());

//          PLOT
                Elements endOfThePlot = document.select("h2:has(span[id='Plot']) ~ h2 span:first-child");
                String endOfThePlotId = endOfThePlot.attr("id");
                ArrayList<String> plot = new ArrayList<>();

                Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='" + endOfThePlotId +  "']),h2:has(span[id='" + endOfThePlotId +  "']) ~ *) ");
                for (Element element : plotList) {
                    plot.add(element.text().replaceAll("'", "\"").replaceAll("\\[edit]","") + "\n");
                }


//            POSTER
                Elements infoBoxImage = document.select("[class='infobox-image']");
                String poster = "https:" + infoBoxImage.select("img").attr("src");

//            DIRECTORS
                Elements infoBoxVevent = document.select("[class='infobox vevent'] tbody tr");
                Elements directorsList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Directed) ~ td");
                StringBuilder director = new StringBuilder();
                directorsList.forEach(directorElement -> director.append(directorElement.text().replaceAll("'","''").replaceAll("\\[\\d]","").replaceAll("'","\"")));

//           CAST
                Elements starringList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td li");// th:contains(Starring) ~ td
                StringBuilder starring =  new StringBuilder();

                if(!starringList.isEmpty()) {
                    starringList.forEach(actor -> starring.append(actor.text().replaceAll("'","''").replaceAll("\\[\\d]","").replaceAll("'","\"")).append("  "));
                }
                else {
                    starringList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td");
                    starring.append(starringList.text().replaceAll("\\[\\d]","").replaceAll("'","\"").replaceAll("\\[\\d]","").replaceAll("'","\""));
                }

//            STUDIO
                String studio;
                if(!document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Production) ~ td").isEmpty()){
                    studio = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Production) ~ td").text().replaceAll("\\[\\d]","").replaceAll("'","\"");
                }else{
                    studio = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Distributed) ~ td").text().replaceAll("\\[\\d]","").replaceAll("'","\"");
                }

//            RELEASE DATE
                String releaseDate = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Release date) ~ td").text().replaceAll("\\[\\d]","").replaceAll("'","\"");

//        BUDGET
                String budget = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Budget) ~ td").text().replaceAll("\\[\\d]","").replaceAll("\\[\\d]","").replaceAll("'","\"");

//        BOX OFFICE
                String boxOffice = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Box office) ~ td").text().replaceAll("\\[\\d]","").replaceAll("\\[\\d]","").replaceAll("'","\"");



                Movie movie = new Movie(title,studio,plot,director.toString(),starring.toString(),poster,releaseDate, budget,
                        boxOffice, link);

                moviesList.add(movie);

//                if(moviesList.size() ==10) break;

            }
        }
        catch (IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.err.println(e.getMessage());
        }
        return moviesList;
    }

    private static void fillMovieTableFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFilesDir + "fillMovieTable.sql",true));){

//            System.out.println(sqlFilesDir);


            ArrayList<Movie> moviesList = scrapeMovies();



            for (Movie movie:moviesList
                 ) {
//                bufferedWriter.write("(" + movie.getTitle() +
//                        "," + movie.getStudio() + "," + movie.getPlot() + "," + movie.getDirectors() + "," + movie.getStarring()
//                                + "," + movie.getPoster() + "," + movie.getReleaseDate() + "," + movie.getBudget() + ",");
//                bufferedWriter.write(movie.getTitle() + "\n");

//                !movie.getPlot().isEmpty() ? movie.getPlot().get(0).replace('\'','"') + movie.getPlot().get(1).replace('\'','"') : "null"

//===============
                bufferedWriter.write(String.format(
                        "('%s','%s', ARRAY [",movie.getTitle().replace('\'','"'),movie.getStudio().replace('\'','"')
                ));

                if(!movie.getPlot().isEmpty()){
                    for (String plotParagraph:
                            movie.getPlot()) {
//                        if(plotParagraph.contains("[edit]")) {
//                            count++;
//                            System.out.println(movie.getLink() + " = " + count);
//                        }

                        if(plotParagraph.isEmpty()){
                            count++;
                            System.out.println(movie.getLink() + " = " + count);
                            break;
                        }
                        else if(movie.getPlot().indexOf(plotParagraph) == movie.getPlot().size() -1){
                            bufferedWriter.write(String.format(
                                    "'%s'", plotParagraph.replace("\n","").replaceAll("'","\"")

                            ));
                            break;
                        }
                        else{
                            bufferedWriter.write(String.format(
                                    "'%s', ", plotParagraph.replace("\n","").replaceAll("'","\"")

                            ));
                        }



//                    System.out.println("===========\n" + movie.getPlot() + "===========\n");
                    }
                }
                else{
                    bufferedWriter.write("''");
                }
                bufferedWriter.write("], ");


                if(Objects.equals(moviesList.get(moviesList.size() - 1).getTitle(), movie.getTitle())){
                    bufferedWriter.write(String.format(
                            "'%s','%s','%s','%s','%s','%s');",
                            movie.getDirectors(),movie.getStarring(),movie.getPoster(),
                            movie.getReleaseDate(),
                            movie.getBudget(),movie.getBoxOffice()
                    ));
                }
                else{
                    bufferedWriter.write(String.format(
                            "'%s','%s','%s','%s','%s','%s'),\n",
                            movie.getDirectors(),movie.getStarring(),movie.getPoster(),
                            movie.getReleaseDate(),
                            movie.getBudget(),movie.getBoxOffice()
                    ));
                }





//                System.out.println(movie);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        fillMovieTableFile();
//        System.out.println(moviesList);
//        ArrayList<Movie> movies;
//        movies = scrapeMovies();
//        ArrayList<Movie> theFuckedUpOnes = (ArrayList<Movie>) movies.stream().filter(movie -> {
//            boolean b = movie.getStarring().isEmpty() || movie.getPlot().isEmpty() ||
//                    movie.getStudio().isEmpty() || movie.getBudget().isEmpty()
//                    || movie.getBoxOffice().isEmpty() || movie.getReleaseDate().isEmpty()
//                    || movie.getDirectors().isEmpty();
//            return b;
//        }).collect(Collectors.toList());
//
//        System.out.println(movies);
//        System.out.println(theFuckedUpOnes);
//        System.out.println(theFuckedUpOnes.size());

        fillMovieTableFile();

//        MyConnection myConnection = new MyConnection();
//
//        myConnection.connect();
//
//        String createTableMovie = myConnection.getSQLFileContent(sqlFilesDir + "createTableMovie.sql");
//        String fillMovieTable = myConnection.getSQLFileContent(sqlFilesDir + "fillMovieTable.sql");
//        QueryResult queryResult = myConnection.query(fillMovieTable);
//        System.out.println(queryResult);




    }
}
