package com.example.hellofx.vinyl.View;

import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ActivityLogView {
    @FXML private ListView<String> activityList;

    public void bind(MainScreenViewModel viewModel) {
        activityList.setItems(viewModel.getActivityMessages());
    }
}
