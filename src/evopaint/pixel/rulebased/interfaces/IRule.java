/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.Condition;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tam
 */
public interface IRule extends IHTML, Serializable, ICopyable {

    public Action getAction();
    public void setAction(Action action);
    public List<Condition> getConditions();
    public void setConditions(List<Condition> conditions);
    public boolean apply(Pixel pixel, Configuration configuration);
}
