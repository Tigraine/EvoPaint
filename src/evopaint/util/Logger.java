package evopaint.util;

import evopaint.Entity;
import evopaint.Relation;
import evopaint.util.objectrenderers.EntityRenderer;
import evopaint.util.objectrenderers.ExceptionRenderer;
import evopaint.util.objectrenderers.RelationRenderer;
import evopaint.util.objectrenderers.VerboseEntityRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 13:46:40
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    public static final int logLevel = LogLevel.ERROR;
    public static final int logVerbosity = Log.Verbosity.VERBOSE;
    public static Log log = new ConsoleLog(logLevel);

    public static void init(){
        initLogger(logLevel, logVerbosity);
    }

    private static void initLogger(int logLevel, int logVerbosity) {
        log = new ConsoleLog(logLevel);
        if (logVerbosity == Log.Verbosity.VERBOSEVERBOSE) {
            log.addRenderer(Entity.class, new VerboseEntityRenderer());
        }
        else {
            log.addRenderer(Entity.class, new EntityRenderer());
        }
        log.addRenderer(Relation.class, new RelationRenderer());
        log.addRenderer(Exception.class, new ExceptionRenderer());
    }
}