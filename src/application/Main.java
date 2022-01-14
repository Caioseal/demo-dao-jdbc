package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("=== TEST 1 | FIND BY ID ===");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println();

        System.out.println("=== TEST 2 | FIND BY DEPARTMENT ===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller seller1 : list) {
            System.out.println(seller1);
        }
        System.out.println();

        System.out.println("=== TEST 3 | FIND ALL ===");
        List<Seller> list1 = sellerDao.findAll();
        for (Seller seller1 : list1) {
            System.out.println(seller1);
        }
        System.out.println();

        System.out.println("=== TEST 4 | INSERTING SELLER");
        Seller seller1 = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.00, department);
        sellerDao.insert(seller1);
        System.out.println();

        System.out.println("=== TEST 5 | UPDATING SELLER");
        seller = sellerDao.findById(1);
        seller.setName("Martha Wayne");
        sellerDao.update(seller);
        System.out.println();

        System.out.println("=== TEST 6 | DELETING SELLER");
        sellerDao.deleteById(12);
    }
}
