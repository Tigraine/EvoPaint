/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.pixel.rulebased.interfaces.INamed;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tam
 */
public class RuleSetNode extends DefaultMutableTreeNode {

        public RuleSetNode(Object userObject) {
            super(userObject, false);
        }

        public String getName() {
            return ((INamed)getUserObject()).getName();
        }

        @Override
        public boolean isLeaf() {
            return true;
        }
}
