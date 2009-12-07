package evopaint.util;

import evopaint.Config;
import evopaint.Relation;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.11.2009
 * Time: 19:50:27
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLog extends Log {

    public void information(String message) {
        if (Config.logLevel >= Log.Level.INFORMATION) {
            writeToConsole("[INFO] " + message);
        }
    }

    public void warning(String message) {
        if (Config.logLevel >= Log.Level.WARNING) {
            writeToConsole("[WARN] " + message);
        }
    }

    public void error(String message) {
        if (Config.logLevel >= Log.Level.ERROR) {
            System.err.println("[ERR]  " + message); // XXX needs unbuffered stream
        }
    }
    
    private void writeToConsole(String message)
    {
        System.out.println(message);
    }
}
