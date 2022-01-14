package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.SQLException;
import java.util.List;

public class Main2 {

    public static void main(String[] args) throws SQLException {
        System.out.println("=== TEST 1 | FIND BY ID ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department department = departmentDao.findById(3);
        System.out.println(department);
        System.out.println();

        System.out.println("=== TEST 2 | FIND ALL ===");
        List<Department> departmentList = departmentDao.findAll();
        for (Department department1 : departmentList) {
            System.out.println(department1);
        }
        System.out.println();

        System.out.println("=== TEST 3 | INSERTING DEPARTMENT ===");
        Department department1 = new Department(null, "Flowers");
        departmentDao.insert(department1);
        System.out.println();

        System.out.println("=== TEST 4 | UPDATING DEPARTMENT ===" );
        department = departmentDao.findById(7);
        department.setName("Appliances");
        departmentDao.update(department);
        System.out.println();

        System.out.println("=== TEST 5 | DELETING DEPARTMENT ===");
        departmentDao.deleteById(7);
    }
}
