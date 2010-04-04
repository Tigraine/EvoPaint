package evopaint.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import evopaint.Selection;

public class SelectionToolBox extends JPanel implements Observer {

	private final Showcase showcase;

	public SelectionToolBox(Showcase showcase) {
		this.showcase = showcase;
		showcase.getCurrentSelections().addObserver(this);
		initializeComponents();
	}
	
	private void initializeComponents(){
		this.setLayout(new GridLayout(0, 1));
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		SelectionList.SelectionListUpdateArgs updateEvent = (SelectionList.SelectionListUpdateArgs) arg1;
        if (updateEvent.getChangeType() == SelectionList.ChangeType.LIST_CLEARED) {
            this.removeAll();
        }
        if (updateEvent.getChangeType() == SelectionList.ChangeType.ITEM_ADDED) {
            this.add(new SelectionWrapper(updateEvent.getSelection(), showcase));
        }
        if (updateEvent.getChangeType() == SelectionList.ChangeType.ITEM_DELETED) {
            for(int i = 0; i < this.getComponentCount() ; i++) {
                SelectionWrapper wrapper = (SelectionWrapper)this.getComponent(i);
                if (wrapper.getSelection() == updateEvent.getSelection()) {
                    this.remove(i);
                    break;
                }
            }
        }
        
        this.revalidate();
	}
	
	private class SelectionWrapper extends JPanel implements Observer, MouseListener {

		private final Color backColor;
		private final Showcase showcase2;
		
		private JLabel selectionName;

		private final Selection selection;

		public SelectionWrapper(Selection selection, Showcase showcase){
			this.selection = selection;
			selection.addObserver(this);
			showcase2 = showcase;
			
			
			selectionName = new JLabel(selection.getSelectionName());
			this.add(selectionName);
			this.addMouseListener(this);
			
			backColor = this.getBackground();
		}
		
		@Override
		public void update(Observable o, Object arg) {
			Selection selection = (Selection) o;
            UpdateName(selection);			
		}

		private void UpdateName(Selection selection) {
			selectionName.setText(selection.getSelectionName());
		}

		public Selection getSelection() {
			return selection;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			this.selection.setHighlighted(true);
			this.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			this.selection.setHighlighted(false);
			this.setBackground(backColor);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			showcase2.setActiveSelection(selection);
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
