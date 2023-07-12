package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
*/
public class MyUser {
    //private static final String DB_URL = "jdbc:sqlite:demo.db";

    private final List<Customer> customers;
    private final List<Product> products;
    private Customer loggedInCustomer;
    private final ShoppingCart shoppingCart;

    public MyUser() {
        customers = new ArrayList<>();
        products = new ArrayList<>();
        loggedInCustomer = null;
        shoppingCart = new ShoppingCart();
    }
    protected void userSystem(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 用户系统 ====");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 返回上级菜单");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerCustomer(scanner);
                    break;
                case 2:
                    loggedInCustomer = UserLogin(scanner);
                    if (loggedInCustomer != null) {
                        shopping(scanner);
                    }
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
    private void registerCustomer(Scanner scanner) {
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();

        Customer newCustomer = new Customer(name, password);
        customers.add(newCustomer);

        System.out.println("注册成功！");
    }
    private Customer UserLogin(Scanner scanner) {
        System.out.print("请输入用户名：");
        String name = scanner.next();
        System.out.print("请输入密码：");
        String password = scanner.next();

        for (Customer customer : customers) {
            if (customer.getName().equals(name) && customer.getPassword().equals(password)) {
                System.out.println("登录成功！");
                return customer;
            }else if(!customer.getName().equals(name)){
                System.out.println("用户名错误！");
            }else if(!customer.getPassword().equals(password)){
                System.out.println("密码错误！");
            }
        }
        return null;
    }
    private Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
    // 用户系统功能
    private void shopping(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 购物系统 ====");
            System.out.println("1. 将商品加入购物车");
            System.out.println("2. 从购物车移除商品");
            System.out.println("3. 修改购物车中的商品数量");
            System.out.println("4. 模拟结账");
            System.out.println("5. 查看购物历史");
            System.out.println("6. 退出登录");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("请输入要加入购物车的商品名称：");
                    String productName = scanner.next();
                    Product product = getProductByName(productName);
                    if (product != null) {
                        System.out.print("请输入要加入购物车的数量：");
                        int quantity = scanner.nextInt();
                        shoppingCart.addItem(product, quantity);
                        System.out.println("商品已添加至购物车！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 2:
                    System.out.print("请输入要移除的商品名称：");
                    String pName = scanner.next();
                    Product p = getProductByName(pName);
                    if (p != null) {
                        System.out.print("请输入要移除的数量：");
                        int qty = scanner.nextInt();
                        shoppingCart.removeItem(p, qty);
                        System.out.println("商品已从购物车移除！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 3:
                    System.out.print("请输入要修改数量的商品名称：");
                    String prodName = scanner.next();
                    Product pr = getProductByName(prodName);
                    if (pr != null) {
                        System.out.print("请输入新的商品数量：");
                        int newQty = scanner.nextInt();
                        shoppingCart.updateQuantity(pr, newQty);
                        System.out.println("商品数量已修改！");
                    } else {
                        System.out.println("找不到该商品！");
                    }
                    break;
                case 4:
                    double total = shoppingCart.calculateTotal();
                    if (total > 0) {
                        System.out.println("结账成功！总金额为 $" + total);
                        shoppingCart.clear();
                    } else {
                        System.out.println("购物车为空，无法结账！");
                    }
                    break;
                case 5:
                    shoppingCart.displayCart();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
}

