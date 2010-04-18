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

package evopaint.pixel.rulebased.targeting;

import evopaint.pixel.rulebased.interfaces.IHTML;
import evopaint.util.ExceptionHandler;
import evopaint.util.mapping.RelativeCoordinate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class MetaTarget implements ITarget, IHTML {
    protected List<RelativeCoordinate> directions;

    public MetaTarget(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    public MetaTarget() {
        this.directions = new ArrayList<RelativeCoordinate>();
    }

    public List<RelativeCoordinate> getDirections() {
        return directions;
    }

    public void setDirections(List<RelativeCoordinate> directions) {
        this.directions = directions;
    }

    public MetaTarget getCopy() {
        MetaTarget newTarget = null;
        try {
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outByteStream);
            out.writeObject(this);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
            newTarget = (MetaTarget) in.readObject();
        } catch (ClassNotFoundException ex) {
            ExceptionHandler.handle(ex, true);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex, true);
        }
        return newTarget;
    }

    @Override
    public String toString() {
        if (directions.size() == 0) {
            return "<no targets>";
        }
        if (directions.size() == 8 && false == directions.contains(RelativeCoordinate.CENTER)) {
            return "my neighbors";
        }
        if (directions.size() == 9) {
            return "us";
        }
        String ret = new String();
        ret += "[";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }

    public String toHTML() {
        if (directions.size() == 0) {
            return "&lt;no targets&gt;";
        }
        if (directions.size() == 8 && false == directions.contains(RelativeCoordinate.CENTER)) {
            return "my neighbors";
        }
        if (directions.size() == 9) {
            return "us";
        }
        String ret = new String();
        ret += "[";
        for (Iterator<RelativeCoordinate> ii = directions.iterator(); ii.hasNext();) {
            ret += ii.next().toString();
            if (ii.hasNext()) {
                ret += ", ";
            }
        }
        ret += "]";
        return ret;
    }
    
}
