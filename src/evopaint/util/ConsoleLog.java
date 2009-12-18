package evopaint.util;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.11.2009
 * Time: 19:50:27
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLog extends Log {

    public ConsoleLog(int logLevel) {
        super(logLevel);
    }

    @Override
    protected void writeInformation(String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    protected void writeWarning(String message) {
        System.out.println("[WARN] " + message);
    }

    @Override
    protected void writeError(String message) {
        System.err.println("[ERR]  " + message); // XXX needs unbuffered stream
    }

    @Override
    protected void writeDebug(String message) {
        System.out.println("[DBG]  " + message);
    }
}
