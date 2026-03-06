package com.example.hellofx.vinyl.View;

import com.example.hellofx.vinyl.Model.AvailableState;
import com.example.hellofx.vinyl.Model.Library;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainScreenView {

    @FXML
    private TableView<Vinyl> vinylTable;

    @FXML
    private TableColumn<Vinyl, String> titleColumn;

    @FXML
    private TableColumn<Vinyl, String> artistColumn;

    @FXML
    private TableColumn<Vinyl, Number> yearColumn;

    @FXML
    private TableColumn<Vinyl, String> stateColumn;

    private MainScreenViewModel viewModel;

    public void initialize() {

        // vytvoríme model + viewmodel

        Library library = new Library();
        viewModel = new MainScreenViewModel(library);

        // for testing purposes we add random data
        viewModel.addVinyl(new Vinyl("Gatkove hity", "Gatko Gatkovic", 2026));
        viewModel.addVinyl(new Vinyl("Waka waka", "Shakira",2010));

        // nastavenie columns
        titleColumn.setCellValueFactory(data -> data.getValue().getTitleProperty());
        artistColumn.setCellValueFactory(data -> data.getValue().getArtistProperty());
        yearColumn.setCellValueFactory(data -> data.getValue().getYearProperty());
        // zobrazenie názvu stavu
        stateColumn.setCellValueFactory(data -> data.getValue().getStateName());

        vinylTable.setItems(viewModel.getVinyls());
    }

    @FXML
    private void onReserve() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.reserve(selected);
        vinylTable.refresh();
    }

    @FXML
    private void onBorrow() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.borrow(selected);
        vinylTable.refresh();
    }

    @FXML
    private void onReturn() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.returnVinyl(selected);
        vinylTable.refresh();
    }

    @FXML
    private void onRemove() {
        Vinyl selected = vinylTable.getSelectionModel().getSelectedItem();
        viewModel.remove(selected);
        vinylTable.refresh();
    }

}