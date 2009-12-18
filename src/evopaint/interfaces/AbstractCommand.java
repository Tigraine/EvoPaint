package evopaint.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractCommand implements ICommand, ActionListener {
    public void actionPerformed(ActionEvent actionEvent) {
        this.execute();
    }
}
