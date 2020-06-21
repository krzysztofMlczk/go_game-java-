module PlayerApplication{
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    exports com.tearsvalley;
    opens com.tearsvalley to javafx.fxml; 

    exports com.tearsvalley.controllers;
    opens com.tearsvalley.controllers to javafx.fxml;

    exports com.tearsvalley.data;
    exports com.tearsvalley.client;
}