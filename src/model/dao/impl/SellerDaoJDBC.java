package model.dao.impl;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setObject(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
            }
            System.out.println("Done! Rows inserted: " + rowsAffected);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement("update seller set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? where Id = ?");

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setObject(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

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
            preparedStatement = connection.prepareStatement("delete from seller where Id = ?");
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Done! Rows deleted: " + rowsAffected);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id where seller.Id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet, department);
                preparedStatement.close();
                resultSet.close();
                return seller;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id order by name");
            resultSet = preparedStatement.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {
                Department department = map.get(resultSet.getInt("DepartmentId"));
                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department);
                }
                Seller seller = instantiateSeller(resultSet, department);
                list.add(seller);
            }
            return list;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement("select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id where DepartmentId = ? order by name");
            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {
                Department department1 = map.get(resultSet.getInt("DepartmentId"));

                if (department1 == null) {
                    department1 = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department1);
                }
                Seller seller1 = instantiateSeller(resultSet, department1);
                list.add(seller1);
            }
            return list;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getNString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }
}
