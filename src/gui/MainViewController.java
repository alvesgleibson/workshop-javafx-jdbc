package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{

	
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onmenuItemSellerAcition() {
		System.out.println("Action Sellec Clicked!!!");
	}
	
	@FXML
	public void onmenuItemDepartmentAcition() {
		System.out.println("Action Department Clicked!!!");
	}
	
	@FXML
	public void onmenuItemAboutAcition() {
		System.out.println("Action About Clicked!!!");
	}
	
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	

}
