/*******************************************************************************
 *
 * VUT FIT Brno - IJA project BlockDiagram
 *
 * Copyright (C) 2018, Adam Petras (xpetra19)
 * Copyright (C) 2018, Erik Pitko (xpitko00)
 * 
 * Contributors: 
 * 		Adam Petras - GUI, base application implementation, tests
 * 		Erik Pitko - base application implementation, Doxygen doc, tests, save/load scene
 * 
 ******************************************************************************/
package Graphics;

import javafx.scene.layout.AnchorPane;

/**
 * The Interface DrawableObject.
 */
public interface DrawableObject {
    
    /**
     * Draw object on AnchorPane
     *
     * @param pane the pane
     */
    void Draw(AnchorPane pane);
}