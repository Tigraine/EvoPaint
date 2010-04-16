/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint;

import evopaint.gui.util.ColorIcon;
import evopaint.gui.util.FairyDustIcon;
import evopaint.interfaces.IPaintChanger;
import evopaint.interfaces.IPaintChangeListener;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Represents the paint our paint brush or fill tool can use to paint on the
 * canvas. Paint in evopaint consists of a color and a rule set. This class has
 * multiple purposes. For once it is the paint managing unit, residing in the
 * configuration, second it can be initialized to be a concrete Paint object
 * to paint with and third it contains the paint history including its
 * graphical representation. Classes who are interested in changes of paint
 * should implement the <code>IPaintChangeListener</code> interface and
 * subscribe to the managing unit of this class residing in the configuration
 * to be noticed whenever the current paint changes.
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Paint implements IPaintChanger {
    public static final int COLOR = 0;
    public static final int FAIRY_DUST = 1;
    public static final int EXISTING_COLOR = 2;

    public static final int RULE_SET = 0;
    public static final int NO_RULE_SET = 1;
    public static final int EXISTING_RULE_SET = 2;

    private Configuration configuration;
    private LinkedList<Paint> paintHistory;
    private Paint currentPaint;
    private JPopupMenu paintHistoryMenu;
    private List<IPaintChangeListener> changeListeners;

    private int colorMode;
    private int ruleSetMode;
    private PixelColor color;
    private RuleSet ruleSet;

    public Paint(Configuration configuration) {
        this.configuration = configuration;
        this.paintHistory = new LinkedList<Paint>();
        this.paintHistoryMenu = new JPopupMenu();
        this.changeListeners = new ArrayList<IPaintChangeListener>();
        this.currentPaint = new Paint(configuration, Paint.COLOR, Paint.NO_RULE_SET, new PixelColor(0xFF0000), null);
    }

    private Paint(Configuration configuration, int colorMode, int ruleSetMode, PixelColor color, RuleSet ruleSet) {
        this.configuration = configuration;
        this.colorMode = colorMode;
        this.ruleSetMode = ruleSetMode;
        this.color = color;
        this.ruleSet = ruleSet;
    }

    public void showHistory(Component invoker, Point location) {
        if (paintHistory.size() < 1) {
                 return;
        }
        paintHistoryMenu.removeAll();
        for (final Paint paint : paintHistory) {
            JMenuItem menuItem = paint.toMenuItem();
            menuItem.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                        changePaint(paint);
                        rememberCurrent();
                    }
            });
            paintHistoryMenu.add(menuItem);
        }
        paintHistoryMenu.show(invoker, location.x, location.y);
    }

    public PixelColor getCurrentColor() {
        return currentPaint.color;
    }

    public int getCurrentColorMode() {
        return currentPaint.colorMode;
    }

    public int getCurrentRuleSetMode() {
        return currentPaint.ruleSetMode;
    }

    public RuleSet getCurrentRuleSet() {
        return currentPaint.ruleSet;
    }

    public void changeCurrentColor(PixelColor color) {
        changePaint(new Paint(configuration, Paint.COLOR,
                currentPaint.ruleSetMode, color, currentPaint.ruleSet));
    }

    public void changeCurrentColorMode(int colorMode) {
        changePaint(new Paint(configuration, colorMode, currentPaint.ruleSetMode,
                currentPaint.color, currentPaint.ruleSet));
    }

    public void changeCurrentRuleSet(RuleSet ruleSet) {
        changePaint(new Paint(configuration, currentPaint.colorMode,
                Paint.RULE_SET, currentPaint.color, ruleSet));
    }

    public void changeCurrentRuleSetMode(int ruleSetMode) {
        changePaint(new Paint(configuration, currentPaint.colorMode,
                ruleSetMode, currentPaint.color, currentPaint.ruleSet));
    }

    public void rememberCurrent() {
        // reset position in history if history contains paint
        if (paintHistory.size() > 1 &&
                false == paintHistory.getFirst().equals(currentPaint) &&
                paintHistory.contains(currentPaint)) {
            //System.out.println("contained");
            paintHistory.remove(currentPaint);
            paintHistory.addFirst(currentPaint);
        }
        // else add it as top most element and remove the last if we have too many
        else if (false == (colorMode == Paint.EXISTING_COLOR && ruleSet == null) &&
                false == paintHistory.contains(currentPaint)) {
            //System.out.println("new");
            paintHistory.addFirst(currentPaint);
            if (paintHistory.size() > configuration.paintHistorySize) {
                paintHistory.removeLast();
            }
        }
    }

    public void changePaint(int colorMode, int ruleSetMode, PixelColor color, RuleSet ruleSet) {
        assert(configuration != null);
        changePaint(new Paint(configuration, colorMode, ruleSetMode, color, ruleSet));
    }

    private void changePaint(Paint newPaint) {
        currentPaint = newPaint;
        for (IPaintChangeListener changeObserver : changeListeners) {
            changeObserver.paintChanged();
        }
    }

    public void subscribe(IPaintChangeListener subscriber) {
        if (this.changeListeners.contains(subscriber)) {
            return;
        }
        this.changeListeners.add(subscriber);
    }

    public void unsubsribe(IPaintChangeListener subscriber) {
         this.changeListeners.remove(subscriber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paint other = (Paint) obj;
        if (this.colorMode != other.colorMode) {
            return false;
        }
        if (this.ruleSetMode != other.ruleSetMode) {
            return false;
        }
        if (this.color != other.color && (this.color == null || !this.color.equals(other.color))) {
            return false;
        }
        if (this.ruleSet != other.ruleSet && (this.ruleSet == null) || this.ruleSet != other.ruleSet) { //  we want object comparison only... || !this.ruleSet.equals(other.ruleSet)
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.colorMode;
        hash = 97 * hash + this.ruleSetMode;
        hash = 97 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 97 * hash + (this.ruleSet != null ? this.ruleSet.hashCode() : 0);
        return hash;
    }

    private JMenuItem toMenuItem() {
        Icon icon = null;
        switch (colorMode) {
            case COLOR: icon = new ColorIcon(16, 16, new Color(color.getInteger()));
            break;
            case FAIRY_DUST: icon = new FairyDustIcon(configuration, 16, 16);
            break;
            case EXISTING_COLOR: icon = null;
            break;
            default: assert(false);
        }
        String ruleSetName = null;
        switch (ruleSetMode) {
            case RULE_SET: ruleSetName = ruleSet.getName();
            break;
            case NO_RULE_SET:
            break;
            case EXISTING_RULE_SET: ruleSetName = "<use existing>";
            break;
            default: assert(false);
        }
        JMenuItem ret = new JMenuItem(ruleSetName, icon);
        return ret;
    }
    
}
