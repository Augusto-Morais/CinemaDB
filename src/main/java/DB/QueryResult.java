package DB;

import DB.Table;

public class QueryResult {
    private Table table;

    private int affectedRows;

    public  QueryResult(Table table){
        this.table = table;
        this.affectedRows = 0;
    }

    public  QueryResult(int affectedRows){
        this.affectedRows = affectedRows;
        this.table = null;
    }

    public Table getTable() {
        return table;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return
                "QueryResult:{\n" +
                        "Table : " + this.getTable() + "\n" +
                        "AffectedRows : " + this.getAffectedRows() + "\n" +
                        "}\n";

    }
}
