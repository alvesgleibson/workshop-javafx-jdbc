package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller sellerEntity;
	private SellerService service;
	private DepartmentService departmentService;

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
	private ComboBox<Department> comboBoxDepartment;

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

	private ObservableList<Department> obsList;

	public void setSeller(Seller sellerEntity) {
		this.sellerEntity = sellerEntity;
	}

	public void setService(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;

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

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			validException.addErrors("Email", "Field Name can´t be empty");
		}
		dep.setEmail(txtEmail.getText());

		if (dpBirthDate.getValue() == null) {
			validException.addErrors("birthDate", "Field Name can´t be empty");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			dep.setBirthDate(Date.from(instant));
		}

		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			validException.addErrors("baseSalary", "Field Name can´t be empty");
		}

		dep.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));

		dep.setDepartment(comboBoxDepartment.getValue());

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
		initializeComboBoxDepartment();
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

		if (sellerEntity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(sellerEntity.getDepartment());
		}

	}

	public void loadAssociateObjects() {

		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null!!!");
		}
		List<Department> depList = departmentService.findAll();
		obsList = FXCollections.observableArrayList(depList);

		comboBoxDepartment.setItems(obsList);

	}

	public void setErrorMessage(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		labelErroName.setText(fields.contains("name") ? errors.get("name") : "");

		labelErroEmail.setText(fields.contains("Email") ? errors.get("Email") : "");

		labelErroBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");

		labelErroBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
