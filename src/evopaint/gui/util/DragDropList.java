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

package evopaint.gui.util;

/**
 *
 * @author tam
 */
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

public class DragDropList extends JList {

    DefaultListModel model;

    public DragDropList(ListModel model) {
        super(model);
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);

        setTransferHandler(new MyListDropHandler(this));

        new MyDragListener(this);
    }

    private class MyDragListener implements DragSourceListener, DragGestureListener {

        DragDropList list;
        DragSource ds = new DragSource();

        public MyDragListener(DragDropList list) {
            this.list = list;
            DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(list,
                    DnDConstants.ACTION_MOVE, this);

        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            StringSelection transferable = new StringSelection(Integer.toString(list.getSelectedIndex()));
            ds.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }

    private class MyListDropHandler extends TransferHandler {

        DragDropList list;

        public MyListDropHandler(DragDropList list) {
            this.list = list;
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            if (dl.getIndex() == -1) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            String indexString;
            try {
                indexString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            // get from index, insert at droptargetindex
            int index = Integer.parseInt(indexString);
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int dropTargetIndex = dl.getIndex();

            DefaultListModel model = (DefaultListModel)list.getModel();
            Object element = model.getElementAt(index);
            model.remove(index);
            if (index < dropTargetIndex) {
                dropTargetIndex--;
            }
            model.add(dropTargetIndex, element);
            setSelectedIndex(dropTargetIndex);
            
            return true;
        }
    }
}
