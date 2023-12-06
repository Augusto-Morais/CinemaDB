package DB.TablesSetUp;

import Data.Movie;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class MovieSetup {
    private static final String dir = System.getProperty("user.dir");

    private static int count = 0;
    private static final String sqlFilesDir = dir + "\\src\\main\\SQLFiles\\Movie\\";
    private static  final String jsonFilesDir = dir + "\\src\\main\\\\JSONFiles\\Movie\\";


    private static void scrapeYearsInFilmLinks(){

        ArrayList<String> yearsInFilmLinks = new ArrayList<>();

        try (FileWriter fileWriter = new FileWriter(jsonFilesDir + "yearsLinks.json")) {

            Document decadesDoc = Jsoup.connect("https://en.wikipedia.org/wiki/Lists_of_films")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .header("Accept-Language","*")


                    .get();

            Elements rows = decadesDoc.select("div[class='hlist']");

            JSONArray yearsLinks = new JSONArray();

            fileWriter.write("[\n");

            for (Element row:
                 rows) {

                String decade = row.select("dt").text().replace("s","");

                if(Integer.parseInt(decade.replace("s","")) <= 2020
                && Integer.parseInt(decade.replace("s","")) >= 1910){

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Decade", decade + "s");
                JSONArray jsonArray = new JSONArray();

                Elements years = row.select("dd a");
                    for (Element year:
                         years) {

                    if(Integer.parseInt(year.text()) >= 1914 && Integer.parseInt(year.text()) <= 2023){
                        String link = "https://en.wikipedia.org" + year.attr("href");
                        jsonArray.add(link.replace("\\\\",""));


                        if(years.last().toString().equals(year.toString())){
                            jsonObject.put("YearsLinks",jsonArray);
//                            System.out.println(year.toString());
//                    yearsLinks.add(jsonObject);
                            if(decade.equals("1910")){
//                                System.out.println("something");
                                fileWriter.write(jsonObject.toString());
                            }
                            else{
                                System.out.println(row);
//                        System.out.println("Something");
//                                System.out.println(rows.last());
//                                System.out.println(row);
                                fileWriter.write(jsonObject + ",\n");
                            }
                        }
                    }
                }



                }

            }

            fileWriter.write("\n]");
//            fileWriter.write(yearsLinks.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void scrapeMoviesLinks(){

        JSONParser jsonParser = new JSONParser();

        try(FileReader fileReader = new FileReader(jsonFilesDir + "yearsLinks.json");
                FileWriter fileWriter = new FileWriter(jsonFilesDir + "moviesLinks.json",true)) {

            Object obj = jsonParser.parse(fileReader);

            JSONArray yearsLinksObj = (JSONArray) obj;

            JSONArray moviesLinks = new JSONArray();

            fileWriter.write("[\n");

            for (Object yearLinkObj:
                 yearsLinksObj) {
                JSONObject years = (JSONObject) yearLinkObj;

                String decade = (String) years.get("Decade");

                JSONArray links = (JSONArray) years.get("YearsLinks");


                for (Object link:
                     links) {

                    Document yearDoc = Jsoup.connect((String) link)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                    "OPR/102.0.0.0")
                            .header("Accept-Language","*")


                            .get();

                    Element table = yearDoc.select("table:has(caption)").get(0);
                    Elements rows = table.select("tr");
//                        if(rows.isEmpty()){
//                            System.err.println(link + "\tERROR======================");
//                            throw new Exception("Something went wrong");
//                        }
                    rows.remove(0);

//                        System.out.println(rows);
                    System.out.println(link);

                    for (Element row:
                            rows) {

                        String movieTitle = row.select("td").get(0).text();
                        System.out.print(movieTitle + ":\t");

                        String movieLink = "https://en.wikipedia.org" +
                                row.select("td").get(0).select("a").attr("href");

                        System.out.println(movieLink);
                        JSONObject movieObj = new JSONObject();

                        movieObj.put("MovieTitle",movieTitle);
                        movieObj.put("MovieLink", movieLink);
                        moviesLinks.add(movieObj);

//links.get(links.size() - 1).toString().equals(link.toString())
                            if(yearsLinksObj.get(yearsLinksObj.size() - 1).toString().equals(yearLinkObj.toString())
                            && rows.last().toString().equals(row.toString())
                            && links.get(links.size() - 1).toString().equals(link.toString()))
                            {
                                fileWriter.write(movieObj.toJSONString());
                            }
                            else{
                                fileWriter.write(movieObj.toJSONString() + ",\n");
                            }

                    }

                    System.out.println();

                }



            }

            fileWriter.write("\n]");


        }  catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Movie> scrapeMovies() {
        ArrayList<Movie> moviesList = new ArrayList<>();

        try (FileReader fileReader = new FileReader(jsonFilesDir + "moviesLinks.json")) {

            JSONParser jsonParser = new JSONParser();

            Object obj = jsonParser.parse(fileReader);

            JSONArray moviesLinks = (JSONArray) obj;

            for (Object movieLink:
                 moviesLinks) {

                Document movieDoc = Jsoup.connect((String) ((JSONObject)movieLink).get("MovieLink"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .header("Accept-Language","*")


                        .get();


                //                TITLE
                String title = (String) ((JSONObject) movieLink).get("MovieTitle");

                ////            STUDIO
                String studio;
                if(!movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Production) ~ td").isEmpty()){
                    studio = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Production) ~ td").text().replaceAll("\\[\\d]","").replace('\'','"');
                }else{
                    studio = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Distributed) ~ td").text().replaceAll("\\[\\d]","").replace('\'','"');
                }

                ////            DIRECTORS
//                Elements infoBoxVevent = movieDoc.select("[class='infobox vevent'] tbody tr");
                Elements directorsList = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Directed) ~ td");
                StringBuilder director = new StringBuilder();
                directorsList.forEach(directorElement -> director.append(directorElement.text().replaceAll("'","''").replaceAll("\\[\\d]","").replace('\'','"')));

////           CAST
                Elements starringList = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td li");// th:contains(Starring) ~ td
                StringBuilder starring =  new StringBuilder();

                if(!starringList.isEmpty()) {
                    starringList.forEach(actor -> starring.append(actor.text().replaceAll("'","''").replaceAll("\\[\\d]","").replace('\'','"')).append("  "));
                }
                else {
                    starringList = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td");
                    starring.append(starringList.text().replaceAll("\\[\\d]","").replaceAll("'","\"").replaceAll("\\[\\d]","").replace('\'','"'));
                }

////            POSTER
                Elements infoBoxImage = movieDoc.select("[class='infobox-image']");
                String poster = "https:" + infoBoxImage.select("img").attr("src");

                ////            RELEASE DATE
                String releaseDate = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Release date) ~ td").text().replaceAll("\\[\\d]","").replace('\'','"');

                ////        BUDGET
                String budget = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Budget) ~ td").text().replaceAll("\\[\\d]","").replaceAll("\\[\\d]","").replace('\'','"');

                ////        BOX OFFICE
                String boxOffice = movieDoc.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Box office) ~ td").text().replaceAll("\\[\\d]","").replaceAll("\\[\\d]","").replace('\'','"');

                ////          PLOT
                Elements endOfThePlot = movieDoc.select("h2:has(span[id='Plot']) ~ h2 span:first-child");
                String endOfThePlotId = endOfThePlot.attr("id");
                ArrayList<String> plot = new ArrayList<>();

                Elements plotList = movieDoc.select("h2:has(span[id='Plot'])  ~ p:not(h2:has(span[id='" + endOfThePlotId +  "']),h2:has(span[id='" + endOfThePlotId +  "']) ~ *) ");
                for (Element element : plotList) {
                    plot.add(element.text().replace('\'','"').replaceAll("\\[edit]","").replaceAll("\\[\\d]","") + "\n");
                }






