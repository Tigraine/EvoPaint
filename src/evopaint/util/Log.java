package evopaint.util;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.11.2009
 * Time: 19:49:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class Log {
    public class Level {
        public static final int NOTHING=0, ERROR=1, WARNING=2, INFORMATION=3;
    }
    public class Verbosity {
         public static final int BRIEF=0, VERBOSE=1, VERBOSEVERBOSE=2;
    }
    public class Format {
         public static final int COMPACT=0, LONG=1;
    }

    public abstract void information(String message);
    public abstract void warning(String message);
    public abstract void error(String message);
}
