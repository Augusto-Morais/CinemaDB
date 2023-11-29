package Testing;
import DB.MyConnection;
import DB.QueryResult;
import DB.Table;

public class MyConnectionTest {
//    5432 port
    public static void main(String[] args) {
        MyConnection connection = new MyConnection();
        connection.connect();

        String dir = System.getProperty("user.dir");
        String sqlFilesDir = dir + "\\src\\main\\java\\DB\\SQLFiles\\";
//        System.out.println(dir);

//       TABLE MOVIE
//       String createTableMovie = connection.getSQLFileContent(sqlFilesDir + "Movie\\createTableMovie.sql");
//
//        connection.query(createTableMovie);

//        String fillMovieTable = connection.getSQLFileContent(sqlFilesDir + "Movie\\fillMovieTable.sql");
//
//        connection.query(fillMovieTable);



//        TABLE ACTOR
//        String createTableActor = connection.getSQLFileContent(sqlFilesDir + "Actor\\createTableActor.sql");
//
//        connection.query(createTableActor);

//        connection.query(connection.getSQLFileContent(sqlFilesDir + "Actor\\fillActorTable.sql"));


//        TABLE STUDIO
//        String createTableStudio = connection.getSQLFileContent(sqlFilesDir + "Studio\\createTableStudio.sql");
//
//        connection.query(createTableStudio);

        connection.query(connection.getSQLFileContent(sqlFilesDir + "Studio\\fillStudioTable.sql"));



//        connection.query("INSERT INTO movie (title, boxoffice, year) VALUE ('Avengers', '$2.000.000.000', 2019);");
//        System.out.println(connection.getSQLFileContent(sqlFilesDir +  "\\fillMovieTable.sql"));

//        SELECT QUERY
//        QueryResult queryResult = connection.query("SELECT * FROM movie");
//        Table table = queryResult.getTable();
//        if(table != null){
//            System.out.println(table.getColumns());
//            System.out.println(table.getRows());
//        }
//        System.out.println("Affected Rows: " + queryResult.getAffectedRows());

        //INSERT QUERY
//        connection.query(connection.getSQLFileContent(sqlFilesDir + "fillMovieTable.sql"));

        //DELETE QUERY
//        connection.query("DELETE FROM movie WHERE;");





    }
}
