package DB.TablesSetUp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;

public class APITest {
    public static void main(String[] args) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient();

//  Parameters
//        query: Actor Name
        Request actorIDrequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/person?query=Leonardo%20Dicaprio&include_adult=false&language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2EzYjBjYWIyNWQ4OGEyMTFmZjljYmEwODI0YTRlNSIsInN1YiI6IjY1NjEzZGRhMjQ0MTgyMDBlYmU0MTFhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0e0pBqAhlGkm_tYkecunAxRJPKzbgMHDd6D1C2_vb7Q")
                .build();

        Response response = null;
        response = client.newCall(actorIDrequest).execute();
        JSONParser jsonParser = new JSONParser();

        JSONArray results = (JSONArray) ((JSONObject) jsonParser.parse(response.body().string())).get("results");

        System.out.println("Response Code: " + response.code());


        System.out.println(results.size());

        if(!results.isEmpty()){
            JSONObject actor = (JSONObject) results.get(0);

            System.out.println(actor.get("name"));
            System.out.println(actor.get("id"));
        }


        Request filmographyRequest = new Request.Builder()
                .url("https://api.themoviedb.org/3/person/6193/combined_credits?language=en-US")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzY2EzYjBjYWIyNWQ4OGEyMTFmZjljYmEwODI0YTRlNSIsInN1YiI6IjY1NjEzZGRhMjQ0MTgyMDBlYmU0MTFhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.0e0pBqAhlGkm_tYkecunAxRJPKzbgMHDd6D1C2_vb7Q")
                .build();

        response = client.newCall(filmographyRequest).execute();

        JSONArray cast = (JSONArray) ((JSONObject) jsonParser.parse(response.body().string())).get("cast");

        for (Object o : cast) {
            JSONObject movie = (JSONObject) o;

            String movieTitle = movie.get("original_title") != null? (String) movie.get("original_title") : (String) movie.get("name");
            String moviePoster = movie.get("poster_path") != null? (String) movie.get("poster_path") : null;
            String overview = movie.get("overview") != null? (String) movie.get("overview") : null;
            String year =  movie.get("release_date") !=null && movie.get("name") != null? ((String) movie.get("release_date")).split("-")[0] : ((String) movie.get("first_air_date")).split("-")[0];
            String role = movie.get("character") != null? (String) movie.get("character"): null;
            System.out.println("\tMovie: ");
/// https://api.themoviedb.org/3/person/6193/combined_credits?language=en-US/3HoDechba9wJhTUPOZTwxJBkh1Z.jpg
            System.out.println(movieTitle);
            System.out.println(moviePoster);
            System.out.println(overview);
            System.out.println(year);
            System.out.println(role);
            System.out.println();

        }




//        System.out.println(obj);
    }
}
