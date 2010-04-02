/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.INamed;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tam
 */
public class CollectionNode extends DefaultMutableTreeNode implements INamed {

    public CollectionNode(RuleSetCollection collection) {
        super(collection, true);
    }

    public String getName() {
        return ((INamed)getUserObject()).getName();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
