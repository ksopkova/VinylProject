package Simulation;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.ViewModel.MainScreenViewModel;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;

public class RandomUserSimulator implements Runnable {

    private final MainScreenViewModel viewModel;
    private final Random random = new Random();

    // NEW: multiple simulated users
    private final List<User> simUsers = List.of(
            new User("sim-1", "Kiko"),
            new User("sim-2", "Radko"),
            new User("sim-3", "Evzen"),
            new User("sim-4", "Sebastian")
    );

    public RandomUserSimulator(MainScreenViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000 + random.nextInt(3000));
            } catch (InterruptedException e) {
                return; // ukonči thread pekne
            }

            Platform.runLater(() -> {
                if (viewModel.getVinyls().isEmpty()) return;

                Vinyl vinyl = viewModel.getVinyls()
                        .get(random.nextInt(viewModel.getVinyls().size()));

                User user = simUsers.get(random.nextInt(simUsers.size()));
                int action = random.nextInt(4);

                try {
                    switch (action) {
                        case 0 -> viewModel.reserveAs(vinyl, user);
                        case 1 -> viewModel.borrowAs(vinyl, user);
                        case 2 -> viewModel.returnAs(vinyl, user);
                    }
                } catch (IllegalStateException e) {
                    System.out.println("Simulation skipped invalid action: " + e.getMessage());
                }
            });
        }
    }
}
