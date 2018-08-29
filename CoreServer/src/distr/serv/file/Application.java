package distr.serv.file;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.JSONArray;

import org.apache.commons.io.FilenameUtils;

public class Application
{
    private static PrintWriter pwLog;

    public static void main(String[] args)
    {
        log("Starting Core Server...");

        log("Looking for server state file in " + System.getProperty("user.dir")+"...");
        File stateFile = new File(System.getProperty("user.dir"), "state.json");
        if (stateFile.exists())
        {
            List<String> changedFiles = new ArrayList<>();
            List<String> removedFiles = new ArrayList<>();
            log("Found an existing state file...");
            log("Checking for changes...");

            try
            {
                String content = FileUtils.readFileToString(stateFile, "UTF-8");
                JSONObject stateObject = new JSONObject(content);
                JSONArray list = stateObject.getJSONArray("files");
                for(int i=0;i< list.length();i++)
                {
                    JSONObject fileJSON = list.getJSONObject(i);
                    String name = fileJSON.getString("name");
                    File currentFile = new File(System.getProperty("user.dir"), name);
                    if(!currentFile.exists())
                        remove
                }
            }
            catch (IOException e)
            {
                log("Cannot reade state file.");
                logError(e);
                System.exit(1);
            }

        }
        else
        {
            log("Could not find an existing state file. Scanning the directory for files and creating a state file...");
            JSONObject stateObj = new JSONObject();
            stateObj.put("type", "CoreServer");
            stateObj.put("date", (new Date().toString()));

            JSONArray list = new JSONArray();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            for(File file : new File(System.getProperty("user.dir")).listFiles())
            {
                if(!file.isDirectory() && !FilenameUtils.isExtension(file.getAbsolutePath(),"log") && !FilenameUtils.isExtension(file.getAbsolutePath(), "json"))
                {
                    JSONObject fileObj = new JSONObject();
                    fileObj.put("name", file.getName());
                    fileObj.put("size", file.getTotalSpace());
                    fileObj.put("dateModified", sdf.format(file.lastModified()));
                    list.put(fileObj);
                }
            }
            stateObj.put("files", list);
            try(FileWriter file = new FileWriter(System.getProperty("user.dir")+"/state.json"))
            {
                file.write(stateObj.toString());
                file.flush();
            }
            catch (IOException e)
            {
                log("Cannot create state file.");
                logError(e);
                System.exit(1);
            }
            log("Directory scanned and state file created");
        }

        //log("Looking for files in " + System.getProperty("user.dir"));

    }

    private static void log(String msg)
    {
        if (pwLog == null)
        {
            try
            {
                pwLog = new PrintWriter("core-server.log", "UTF-8");
            }
            catch (FileNotFoundException e)
            {
                logError(e);
                System.exit(1);
            }
            catch (UnsupportedEncodingException e)
            {
                logError(e);
                System.exit(1);
            }
        }
        System.out.println(msg);
        pwLog.println(msg);
        pwLog.flush();
        //TODO close pwLog somewhere
    }

    private static void logError(Exception e)
    {
        if (pwLog == null)
        {
            try
            {
                pwLog = new PrintWriter("core-server.log", "UTF-8");
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        e.printStackTrace(pwLog);
        pwLog.flush();
        //TODO close pwLog somewhere
    }
}
