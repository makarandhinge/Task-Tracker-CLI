package org.example;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static String currentTime(){
        return dateFormat.format(new Date());
    }

    private static boolean argumentValidaton(String[] args, int required, String usage){
        if(args.length < required){
            System.out.println(usage);
        }else{
            return true;
        }
        return false;
    }

    static void executeCommand(String[] args, int requiredArgs, String usage, Runnable action){
        if(argumentValidaton(args, requiredArgs, usage)){
            action.run();
        }
    }

    interface ThrowingRunnable {
        void run() throws IOException;
    }

    static void safeExecute(ThrowingRunnable action){
        try{
            action.run();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
