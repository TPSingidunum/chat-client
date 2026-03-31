module ac.rs.singidunum.chatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.jooq;
    requires io.github.willena.sqlitejdbc;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;

    opens ac.rs.singidunum.chatclient to javafx.fxml;
    exports ac.rs.singidunum.chatclient;
    exports ac.rs.singidunum.chatclient.controllers;
    opens ac.rs.singidunum.chatclient.controllers to javafx.fxml;
}