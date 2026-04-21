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
    requires spring.websocket;
    requires spring.messaging;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    opens ac.rs.singidunum.chatclient to javafx.fxml;
    exports ac.rs.singidunum.chatclient;
    exports ac.rs.singidunum.chatclient.controllers;
    exports ac.rs.singidunum.chatclient.messaging.dtos;
    opens ac.rs.singidunum.chatclient.controllers to javafx.fxml;
    opens ac.rs.singidunum.chatclient.messaging.dtos to com.fasterxml.jackson.databind;
}