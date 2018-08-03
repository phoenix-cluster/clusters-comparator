package cn.edu.cqupt.util;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangjs on 2018/4/9.
 */
public class TabPaneExpansion {
    private TabPane tabPane;
    private HashMap<String, Tab> tabsMap;

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public TabPaneExpansion() {
        this.tabPane = new TabPane();
        this.tabsMap = new HashMap<>();
        initial();
    }

    public TabPaneExpansion(TabPane tabPane) {
        this.tabPane = tabPane;
        this.tabsMap = new HashMap<>();
        initial();
    }

    private void initial() {
        tabPane.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            while (c.next()) {

                // if elements were added into list, the elements's text
                // and the elements themselves need to be added into HashMap
                if (c.wasAdded()) {
                    List<? extends Tab> addedTabs = c.getAddedSubList();
                    for (Tab tab : addedTabs) {
                        tabsMap.put(tab.getText(), tab);
                    }
                }

                // if elements were removed from list, the elements's text
                // and the elements themselves need to be removed from HashMap
                if (c.wasRemoved()) {
                    List<? extends Tab> removedTabs = c.getRemoved();
                    for (Tab tab : removedTabs) {
                        tabsMap.remove(tab.getText());
                    }
                }
            }


        });
    }

    public boolean addTab(Tab tab) {
        return tabPane.getTabs().add(tab);
    }

    public boolean addTabs(Tab... tabs) {
        return this.tabPane.getTabs().addAll(tabs);
    }

    public Tab getTabByText(String text) {
        return tabsMap.get(text);
    }

    public boolean removeTab(String text) {
        return this.tabPane.getTabs().remove(getTabByText(text));
    }
}