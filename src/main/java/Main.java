import repository.Derby;
import ui.MainWindow;

import java.awt.*;


public class Main {

    public static void main(String[] args) {
        var derby = new Derby();
        var clientDao = derby.getClientDao();
        var workTypeDao = derby.getWorkTypeDao();
        var timeEntryDao = derby.getTimeEntryDao();
        EventQueue.invokeLater(() -> MainWindow.run(timeEntryDao, clientDao, workTypeDao));
    }
}
