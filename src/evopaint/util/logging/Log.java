package evopaint.util.logging;

import evopaint.interfaces.IObjectRenderer;

import java.util.HashMap;
import java.util.Hashtable;

public abstract class Log {
    private final int logLevel;

    public Log(int logLevel) {
        this.logLevel = logLevel;
    }

    public class Verbosity {
        public static final int BRIEF = 0, VERBOSE = 1, VERBOSEVERBOSE = 2;
    }

    public void information(String message, Object... parameters) {
        if (logLevel < LogLevel.INFORMATION) return; 
        String text = createLogText(message, parameters);
        writeInformation(text);
    }

    public void warning(String message, Object... parameters) {
        if (logLevel < LogLevel.WARNING) return;
        String text = createLogText(message, parameters);
        writeWarning(text);
    }

    public void error(String message, Object... parameters) {
        if (logLevel < LogLevel.ERROR) return;
        String text = createLogText(message, parameters);
        writeError(text);
    }

    public void debug(String message, Object... parameters) {
        if (logLevel >= LogLevel.DEBUG);
        String text = createLogText(message, parameters);
        writeDebug(text);
    }

    private String createLogText(String message, Object... parameters) {
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

    private HashMap<Class<? extends Object>, IObjectRenderer> renderers = new HashMap<Class<? extends Object>, IObjectRenderer>();
    private DefaultRenderer defaultRenderer = new DefaultRenderer();
    private IObjectRenderer FindSuitableRenderer(Class<? extends Object> target) {
        if (renderers.containsKey(target))
            return renderers.get(target);

        Class<? extends Object> superclass = target.getSuperclass();
        if (superclass == null) return defaultRenderer;

        //Recursive finding of higher renderer
        if (renderers.containsKey(superclass)) {
            IObjectRenderer renderer = renderers.get(superclass);
            renderers.put(target, renderer);
        }
        return FindSuitableRenderer(superclass);
    }

    private String WriteObject(Object message) {
        IObjectRenderer iObjectRenderer = FindSuitableRenderer(message.getClass());
        return iObjectRenderer.render(message);
    }

    public void addRenderer(Class target, IObjectRenderer renderer) {
        renderers.put(target, renderer);
    }
    public void clearRenderers() {
        renderers.clear();
    }

    private class DefaultRenderer implements IObjectRenderer {
        public String render(Object object) {
            return object.toString();
        }
    }
}
