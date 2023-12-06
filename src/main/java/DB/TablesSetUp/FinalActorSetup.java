package DB.TablesSetUp;

import Data.Actor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FinalActorSetup {

    private static final String dir = System.getProperty("user.dir");
    private static final String jsonFilesDir = dir + "\\src\\main\\JSONFiles\\Actor\\";


    public static String formatName(String actorName){

        return String.join("%20",actorName.replace("á","%C3%A1")
                .replace("\"","%27").replace("é","%C3%A9")
                .replace("í","%C3%AD").split(" "));

    }
    public static long getActorID(String actorName){

        long actorId;

        try {

//            String[] formattedNameTokens = actorName.split(" ");
//            String formattedName = String.join("%20", formattedNameTokens);
            String formattedName = formatName(actorName);

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
    public static ArrayList<Actor> getActors(){

        long actorID = 0;

        try(FileReader fileReader = new FileReader(jsonFilesDir + "actorsLinks.json")) {

            JSONParser jsonParser = new JSONParser();

            Object obj = jsonParser.parse(fileReader);

            JSONArray actorsObjects = (JSONArray) obj;
            int count = 0;
            int words = 0;

            for (int i = 0; i < actorsObjects.size(); i++) {
                if(i == 50) break;
                count++;
                System.out.println("=========================================================");
                System.out.println(count + " / " + actorsObjects.size());
                Object actorObject = actorsObjects.get(i);

                String actorName = (String) ((JSONObject) actorObject).get("ActorName");

                


                OkHttpClient client = new OkHttpClient();
//https://api.themoviedb.org/3/person/{person_id}
                actorID = getActorID(actorName);


                    //https://api.themoviedb.org/3/person/{person_id}
                    Request actorDetailsRequest = new Request.Builder()
                            .url(String.format(String.format("https://api.themoviedb.org/3/person/%s", actorID)))
                            .get()
                            .addHeader("accept", "application/json")
                            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2EzYjBjYWIyNWQ4OGEyMTFmZjljYmEwODI0YTRlNSIsInN1YiI6IjY1NjEzZGRhMjQ0MTgyMDBlYmU0MTFhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0e0pBqAhlGkm_tYkecunAxRJPKzbgMHDd6D1C2_vb7Q")
                            .build();

//                    234352 Margot Robbie
                           Response response = client.newCall(actorDetailsRequest).execute();

                    JSONObject actorDetailsObject = (JSONObject) jsonParser.parse(response.body().string());

                    String born = actorDetailsObject.get("birthday") + "   " + actorDetailsObject.get("place_of_birth");

                    String imageURL = (String) actorDetailsObject.get("profile_path");

                    ArrayList<String> general_info = new ArrayList<>();
                    general_info.add((String) actorDetailsObject.get("biography"));
//                    (String) actorDetailsObject.get("biography")

                System.out.println(actorID);
                    String filmography = getFilmography(actorID);

                    Actor actor = new Actor(actorName, born, imageURL, general_info, filmography);

                    words += actor.getGeneral_info().get(0).split(" ").length;

                    System.out.println(actor);

                

            }

            System.out.println((double) words/50);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getFilmography(long actorID){

        StringBuilder filmography = new StringBuilder();

        try {

            OkHttpClient client = new OkHttpClient();


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

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return filmography != null ? filmography.toString() : null;

    }

    public static void main(String[] args) {
        getActors();
    }
}
