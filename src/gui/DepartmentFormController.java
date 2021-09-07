package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department departmentEntity;
	private DepartmentService service;

	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErroName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void setDepartment(Department departmentEntity) {
		this.departmentEntity = departmentEntity;
	}

	public void setDepartmentServe(DepartmentService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {

		dataChangeListener.add(listener);
	}

	@FXML
	public void onbtSaveAction(ActionEvent event) {
		if (departmentEntity == null) {
			throw new IllegalAccessError("Department was Null");
		}
		if (service == null) {
			throw new IllegalAccessError("DepartmentService was null");
		}
		try {
			departmentEntity = getFormDate();
			service.saveOrUpdate(departmentEntity);
			notifyDataChangeListener();
			Utils.currentScene(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving Object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private Department getFormDate() {
		return new Department(Utils.tryParseToInt(txtId.getText()), txtName.getText());
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
	}

	public void updateFormDateDepertment() {

		if (departmentEntity == null) {
			throw new IllegalAccessError("Entity was null");
		}

		txtId.setText(String.valueOf(departmentEntity.getId()));
		txtName.setText(departmentEntity.getName());

	}

}
