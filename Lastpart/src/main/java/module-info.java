module com.lastpart.lastpart {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
   // requires javafx.fxml;
    //requires controlsfx;


    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.lastpart.lastpart to javafx.fxml;
    //opens server to javafx.graphics, javafx.fxml;
    //opens client to javafx.graphics, javafx.fxml;
   // opens Datahouse to javafx.base;
    exports com.lastpart.lastpart;
}