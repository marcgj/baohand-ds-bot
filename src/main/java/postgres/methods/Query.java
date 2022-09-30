package postgres.methods;

import java.sql.Connection;
import java.util.LinkedList;

public class Query {
    final Connection conn;
    final String query;

    public Query(Connection conn, String query) {
        this.conn = conn;
        this.query = query;
    }

    public boolean update() {
        try {
            var statement = conn.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LinkedList<String> getColumn(String column) {
        var list = new LinkedList<String>();

        for (var elem : getColumns(new String[]{column})){
            list.add(elem[0]);
        }
        return list;
    }

    public LinkedList<String[]> getColumns(String[] columns) {
        try {
            var statement = conn.createStatement();
            var result = statement.executeQuery(query);

            final LinkedList list = new LinkedList<String[]>();
            
            while(result.next()){
                String[] tempArr = new String[columns.length];
                
                int i = 0;
                for(var col : columns){
                    tempArr[i++] = result.getString(col);
                }
                list.add(tempArr);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}