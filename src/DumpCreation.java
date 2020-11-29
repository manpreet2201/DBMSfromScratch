import org.json.JSONArray;
import java.io.*;
import java.util.*;
import org.json.JSONObject;

public class DumpCreation {
    public List<File> listFilesForFolder(final File folder) {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }

    public void CreateDump(String DatabaseName) throws IOException {
        System.out.println(System.getProperty("user.dir"));

        List<String> queryList=new ArrayList<>();
        String outputFile = DatabaseName+" script.sql";
        final File folder = new File(System.getProperty("user.dir") + "/"+DatabaseName+"/");
        var files = listFilesForFolder(folder);
        for (var file : files) {
            File tableFile = file.getAbsoluteFile();
            String query = "";
            String table = "";
            int count = 0;
            BufferedReader bufferedReaderObject = new BufferedReader(new FileReader(tableFile));
            while ((table = bufferedReaderObject.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(table);
                query += "create table " + jsonObject.get("tablename") + "(";
                JSONArray columnList = (JSONArray) jsonObject.get("columnlist");
                for (int i = 0; i < columnList.length(); i++) {
                    JSONObject obj = (JSONObject) columnList.get(i);
                    Set<String> column = obj.keySet();
                    for (var columnName : column) {
                        if (count != 0) {
                            query += ",";
                        }
                        count++;
                        query += columnName + " " + obj.get(columnName);
                    }
                }
                query += ")";
            }
            queryList.add(query);
        }
        if(queryList!=null&&queryList.size()>0)
        {
            BufferedWriter bufferedWriterObject = new BufferedWriter(new FileWriter(outputFile));
            for(var query:queryList) {
                System.out.println(query);
                bufferedWriterObject.write(query+"\n");
            }
            bufferedWriterObject.close();
            System.out.println(outputFile+" generated for database "+DatabaseName);
        }
    }
}

//https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java