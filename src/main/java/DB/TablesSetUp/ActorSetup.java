package DB.TablesSetUp;

import Data.Actor;
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

public class ActorSetup {

    private static final String dir = System.getProperty("user.dir");

    private static int count = 0;
    private static final String sqlFilesDir = dir + "\\src\\main\\java\\DB\\SQLFiles\\Actor\\";


    static ArrayList<Actor> scrapeActors(){
        ArrayList<Actor> actors = new ArrayList<>();

        try{


            Document innerDoc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_actors_with_Academy_Award_nominations")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .header("Accept-Language","*")


                    .get();

            Document document = Jsoup.parse(innerDoc.html());

            Elements table = document.select(".sortable.wikitable");

            Elements rows = table.select("tr");



            rows.remove(0);

            int count = 0;

            for (Element row: rows
                 ) {
                String actorLink = "https://en.wikipedia.org" + row.select("td:first-of-type a").attr("href");

                Document actorDoc = Jsoup.connect(actorLink)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .timeout(0)
                        .header("Accept-Language","*")
                        .get();

//                        NAME
                String name = actorDoc.select("h1 span").text().replaceAll("'","\"").replaceAll("\\[\\d]","");

//                BORN
//                String born = actorDoc.select("tr:has(th:contains(Born)) td").text().replaceAll("'","\"").replaceAll("\\[\\d]","");
//
////                IMAGE_LINK
//                String image_link = !actorDoc.select("td[class='infobox-image'] img").attr("src").isEmpty()?
//                        actorDoc.select("td[class='infobox-image'] img").attr("src").substring(2) : "";
//
//
////                GENERAL_INFO
////                START infobox biography vcard
////                END H2
//                ArrayList<String> general_info = new ArrayList<>();
//                Element endOfGeneralInfo = !actorDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").isEmpty()?
//                        actorDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").get(0):
//                        new Element("undefined");
//                String endOfGeneralInfoId = endOfGeneralInfo.attr("id");
//
//
//                if(!endOfGeneralInfoId.isEmpty()){
//                    Elements generalInfoParagraphs = actorDoc.select("table[class='infobox biography vcard']  ~ :not(h2:has(span[id='" + endOfGeneralInfoId +  "']),h2:has(span[id='" + endOfGeneralInfoId +  "']) ~ *) ");
//
////                    Elements firstTesting = actorDoc.select("h2:has(span[id='" + endOfGeneralInfoId +  "'])");
////                    Element firstTesting = document.select("h2:has(span)").get(2);
////                    System.out.println(generalInfoParagraphs.size() + "\t=\t" +  name + "\t:\t" +  "|" + endOfGeneralInfoId + "|");
//                    for (Element element:
//                            generalInfoParagraphs) {
//                        general_info.add(element.text().replaceAll("'","\"").replaceAll("\\[\\d]",""));
//                    }
//                }

//                System.out.println(general_info);


//                System.out.println(endOfGeneralInfoId + " = " +  name);

//                FILMOGRAPHY

//                https://www.google.com/search?q=paul+newman+filmography+wiki&client=opera
//                https://www.google.com/search?q=leonardo+dicaprio+filmography+wiki&client=opera

                String formattedName = String.join("+",Arrays.asList(name.split(" ")));
                String actorFilmographySearchLink = "https://www.google.com/search?q=" + formattedName + "+filmography+wiki+en&client=opera";
//                System.out.println(actorFilmographyLink);


                Document searchResults = Jsoup.connect(actorFilmographySearchLink)
//                        .followRedirects(false)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .timeout(0)
                        .header("Accept-Language","*")
                        .get();


//                String actorFilmographyLink = Objects.requireNonNull(searchResults.select("div[class='MjjYud'] a").first()).attr("href");
                String actorFilmographyLink = searchResults.select("span[jscontroller='msmzHf'] a").first() != null ?
                        Objects.requireNonNull(searchResults.select("span[jscontroller='msmzHf'] a").first()).attr("href"):
                        "";

                System.out.println(searchResults.select("span") + "\t=\t" + name);


//                if(count ==5) break;

//              RESULT CARD  "MjjYud" || jscontroller=msmzHf || class="yuRUbf


//                String name = row.select("td")

//                Actor actor = new Actor(name,born, image_link, general_info, "");
//                System.out.println(actor);

//                actors.add(actor);

            }

//            System.out.println(table);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return actors;

    }

    static void scrapeFilmographies(){
        try {
//            scra

//                FILMOGRAPHY

//                https://www.google.com/search?q=paul+newman+filmography+wiki&client=opera
//                https://www.google.com/search?q=leonardo+dicaprio+filmography+wiki&client=opera

            String formattedName = String.join("+",Arrays.asList(name.split(" ")));
            String actorFilmographySearchLink = "https://www.google.com/search?q=" + formattedName + "+filmography+wiki+en&client=opera";
//                System.out.println(actorFilmographyLink);


            Document searchResults = Jsoup.connect(actorFilmographySearchLink)
//                        .followRedirects(false)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .timeout(0)
                    .header("Accept-Language","*")
                    .get();


//                String actorFilmographyLink = Objects.requireNonNull(searchResults.select("div[class='MjjYud'] a").first()).attr("href");
            String actorFilmographyLink = searchResults.select("span[jscontroller='msmzHf'] a").first() != null ?
                    Objects.requireNonNull(searchResults.select("span[jscontroller='msmzHf'] a").first()).attr("href"):
                    "";

            System.out.println(searchResults.select("span") + "\t=\t" + name);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void fillActorTable(){
        ArrayList<Actor> actors = scrapeActors();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sqlFilesDir + "fillActorTable.sql"))){

            for (Actor actor: actors){

                bufferedWriter.write(
                        String.format("""
                                ('%s', '%s', %s', ARRAY [
                                
                                """,actor.getName(), actor.getBorn(), actor.getImage_link())
                );

                for (String paragraph: actor.getGeneral_info())
                {
                    if(Objects.equals(actor.getGeneral_info().get(actor.getGeneral_info().size() - 1), paragraph)){

                        bufferedWriter.write(
                                String.format(
                                        """
                                '%s'] , '%s')
                                
                                """, paragraph.replaceAll("'","\""), actor.getFilmography()
                                )
                        );

                    }
                    else{

                        bufferedWriter.write(
                                String.format(
                                        """
                                '%s',
                                """, paragraph.replaceAll("'","\"")
                                )
                        );

                    }
                }

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){


//        MyConnection myConnection = new MyConnection();
//
//        myConnection.connect();
//
//        String createTableActor = myConnection.getSQLFileContent(sqlFilesDir + "createTableActor.sql");
//        String fillActorTable = myConnection.getSQLFileContent(sqlFilesDir + "fillActorTable.sql");
//        QueryResult queryResult = myConnection.query(createTableActor);
//        System.out.println(queryResult);

        scrapeActors();
    }
}
