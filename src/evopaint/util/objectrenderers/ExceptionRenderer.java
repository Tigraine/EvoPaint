package evopaint.util.objectrenderers;

import evopaint.interfaces.IObjectRenderer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 22.01.2010
 * Time: 00:51:51
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionRenderer implements IObjectRenderer {
    public String render(Object object) {
        Exception ex = (Exception)object;
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);

        return writer.getBuffer().toString();
    }
}
