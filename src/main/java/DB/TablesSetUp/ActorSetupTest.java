package DB.TablesSetUp;

import Data.Actor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
import java.util.Arrays;

public class ActorSetupTest {
    private static final String dir = System.getProperty("user.dir");

    private static int count = 0;

    private static final String jsonFilesDir = dir + "\\src\\main\\\\JSONFiles\\Actor\\";
    private static final String sqlFilesDir = dir + "\\src\\main\\\\SQLFiles\\Actor\\";


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
                String name = (String) ((JSONObject) actorObject).get("ActorName");

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

//                String movieTitle = movie.get("original_title") != null? (String) movie.get("original_title") : (String) movie.get("name");
//                String moviePoster = movie.get("poster_path") != null? (String) movie.get("poster_path") : null;
//                String overview = movie.get("overview") != null? (String) movie.get("overview") : null;
//                String year =  movie.get("release_date") !=null? ((String) movie.get("release_date")).split("-")[0] : ((String) movie.get("first_air_date")).split("-")[0];
//                String role = movie.get("character") != null? (String) movie.get("character"): null;

                String filmography = getFilmography(getActorID(name)) ;
                
                




                Actor actor = new Actor(name, born,image_link,general_info,
                        filmography);






                System.out.println(actor);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static long getActorID(String actorName){

        long actorId;

        try {
            String[] formattedNameTokens = actorName.split(" ");
            String formattedName = String.join("%20", formattedNameTokens);
            System.out.println(formattedName);


            OkHttpClient client = new OkHttpClient();
//https://api.themoviedb.org/3/person/{person_id}
            Request actorIDrequest = new Request.Builder()
                    .url(String.format("https://api.themoviedb.org/3/search/person?query=%s&include_adult=false&language=en-US&page=1", formattedName))
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2EzYjBjYWIyNWQ4OGEyMTFmZjljYmEwODI0YTRlNSIsInN1YiI6IjY1NjEzZGRhMjQ0MTgyMDBlYmU0MTFhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0e0pBqAhlGkm_tYkecunAxRJPKzbgMHDd6D1C2_vb7Q")
                    .build();

            Response response;
            response = client.newCall(actorIDrequest).execute();

            JSONParser jsonParser = new JSONParser();


            JSONArray results = (JSONArray) ((JSONObject) jsonParser.parse(response.body().string())).get("results");
            System.out.println(results.size());


            if(!results.isEmpty()) {
                JSONObject actorObjectIMDB = (JSONObject) results.get(0);

                actorId = (long) actorObjectIMDB.get("id");
                return actorId;
            }
            else {
                return -1;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getFilmography(long actorID){
//        String[] formattedNameTokens = actorName.split(" ");
//        String formattedName = String.join("%20", formattedNameTokens);

        StringBuilder filmography = new StringBuilder();

        try {

            OkHttpClient client = new OkHttpClient();
            System.out.println(actorID);

            Request filmographyRequest = new Request.Builder()
                    .url(String.format("https://api.themoviedb.org/3/person/%s/combined_credits?language=en-US", actorID))
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2EzYjBjYWIyNWQ4OGEyMTFmZjljYmEwODI0YTRlNSIsInN1YiI6IjY1NjEzZGRhMjQ0MTgyMDBlYmU0MTFhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0e0pBqAhlGkm_tYkecunAxRJPKzbgMHDd6D1C2_vb7Q")
                    .build();

            Response response = client.newCall(filmographyRequest).execute();

            JSONParser jsonParser = new JSONParser();

            JSONArray cast = (JSONArray) ((JSONObject) jsonParser.parse(response.body().string())).get("cast");


            for (Object o : cast) {
                JSONObject movie = (JSONObject) o;

                String movieTitle = movie.get("title") != null? (String) movie.get("title") : (String) movie.get("name");

                String moviePoster = "https://image.tmdb.org/t/p/original" + (movie.get("poster_path") != null? (String) movie.get("poster_path") : null);

                String overview = movie.get("overview") != null? (String) movie.get("overview") : null;

                String year =  movie.get("release_date") !=null ? ((String) movie.get("release_date")).split("-")[0] : null;
//                ((String) movie.get("first_air_date")).split("-")[0]

                String role = movie.get("character") != null? (String) movie.get("character"): null;

/// https://api.themoviedb.org/3/person/6193/combined_credits?language=en-US/3HoDechba9wJhTUPOZTwxJBkh1Z.jpg

                filmography.append(
                        String.format("""
                        {
                            "MovieTitle" = "%s",
                            "MoviePoster" = "%s",
                            "Overview" = "%s",
                            "Year" = "%s",
                            "Role" = "%s",
                        },

                        """,movieTitle, moviePoster, overview, year, role)
                );


            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return filmography.toString();

    }

    public static void main(String[] args) {
          scrapeActors();

    }

}
