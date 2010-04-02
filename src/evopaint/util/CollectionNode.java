/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.pixel.rulebased.RuleSetCollection;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tam
 */
public class CollectionNode extends DefaultMutableTreeNode {

    public CollectionNode(RuleSetCollection collection) {
        super(collection, true);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
