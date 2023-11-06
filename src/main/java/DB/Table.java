package DB;

import java.util.ArrayList;

public class Table {
    private ArrayList<String> columns;
    private ArrayList<ArrayList<String>> rows;


    public Table(ArrayList<String> columns, ArrayList<ArrayList<String>> rows){
        this.columns = columns;
        this.rows = rows;
    }

    public ArrayList<ArrayList<String>> getRows() {
        return rows;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void setRows(ArrayList<ArrayList<String>> rows) {
        this.rows = rows;
    }
}
