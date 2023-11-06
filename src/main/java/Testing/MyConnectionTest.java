package Testing;
import DB.MyConnection;
import DB.QueryResult;
import DB.Table;

public class MyConnectionTest {
    public static void main(String[] args) {
        MyConnection connection = new MyConnection();
        connection.connect();

        String dir = System.getProperty("user.dir");
        String sqlFilesDir = dir + "\\src\\main\\java\\DB\\SQLFiles\\";
        System.out.println(dir);

//        System.out.println(connection.getSQLFileContent(dir + "\\src\\main\\java\\DB\\SQLFiles\\createTableMovie.sql"));

//        connection.query(connection.getSQLFileContent(dir + "\\src\\main\\java\\DB\\SQLFiles\\createTableMovie.sql"));

//        connection.query(connection.getSQLFileContent(sqlFilesDir +  "\\fillMovieTable.sql"));

//        connection.query("INSERT INTO movie (title, boxoffice, year) VALUE ('Avengers', '$2.000.000.000', 2019);");
//        System.out.println(connection.getSQLFileContent(sqlFilesDir +  "\\fillMovieTable.sql"));

//        SELECT QUERY
        QueryResult queryResult = connection.query("SELECT * FROM movie");
        Table table = queryResult.getTable();
        if(table != null){
            System.out.println(table.getColumns());
            System.out.println(table.getRows());
        }
        System.out.println("Affected Rows: " + queryResult.getAffectedRows());

        //INSERT QUERY
//        connection.query(connection.getSQLFileContent(sqlFilesDir + "fillMovieTable.sql"));

        //DELETE QUERY
//        connection.query("DELETE FROM movie WHERE;");





    }
}
