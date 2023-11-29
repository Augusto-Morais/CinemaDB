package DB.TablesSetUp;

import Data.Actor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ActorSetupTest {
    private static final String dir = System.getProperty("user.dir");

    private static int count = 0;

    private static final String jsonFilesDir = dir + "\\src\\main\\java\\DB\\JSONFiles\\Actor\\";
    private static final String sqlFilesDir = dir + "\\src\\main\\java\\DB\\SQLFiles\\Actor\\";


    public static void scrapeActorsLinks(){
        ArrayList<Actor> actors = new ArrayList<>();

        try (FileWriter fileWriter = new FileWriter(jsonFilesDir + "actorsLinks.json",true)){


            Document innerDoc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_actors_with_Academy_Award_nominations")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                            "OPR/102.0.0.0")
                    .header("Accept-Language","*")
                    .get();

            Document document = Jsoup.parse(innerDoc.html());

            Elements table = document.select(".sortable.wikitable");

            Elements rows = table.select("tr");


//            int count = 0;

            rows.remove(0);

            fileWriter.write("[\n");

            for (Element row: rows
            ) {
//                count++;
//                if(count == 11) break;
                JSONObject actorObject = new JSONObject();

                String actorLink = "https://en.wikipedia.org" + row.select("td:first-of-type a").attr("href");

                String name = row.select("td:first-of-type").text().replace('\'','"').replaceAll("\\[\\d]","");

                actorObject.put("ActorName", name);
                actorObject.put("WikiLink", actorLink);

//                System.out.println(actorObject);


                if(rows.last().toString().equals(row.toString())){
                    fileWriter.write("\t" + actorObject + "\n");
                }
                else{
                    fileWriter.write("\t" + actorObject + ",\n");
                }



            }
            fileWriter.write("]");



//            System.out.println(table);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void scrapeActors(){

        try (FileReader fileReader = new FileReader(jsonFilesDir + "actorsLinks.json");
            FileWriter fileWriter = new FileWriter(jsonFilesDir + "actorsInfo.json")) {


            int count = 0;

            JSONParser jsonParser = new JSONParser();

            Object obj = jsonParser.parse(fileReader);

            JSONArray actorsObjects = (JSONArray) obj;

            for (int i = 0; i < actorsObjects.size(); i++) {
                count++;
                Object actorObject = actorsObjects.get(i);

                String actorLink = ((String) ((JSONObject) actorObject).get("WikiLink")).replace("https","http");
                System.out.println(actorLink + "\t" + count + "/" + actorsObjects.size());



//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36

//                Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/116.0

                Document actorDoc = Jsoup.connect(actorLink)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
                                "OPR/102.0.0.0")
                        .timeout(0)
                        .header("Accept-Language","*")
                        .get();

//                 NAME
                String name = actorDoc.select("h1 span").text().replace('\'','"').replaceAll("\\[\\d]","");

//                BORN
                String born = actorDoc.select("tr:has(th:contains(Born)) td").text().replace('\'','"').replaceAll("\\[\\d]","");

////               IMAGE_LINK
                String image_link = !actorDoc.select("td[class='infobox-image'] img").attr("src").isEmpty()?
                        actorDoc.select("td[class='infobox-image'] img").attr("src").substring(2) : "";



//
////                GENERAL_INFO
//                START infobox biography vcard
//                END H2
                ArrayList<String> general_info = new ArrayList<>();
                Element endOfGeneralInfo = !actorDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").isEmpty()?
                        actorDoc.select("table[class='infobox biography vcard'] ~ h2 span:first-child").get(0):
                        new Element("undefined");
                String endOfGeneralInfoId = endOfGeneralInfo.attr("id");


                if(!endOfGeneralInfoId.isEmpty()){
                    Elements generalInfoParagraphs = actorDoc.select("table[class='infobox biography vcard']  ~ p:not(h2:has(span[id='" + endOfGeneralInfoId +  "']),h2:has(span[id='" + endOfGeneralInfoId +  "']) ~ *) ");

//                    Elements firstTesting = actorDoc.select("h2:has(span[id='" + endOfGeneralInfoId +  "'])");
//                    Element firstTesting = document.select("h2:has(span)").get(2);
//                    System.out.println(generalInfoParagraphs.size() + "\t=\t" +  name + "\t:\t" +  "|" + endOfGeneralInfoId + "|");
                    for (Element element:
                            generalInfoParagraphs) {
                        general_info.add(element.text().replace('\'','"').replaceAll("\\[\\d]",""));
                    }
                }


                Actor actor = new Actor(name, born,image_link,general_info,
                        null);






                System.out.println(actor);
            }

//            for (Object actorObject : actorsObjects){
//
//                count++;
//
//                String actorLink = ((String) ((JSONObject) actorObject).get("WikiLink")).replace("https","http");
//                System.out.println(actorLink + "\t" + count + "/" + actorsObjects.size());
//
////                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
////                        " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
////                        "OPR/102.0.0.0"
//
//
//
//                Document actorDoc = Jsoup.connect(actorLink)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
//                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
//                                "OPR/102.0.0.0")
//                        .timeout(0)
//                        .header("Accept-Language","*")
//                        .get();
//
////                 NAME
//                String name = actorDoc.select("h1 span").text().replace('\'','"').replaceAll("\\[\\d]","");
//
////                BORN
//                String born = actorDoc.select("tr:has(th:contains(Born)) td").text().replace('\'','"').replaceAll("\\[\\d]","");
//
//////               IMAGE_LINK
//                String image_link = !actorDoc.select("td[class='infobox-image'] img").attr("src").isEmpty()?
//                        actorDoc.select("td[class='infobox-image'] img").attr("src").substring(2) : "";
//
//
//
////
//////                GENERAL_INFO
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
//                    Elements generalInfoParagraphs = actorDoc.select("table[class='infobox biography vcard']  ~ p:not(h2:has(span[id='" + endOfGeneralInfoId +  "']),h2:has(span[id='" + endOfGeneralInfoId +  "']) ~ *) ");
//
////                    Elements firstTesting = actorDoc.select("h2:has(span[id='" + endOfGeneralInfoId +  "'])");
////                    Element firstTesting = document.select("h2:has(span)").get(2);
////                    System.out.println(generalInfoParagraphs.size() + "\t=\t" +  name + "\t:\t" +  "|" + endOfGeneralInfoId + "|");
//                    for (Element element:
//                            generalInfoParagraphs) {
//                        general_info.add(element.text().replace('\'','"').replaceAll("\\[\\d]",""));
//                    }
//                }
//
//
//                Actor actor = new Actor(name, born,image_link,general_info,
//                        null);
//
//
//
//
//
//
//                System.out.println(actor);
//
//
//            }




        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void scrapeFilmographies(){

    }

    public static void main(String[] args) {
//        scrapeActorsLinks();
          scrapeActors();

    }

    public static void scrapeActorsT()
    {
        //
////                        NAME
//                String name = actorDoc.select("h1 span").text().replace('\'','"').replaceAll("\\[\\d]","");
//
////                BORN
//                String born = actorDoc.select("tr:has(th:contains(Born)) td").text().replace('\'','"').replaceAll("\\[\\d]","");
////
//////                IMAGE_LINK
//                String image_link = !actorDoc.select("td[class='infobox-image'] img").attr("src").isEmpty()?
//                        actorDoc.select("td[class='infobox-image'] img").attr("src").substring(2) : "";
////
////
//////                GENERAL_INFO
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
//                        general_info.add(element.text().replace('\'','"').replaceAll("\\[\\d]",""));
//                    }
//                }
//
////                System.out.println(general_info);
//
//
////                System.out.println(endOfGeneralInfoId + " = " +  name);
//
////                FILMOGRAPHY
//
////                https://www.google.com/search?q=paul+newman+filmography+wiki&client=opera
////                https://www.google.com/search?q=leonardo+dicaprio+filmography+wiki&client=opera
//
////                String formattedName = String.join("+",Arrays.asList(name.split(" ")));
////                String actorFilmographySearchLink = "https://www.google.com/search?q=" + formattedName + "+filmography+wiki+en&client=opera";
//////                System.out.println(actorFilmographyLink);
////
////
////                Document searchResults = Jsoup.connect(actorFilmographySearchLink)
//////                        .followRedirects(false)
////                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
////                                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 " +
////                                "OPR/102.0.0.0")
////                        .timeout(0)
////                        .header("Accept-Language","*")
////                        .get();
////
////
//////                String actorFilmographyLink = Objects.requireNonNull(searchResults.select("div[class='MjjYud'] a").first()).attr("href");
////                String actorFilmographyLink = searchResults.select("span[jscontroller='msmzHf'] a").first() != null ?
////                        Objects.requireNonNull(searchResults.select("span[jscontroller='msmzHf'] a").first()).attr("href"):
////                        "";
//
////                System.out.println(searchResults.select("span") + "\t=\t" + name);
//
//
////                if(count ==5) break;
//
////              RESULT CARD  "MjjYud" || jscontroller=msmzHf || class="yuRUbf
//
//
////                String name = row.select("td")
//
//                Actor actor = new Actor(name,born, image_link, general_info, "");
//                System.out.println(actor);

//                actors.add(actor);
//                System.out.println(name + "\t" + actorLink);
    }
}
