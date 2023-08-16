module com.example.pipeburstclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;


    opens com.example.pipeburstclient to javafx.fxml;
    exports com.example.pipeburstclient;
}