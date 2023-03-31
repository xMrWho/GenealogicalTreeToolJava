module whocoding.familytree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens whocoding.familytree to javafx.fxml;

    exports whocoding.familytree;
    exports whocoding.familytree.classes;
    exports whocoding.familytree.sql;
    exports whocoding.familytree.gui.controller;
    exports whocoding.familytree.gui.dialogs;
    exports whocoding.familytree.methods;
}
