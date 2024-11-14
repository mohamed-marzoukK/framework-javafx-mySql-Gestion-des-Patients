module com.hopital.miniprojet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.pdfbox;

    opens com.hopital.miniprojet.data to javafx.base;

    opens com.hopital.miniprojet to javafx.fxml;
    exports com.hopital.miniprojet;
}