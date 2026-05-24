package com.example.hellofx.vinyl.View;

import Simulation.RandomUserSimulator;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainScreenView {

    @FXML private TableView<Vinyl> vinylTable;

    @FXML private TableColumn<Vinyl, String> titleColumn;
    @FXML private TableColumn<Vinyl, String> artistColumn;
    @FXML private TableColumn<Vinyl, Number> yearColumn;
    @FXML private TableColumn<Vinyl, String> stateColumn;

    @FXML private TableColumn<Vinyl, String> reservedByColumn;
    @FXML private TableColumn<Vinyl, String> borrowedByColumn;
    @FXML private Button startSimulationButton;
    @FXML private Label statusLabel;
    @FXML private ActivityLogView activityLogController;

    private MainScreenViewModel viewModel;
    private boolean simulationStarted;
    private Thread simulatorThread;

    public void initialize() {
        viewModel = new MainScreenViewModel();

        // base columns
        titleColumn.setCellValueFactory(data -> data.getValue().getTitleProperty());
        artistColumn.setCellValueFactory(data -> data.getValue().getArtistProperty());
        yearColumn.setCellValueFactory(data -> data.getValue().getYearProperty());
        stateColumn.setCellValueFactory(data -> data.getValue().stateNameProperty());

        // user columns (auto-updating)
        reservedByColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(
                        () -> cellData.getValue().getReservedBy() == null
                                ? ""
                                : cellData.getValue().getReservedBy().getUserName(),
                        cellData.getValue().reservedByProperty()
                )
        );

        borrowedByColumn.setCellValueFactory(cellData ->
                Bindings.createStringBinding(
                        () -> cellData.getValue().getBorrowedBy() == null
                                ? ""
                                : cellData.getValue().getBorrowedBy().getUserName(),
                        cellData.getValue().borrowedByProperty()
                )
        );

        // connecting table to the ViewModel list
        vinylTable.setItems(viewModel.getVinyls());
        statusLabel.textProperty().bind(viewModel.statusProperty());
        activityLogController.bind(viewModel);
    }

    @FXML
    private void onReserve() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.reserve(selected);
    }

    @FXML
    private void onBorrow() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.borrow(selected);
    }

    @FXML
    private void onReturn() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.returnVinyl(selected);
    }

    @FXML
    private void onRemove() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.remove(selected);
    }

    @FXML
    private void onStartSimulation() {
        if (simulationStarted) {
            viewModel.setStatus("Simulation is already running.");
            return;
        }

        simulationStarted = true;
        startSimulationButton.setDisable(true);
        simulatorThread = new Thread(new RandomUserSimulator(viewModel));
        simulatorThread.setDaemon(true);
        simulatorThread.start();
        viewModel.setStatus("Simulation started.");
    }
}
