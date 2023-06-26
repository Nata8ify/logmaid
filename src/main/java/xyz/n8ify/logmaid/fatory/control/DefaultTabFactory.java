package xyz.n8ify.logmaid.fatory.control;

import javafx.scene.Node;
import javafx.scene.control.Tab;

public class DefaultTabFactory {

    public static Tab newInstance(String title, Node child) {
        Tab tab = new Tab(title, child);
        tab.setClosable(false);
        return tab;
    }

}
