package model.dao.impl;

import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("insert into department (Name) values (?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, department.getName());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    department.setId(id);
                }
            }
            System.out.println("Done! Rows inserted: " + rowsAffected);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("update department set Name = ? where Id = ?");

            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Done! Rows updated: " + rowsAffected);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("delete from department where Id = ?");
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Done! Rows deleted: " + rowsAffected);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("select * from department where Id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                return department;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("select * from department");
            resultSet = preparedStatement.executeQuery();

            List<Department> departmentList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {
                Department department = map.get(resultSet.getInt("Id"));
                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("Id"), department);
                }
                departmentList.add(department);
            }
            return departmentList;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("Id"));
        department.setName(resultSet.getString("Name"));
        return department;
    }
}