//


//

//
//
//
        Movie movie = new Movie(title,studio,plot,director.toString(),starring.toString(),poster
                ,releaseDate, budget,
                boxOffice, (String) ((JSONObject)movieLink).get("MovieLink"));
//                System.out.println(movie);
//
        moviesList.add(movie);

            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
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
//                            count++;
//                            System.out.println(movie.getLink() + " = " + count);
                            break;
                        }
                        else if(movie.getPlot().indexOf(plotParagraph) == movie.getPlot().size() -1){
                            bufferedWriter.write(String.format(
                                    "'%s'", plotParagraph.replace("\n","").replace('\'','"')

                            ));
                            break;
                        }
                        else{
                            bufferedWriter.write(String.format(
                                    "'%s', ", plotParagraph.replace("\n","").replace('\'','"')

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
//        scrapeYearsInFilmLinks();
//        scrapeMoviesLinks();

        ArrayList<Movie> movies = scrapeMovies();
        fillMovieTableFile();

//        int theFuckedUpOnes = 0;
//
//
//        for (Movie movie:
//             movies) {
//
//            if(movie.getPlot().isEmpty())
//            {
//                System.out.println(movie);
//                theFuckedUpOnes++;
//            }
//
//        }
//
//        System.out.println("theFuckedUpOnesPercentage = " + theFuckedUpOnes / movies.size());
//        System.out.println("theFuckedUpOnesCount = " + theFuckedUpOnes);




    }

//    private static void parseYearLinkObject(JSONObject yearLink){
//
//        String decade = (String) ((JSONObject) yearLink).get("Decade");
//        System.out.println(decade);
//
//        String links = ((JSONObject) yearLink).
//    }


}
