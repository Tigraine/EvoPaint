package evopaint.gui;

import evopaint.Selection;

public interface SelectionManager {
    public Selection getActiveSelection();
    void clearSelections();
    void setActiveSelection(Selection selection);
    void removeActiveSelection();
}
