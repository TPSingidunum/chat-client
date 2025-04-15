module com.masofino.birp.chatclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires com.google.gson;
    opens com.masofino.birp.chatclient.api to com.google.gson;
    requires java.net.http;

    opens com.masofino.birp.chatclient to javafx.fxml;
    exports com.masofino.birp.chatclient;
    exports com.masofino.birp.chatclient.config;
    opens com.masofino.birp.chatclient.config to javafx.fxml;
    exports com.masofino.birp.chatclient.controllers;
    opens com.masofino.birp.chatclient.controllers to javafx.fxml;
}