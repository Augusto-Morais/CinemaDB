package DB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class MyConnection {
    private final String url = "jdbc:postgresql://localhost/MyDB";
    private final String user = "postgres";
    private final String password = "root";

    private Connection connection;

    public void connect(){
        Connection conn = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public String getSQLFileContent(String path){
        StringBuilder stringBuilder = new StringBuilder();
        try(FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {


            bufferedReader.lines().forEach(stringBuilder::append);

        } catch (IOException e) {
            System.err.println(e);
        }

        return stringBuilder.toString();
    }

    public QueryResult query(String query){

        QueryResult queryResult = null;
        Table table;
        int affectedRows = 0;
//        Statement statement = new con

        try {
            if(connection == null || connection.isClosed()) {
//                System.err.println("A Data base connection must be established before executing an sql query");
                throw new SQLException("A Data base connection must be established before executing an sql query");
            }
            else{
                Statement statement = connection.createStatement();

                boolean isThereAResultSet = statement.execute(query);




                if(isThereAResultSet){
                    ResultSet resultSet = statement.getResultSet();
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    int columnCount = metaData.getColumnCount();
                    ArrayList<String> columns = new ArrayList<>();
                    ArrayList<ArrayList<String>> rows = new ArrayList<>();

//                    resultSet.next();

                    for (int currentColumn = 1; currentColumn <= columnCount; currentColumn++){
                        String columnName = metaData.getColumnName(currentColumn);
                        columns.add(columnName);


                    }

//                    System.out.println(columns);

                    while(resultSet.next()){
                        ArrayList<String> row = new ArrayList<>();

                        for (String columnName:columns) {
                            row.add(resultSet.getString(columnName));
                        }
                        rows.add(row);
                    }

//                    System.out.println(rows);

                    table = new Table(columns, rows);
                    queryResult = new QueryResult(table);

//
//                    while(resultSet.next()){
//                        resultSet.getString();
//                    }
                }
                else{
                    affectedRows = statement.getUpdateCount();
                    queryResult = new QueryResult(affectedRows);
                }


            }

        }
        catch (SQLException e){
            e.printStackTrace();
//            System.err.println(e);
        }
        return queryResult;
    }

//    public String query(String query){
////        Statement statement = new con
//
//        try {
//            if(connection == null || connection.isClosed()) {
////                System.err.println("A Data base connection must be established before executing an sql query");
//                throw new SQLException("A Data base connection must be established before executing an sql query");
//            }
//            else{
//                Statement statement = connection.createStatement();
//
//                boolean isThereAResultSet = statement.execute(query);
//
//
//
//                ResultSet resultSet = statement.executeQuery(query);
//
//
//                if(isThereAResultSet){
//                    System.out.println(resultSet);
//
//                    while(resultSet.next()){
//                        resultSet.getString(1);
//                    }
//                }
//
//
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
////            System.err.println(e);
//        }
//        return query;
//    }



}
