package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentScene(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String msg) {
		try {
			return Integer.parseInt(msg);
		} catch (NumberFormatException e) {
			return null;
		}

	}

}
