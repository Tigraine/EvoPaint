package evopaint.util;

import evopaint.interfaces.IObjectRenderer;

import java.util.Hashtable;

public abstract class Log {
    private final int logLevel;

    public Log(int logLevel) {
        this.logLevel = logLevel;
    }

    public class Verbosity {
        public static final int BRIEF = 0, VERBOSE = 1, VERBOSEVERBOSE = 2;
    }

    public class Format {
        public static final int COMPACT = 0, LONG = 1;
    }

    public void information(String message, Object parameter) {
        information(message, new Object[]{parameter});
    }
    public void information(String message, Object[] parameters) {
        if (logLevel >= LogLevel.INFORMATION) {
            String text = createLogText(message, parameters);
            writeInformation(text);
        }
    }

    public void warning(String message, Object parameter) {
        warning(message, new Object[]{parameter});
    }
    public void warning(String message, Object[] parameters) {
        if (logLevel >= LogLevel.WARNING) {
            String text = createLogText(message, parameters);
            writeWarning(text);
        }
    }

    public void error(String message, Object parameter) {
        error(message, new Object[]{parameter});
    }
    public void error(String message, Object[] parameters) {
        if (logLevel >= LogLevel.ERROR) {
            String text = createLogText(message, parameters);
            writeError(text);
        }
    }

    public void debug(String message, Object parameter) {
        debug(message, new Object[]{parameter});
    }
    public void debug(String message, Object[] parameters) {
        if (logLevel >= LogLevel.DEBUG) {
            String text = createLogText(message, parameters);
            writeDebug(text);
        }
    }

    private String createLogText(String message, Object[] parameters) {
        String[] params = new String[parameters.length];
        int i = 0;
        for(Object o : parameters) {
            params[i] = WriteObject(o);
            i++;
        }
        return String.format(message, params);
    }

    protected abstract void writeInformation(String message);

    protected abstract void writeWarning(String message);

    protected abstract void writeError(String message);

    protected abstract void writeDebug(String message);

    private Hashtable<Class, IObjectRenderer> renderers = new Hashtable<Class, IObjectRenderer>();

    private IObjectRenderer FindSuitableRenderer(Object message) {
        Class<? extends Object> aClass = message.getClass();
        if (renderers.contains(aClass))
            return renderers.get(aClass);
        return new DefaultRenderer();
    }

    private String WriteObject(Object message) {
        IObjectRenderer iObjectRenderer = FindSuitableRenderer(message);
        return iObjectRenderer.render(message);
    }

    public void addRenderer(IObjectRenderer renderer, Class target) {
        renderers.put(target, renderer);
    }

    private class DefaultRenderer implements IObjectRenderer {
        public String render(Object object) {
            return object.toString();
        }
    }
}
