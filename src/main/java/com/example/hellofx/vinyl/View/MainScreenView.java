package com.example.hellofx.vinyl.View;

import Simulation.RandomUserSimulator;
import com.example.hellofx.vinyl.Model.Library;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
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

    private MainScreenViewModel viewModel;

    public void initialize() {
        Library library = new Library();
        viewModel = new MainScreenViewModel(library);

        // test data
// test data
        viewModel.addVinyl(new Vinyl("IGOR", "Tyler, The Creator", 2019));
        viewModel.addVinyl(new Vinyl("My Beautiful Dark Twisted Fantasy", "Kanye West", 2010));
        viewModel.addVinyl(new Vinyl("good kid, m.A.A.d city", "Kendrick Lamar", 2012));
        viewModel.addVinyl(new Vinyl("Madvillainy", "Madvillain", 2004));
        viewModel.addVinyl(new Vinyl("The Miseducation of Lauryn Hill", "Lauryn Hill", 1998));

        viewModel.addVinyl(new Vinyl("Mezzanine", "Massive Attack", 1998));
        viewModel.addVinyl(new Vinyl("Dummy", "Portishead", 1994));
        viewModel.addVinyl(new Vinyl("Selected Ambient Works 85–92", "Aphex Twin", 1992));
        viewModel.addVinyl(new Vinyl("Discovery", "Daft Punk", 2001));
        viewModel.addVinyl(new Vinyl("Untrue", "Burial", 2007));
        viewModel.addVinyl(new Vinyl("Since I Left You", "The Avalanches", 2000));

        viewModel.addVinyl(new Vinyl("Unknown Pleasures", "Joy Division", 1979));
        viewModel.addVinyl(new Vinyl("London Calling", "The Clash", 1979));
        viewModel.addVinyl(new Vinyl("The Queen Is Dead", "The Smiths", 1986));
        viewModel.addVinyl(new Vinyl("Rumours", "Fleetwood Mac", 1977));
        viewModel.addVinyl(new Vinyl("Hounds of Love", "Kate Bush", 1985));

        viewModel.addVinyl(new Vinyl("Blue Train", "John Coltrane", 1957));
        viewModel.addVinyl(new Vinyl("Kind of Blue", "Miles Davis", 1959));
        viewModel.addVinyl(new Vinyl("Time Out", "The Dave Brubeck Quartet", 1959));

        viewModel.addVinyl(new Vinyl("Currents", "Tame Impala", 2015));
        viewModel.addVinyl(new Vinyl("Melodrama", "Lorde", 2017));
        viewModel.addVinyl(new Vinyl("Punisher", "Phoebe Bridgers", 2020));

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

        // CRITICAL: connect table to the ViewModel list
        vinylTable.setItems(viewModel.getVinyls());

        // simulation thread
        Thread simulator = new Thread(new RandomUserSimulator(viewModel));
        simulator.setDaemon(true);
        simulator.start();
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
}