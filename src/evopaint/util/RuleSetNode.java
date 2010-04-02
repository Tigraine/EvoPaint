/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tam
 */
public class RuleSetNode extends DefaultMutableTreeNode {

        public RuleSetNode(Object userObject) {
            super(userObject, false);
        }

        @Override
        public boolean isLeaf() {
            return true;
        }
}
