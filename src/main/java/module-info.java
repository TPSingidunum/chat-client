module com.masofino.birp.chatclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.masofino.birp.chatclient to javafx.fxml;
    exports com.masofino.birp.chatclient;
}