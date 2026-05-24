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
    exports com.example.hellofx.vinyl.Model.State;
    opens com.example.hellofx.vinyl.Model.State to javafx.fxml;
    exports com.example.hellofx.vinyl.Model.Observer;
    opens com.example.hellofx.vinyl.Model.Observer to javafx.fxml;
    exports com.example.hellofx.vinyl.client;
    exports com.example.hellofx.vinyl.network.protocol;
    exports com.example.hellofx.vinyl.server;
    exports com.example.hellofx.vinyl.server.log;
    exports com.example.hellofx.vinyl.server.strategy;
    exports Simulation;
    opens Simulation to javafx.fxml;
}
