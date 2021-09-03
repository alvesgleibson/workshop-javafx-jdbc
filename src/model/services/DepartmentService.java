package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		
		
		List<Department> listDepartment = new ArrayList<Department>();
		listDepartment.add(new Department(1, "TI"));
		listDepartment.add(new Department(2, "Call Center"));
		listDepartment.add(new Department(3, "Limpeza"));
		listDepartment.add(new Department(4, "Materiais"));
		listDepartment.add(new Department(6, "Superto 2.0"));		
		
		
		return listDepartment;
	}

}
