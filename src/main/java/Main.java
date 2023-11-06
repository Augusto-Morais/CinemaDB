import Data.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main{

    public static void scrapeMovie(String movieURL) throws IOException {

        Document innerDoc = Jsoup.connect(movieURL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                        "OPR/102.0.0.0")
                .header("Accept-Language","*")


                .get();

        Document document = Jsoup.parse(innerDoc.html());

//        TITLE
        String title = document.select("h1").text();

//      PLOT
//        boolean isThereAVoiceCastSpan = !document.select("span[id='Voice_cast']").isEmpty();
//        ArrayList<String> plot = new ArrayList<>();
//
//
//        if(isThereAVoiceCastSpan){
//            Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='Voice_cast']),h2:has(span[id='Voice_cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//        }
//        else{
//            Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='Cast']),h2:has(span[id='Cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//        }

        //          PLOT
        Elements endOfThePlot = document.select("h2:has(span[id='Plot']) ~ h2 span:first-child");
        String endOfThePlotId = endOfThePlot.attr("id");
        ArrayList<String> plot = new ArrayList<>();

        Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='" + endOfThePlotId +  "']),h2:has(span[id='" + endOfThePlotId +  "']) ~ *) ");
            for (Element element : plotList) {
                plot.add(element.text().replaceAll("'","''") + "\n");
            }


//        boolean isThereAVoiceCastSpan = !document.select("span[id='Voice_cast']").isEmpty();
//        boolean isThereAStoryLineSpan = !document.select("span[id='Storylines']").isEmpty();
//        boolean isThereAnH3Plot = !document.select("h3:contains(Plot)").isEmpty();
//        boolean isThereAProductionSpan = !document.select("span[id='Production']").isEmpty();
//        ArrayList<String> plot = new ArrayList<>();
//
//
//        if(isThereAVoiceCastSpan){
//            Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='Voice_cast']),h2:has(span[id='Voice_cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//            System.out.println("FIRST");
//        }
//        else if(isThereAStoryLineSpan){
//            Elements plotList = document.select("h2:has(span[id='Storylines'])  ~ :not(h2:has(span[id='Cast']),h2:has(span[id='Cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//            System.out.println("SECOND");
//
//        }
//        else if(isThereAnH3Plot){
//            Elements plotList = document.select("h3:contains(Plot)  ~ :not(h2:has(span[id='Cast']),h2:has(span[id='Cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//            System.out.println("THIRD");
//
//        } else if (isThereAProductionSpan) {
//            Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='Production']),h2:has(span[id='Production']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//            System.out.println("FOURTH");
//
//        } else{
//            Elements plotList = document.select("h2:has(span[id='Plot'])  ~ :not(h2:has(span[id='Cast']),h2:has(span[id='Cast']) ~ *) ");
//            for (Element element : plotList) {
//                plot.add(element.text() + "\n\n");
//            }
//            System.out.println("FIFTH");
//
//        }

//        ArrayList<String> s = new ArrayList<>();
//        s.add(String.valueOf(isThereACastSpan));


//            POSTER
        Elements infoBoxImage = document.select("[class='infobox-image']");
        String poster = "https:" + infoBoxImage.select("img").attr("src");

//            DIRECTORS
        Elements infoBoxVevent = document.select("[class='infobox vevent'] tbody tr");
        Elements directorsList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Directed) ~ td");
        StringBuilder director = new StringBuilder();
        directorsList.forEach(directorElement -> director.append(directorElement.text()));

//           CAST
        Elements starringList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td li");// th:contains(Starring) ~ td
        StringBuilder starring =  new StringBuilder();

        if(!starringList.isEmpty()) {
            starringList.forEach(actor -> starring.append(actor.text()).append("  "));
        }
        else {
            starringList = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Starring) ~ td");
            starring.append(starringList.text());
        }

//            STUDIO
        String studio = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Production) ~ td").text();

//            RELEASE DATE
        String releaseDate = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Release date) ~ td").text().replaceAll("\\[\\d]","");;

//        BUDGET
        String budget = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Budget) ~ td").text().replaceAll("\\[\\d]","");

//        BOX OFFICE
        String boxOffice = document.select("[class='infobox vevent'] tbody tr:has(th) th:contains(Box office) ~ td").text().replaceAll("\\[\\d]","");



        Movie movie = new Movie(title,studio,plot,director.toString(),starring.toString(),poster,releaseDate, budget,
                boxOffice, "null");

        System.out.println(movie);
    }
    public static void main(String[] args) throws IOException {
        ArrayList<String> moviesURls = new ArrayList<>();
        moviesURls.add("https://en.wikipedia.org/wiki/Psycho_(1960_film)");
//        moviesURls.add("https://en.wikipedia.org/wiki/Demon_Slayer:_Kimetsu_no_Yaiba_–_The_Movie:_Mugen_Train");
//        moviesURls.add("https://en.wikipedia.org/wiki/Avengers:_Endgame");
//        moviesURls.add("https://en.wikipedia.org/wiki/Shrek_2");
//        moviesURls.add("https://en.wikipedia.org/wiki/Harry_Potter_and_the_Deathly_Hallows_–_Part_2");
//        moviesURls.add("https://en.wikipedia.org/wiki/Frozen_(2013_film)");

        moviesURls.forEach(movieURL -> {
            try {
                scrapeMovie(movieURL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        https://en.wikipedia.org/wiki/Psycho_(1960_film)
//        https://en.wikipedia.org/wiki/Demon_Slayer:_Kimetsu_no_Yaiba_–_The_Movie:_Mugen_Train
//        https://en.wikipedia.org/wiki/Avengers:_Endgame
//        https://en.wikipedia.org/wiki/Shrek_2
//        https://en.wikipedia.org/wiki/Harry_Potter_and_the_Deathly_Hallows_–_Part_2
//        https://en.wikipedia.org/wiki/Frozen_(2013_film)


//        System.out.println(movie);
    }
}
