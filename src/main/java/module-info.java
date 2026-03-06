module com.example.hellofx.vinyl{
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hellofx.vinyl to javafx.fxml;
    exports com.example.hellofx.vinyl;
    exports com.example.hellofx.vinyl.Model;
    opens com.example.hellofx.vinyl.Model to javafx.fxml;
    exports com.example.hellofx.vinyl.View;
    opens com.example.hellofx.vinyl.View to javafx.fxml;
    exports com.example.hellofx.vinyl.ViewModel;
    opens com.example.hellofx.vinyl.ViewModel to javafx.fxml;
}