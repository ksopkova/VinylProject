package com.example.hellofx.vinyl.Model;

import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;

import javafx.application.Platform;

import java.util.Random;

public class RandomUserSimulator implements Runnable {

    private final MainScreenViewModel viewModel;
    private final Random random = new Random();

    public RandomUserSimulator(MainScreenViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void run() {

        while (true) {

            try {
                Thread.sleep(2000 + random.nextInt(3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {

                if (viewModel.getVinyls().isEmpty()) return;

                Vinyl vinyl = viewModel.getVinyls()
                        .get(random.nextInt(viewModel.getVinyls().size()));

                int action = random.nextInt(4);

                switch (action) {
                    case 0 -> viewModel.reserve(vinyl);
                    case 1 -> viewModel.borrow(vinyl);
                    case 2 -> viewModel.returnVinyl(vinyl);
                    case 3 -> viewModel.remove(vinyl);
                }
            });
        }

    }


}