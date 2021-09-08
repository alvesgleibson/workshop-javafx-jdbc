package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jdk.vm.ci.meta.Local;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller sellerEntity;
	private SellerService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private Label labelErroName;

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroBirthDate;

	@FXML
	private Label labelErroBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setSeller(Seller sellerEntity) {
		this.sellerEntity = sellerEntity;
	}

	public void setSellerServe(SellerService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListener.add(listener);
	}

	@FXML
	public void onbtSaveAction(ActionEvent event) {
		if (sellerEntity == null) {
			throw new IllegalAccessError("Seller was Null");
		}
		if (service == null) {
			throw new IllegalAccessError("SellerService was null");
		}
		try {
			sellerEntity = getFormDate();
			service.saveOrUpdate(sellerEntity);
			notifyDataChangeListener();
			Utils.currentScene(event).close();

		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		}

		catch (DbException e) {
			Alerts.showAlert("Error saving Object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Seller getFormDate() {
		Seller dep = new Seller();
		ValidationException validException = new ValidationException("Validation Exception");

		dep.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			validException.addErrors("name", "Field Name can´t be empty");
		}

		dep.setName(txtName.getText());

		if (validException.getErrors().size() > 0) {
			throw validException;
		}

		return dep;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentScene(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}

	public void updateFormDateSeller() {

		if (sellerEntity == null) {
			throw new IllegalAccessError("Entity was null");
		}

		txtId.setText(String.valueOf(sellerEntity.getId()));
		txtName.setText(sellerEntity.getName());
		txtEmail.setText(sellerEntity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", sellerEntity.getBaseSalary()));

		if (sellerEntity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(sellerEntity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

	}

	public void setErrorMessage(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErroName.setText(errors.get("name"));
		}

	}

}
