/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.pixel.rulebased.interfaces;

import evopaint.Configuration;
import evopaint.pixel.Pixel;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tam
 */
public interface IRule extends IHTML, Serializable, ICopyable {

    public IAction getAction();
    public void setAction(IAction action);
    public List<ICondition> getConditions();
    public void setConditions(List<ICondition> conditions);
    public boolean apply(Pixel pixel, Configuration configuration);
}
