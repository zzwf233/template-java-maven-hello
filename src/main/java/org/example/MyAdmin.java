package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class MyAdmin {
    private static final String DB_URL2 = "jdbc:sqlite:Admin.db";
    private static final String CREATE_TABLE_ADMIN = "CREATE TABLE IF NOT EXISTS admin(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,password TEXT)";

    private final Connection connection;
    private final List<Customer> customers;
    private final List<Product> products;
    private Admin loggedInAdmin;

    private Admin admin;
    public MyAdmin() {
        connection = createConnection();
        customers = new ArrayList<>();
        products = new ArrayList<>();
        loggedInAdmin = null;
        admin = null;

        createTables();
    }
    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_ADMIN);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Connection createConnection(){
        try{
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL2);
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void adminSystem(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 管理员系统 ====");
            System.out.println("1. 注册管理员账户");
            System.out.println("2. 登录管理员账户");
            System.out.println("3. 返回上级菜单");

            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerAdmin(scanner);
                    break;
                case 2:
                    if (adminLogin(scanner) != null) {
                        adminMenu(scanner);
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
    private void registerAdmin(Scanner scanner) {
        try {
            if (admin != null) {
                System.out.println("管理员账户已存在，无法注册新的管理员账户！");
                return;
            }
            System.out.print("请输入管理员用户名：");
            String adminname = scanner.next();
            if (!isValidUsername(adminname)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            System.out.print("请输入管理员密码：");
            String password = scanner.next();
            if (!isValidUserpassword(password)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO admin (name, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, adminname);
                statement.setString(2, password);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int adminId = generatedKeys.getInt(1);
                        admin = new Admin(adminId, adminname, password);
                        System.out.println("管理员账号注册成功！");
                    }
                } else {
                    System.out.println("注册管理员账号失败！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }catch (IllegalArgumentException e) {
            System.out.println("错误： " + e.getMessage());
        }
    }
    private boolean isValidUsername(String adminname) {
        String regex = "^[a-zA-Z0-9]+$";
        return adminname.matches(regex);
    }
    private boolean isValidUserpassword(String userpassword) {
        String regex = "^[a-zA-Z0-9]+$";
        return userpassword.matches(regex);
    }


    private Admin adminLogin(Scanner scanner) {
        if (admin == null) {
            System.out.println("管理员账户不存在，请先注册管理员账户！");
            return null;
        }
        System.out.print("请输入管理员用户名：");
        String adminname = scanner.next();
        System.out.print("请输入管理员密码：");
        String password = scanner.next();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE name = ? AND password = ?")) {
            statement.setString(1, adminname);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int adminId = resultSet.getInt("id");
                String adminName = resultSet.getString("name");
                admin= new Admin(adminId, adminName, password);
                System.out.println("管理员账号登陆成功！");
                return admin;
            } else {
                System.out.println("用户名或密码错误！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void adminMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 管理员系统 ====");
            System.out.println("1. 密码管理");
            System.out.println("2. 客户管理");
            System.out.println("3. 商品管理");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    passwordManagement(scanner);
                    break;
                case 2:
                    customerManagement(scanner);
                    break;
                case 3:
                    productManagement(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
    private void passwordManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 密码管理 ====");
            System.out.println("1. 修改管理员密码");
            System.out.println("2. 重置用户密码");
            System.out.println("3. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("请输入新密码：");
                    String newPassword = scanner.next();
                    loggedInAdmin = new Admin(loggedInAdmin.getAdminId(),loggedInAdmin.getUsername(), newPassword);
                    System.out.println("密码修改成功！");
                    break;
                case 2:
                    System.out.print("请输入要重置密码的用户名：");
                    String username = scanner.next();
                    Customer customer = getCustomerByUsername(username);
                    if (customer != null) {
                        System.out.print("请输入新密码：");
                        String newPwd = scanner.next();
                        customer.setPassword(newPwd);
                        System.out.println("用户密码重置成功！");
                    } else {
                        System.out.println("找不到该用户！");
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
    private Customer getCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getName().equals(username)) {
                return customer;
            }
        }
        return null;
    }
    private void customerManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 客户管理 ====");
            System.out.println("1. 列出所有客户信息");
            System.out.println("2. 删除客户信息");
            System.out.println("3. 查询客户信息");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    listAllCustomers();
                    break;
                case 2:
                    System.out.print("请输入要删除的用户名：");
                    String username = scanner.next();
                    deleteCustomer(username);
                    break;
                case 3:
                    System.out.print("请输入要查询的用户名：");
                    String name = scanner.next();
                    Customer customer = getCustomerByUsername(name);
                    if (customer != null) {
                        System.out.println("用户名：" + customer.getName() + " 密码：" + customer.getPassword());
                    } else {
                        System.out.println("找不到该用户！");
                    }
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择！");
                    break;
            }
        }
    }
    private void listAllCustomers() {
        System.out.println("所有客户信息：");
        for (Customer customer : customers) {
            System.out.println("用户名：" + customer.getName() + " 密码：" + customer.getPassword());
        }
    }

    private void deleteCustomer(String username) {
        Customer customer = getCustomerByUsername(username);
        if (customer != null) {
            customers.remove(customer);
            System.out.println("用户删除成功！");
        } else {
            System.out.println("找不到该用户！");
        }
    }

    private void productManagement(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n==== 商品管理 ====");
            System.out.println("1. 列出所有商品信息");
            System.out.println("2. 添加商品信息");
            System.out.println("3. 修改商品信息");
            System.out.println("4. 删除商品信息");
            System.out.println("5. 查询商品信息");
            System.out.println("6. 返回上级菜单");
            System.out.print("请选择操作：");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listAllProducts();
                    break;
                case 2:
                    addProduct(scanner);
                    break;
                case 3:
                    System.out.print("请输入要修改的商品名称：");
                    String name = scanner.next();
                    updateProduct(name, scanner);
                    break;
                case 4:
                    System.out.print("请输入要删除的商品名称：");
                    String productName = scanner.next();
                    deleteProduct(productName);
                    break;
                case 5:
                    System.out.print("请输入要查询的商品名称：");
                    String pname = scanner.next();
                    Product product = getProductByName(pname);
                    if (product != null) {
                        System.out.println("商品名称：" + product.getName() + " 价格：" + product.getPrice());
                    } else {
                        System.out.println("找不到该商品！");
                    }
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

    private void listAllProducts() {
        System.out.println("所有商品信息：");
        for (Product product : products) {
            System.out.println("商品名称：" + product.getName() + " 价格：" + product.getPrice());
        }
    }

    private void addProduct(Scanner scanner) {
        System.out.print("请输入商品名称：");
        String name = scanner.next();
        System.out.print("请输入商品价格：");
        double price = scanner.nextDouble();

        Product newProduct = new Product(name, price);
        products.add(newProduct);

        System.out.println("商品添加成功！");
    }

    private void updateProduct(String name, Scanner scanner) {
        Product product = getProductByName(name);
        if (product != null) {
            System.out.print("请输入新的商品价格：");
            double newPrice = scanner.nextDouble();
            product.setPrice(newPrice);
            System.out.println("商品信息修改成功！");
        } else {
            System.out.println("找不到该商品！");
        }
    }

    private void deleteProduct(String productName) {
        Product product = getProductByName(productName);
        if (product != null) {
            products.remove(product);
            System.out.println("商品删除成功！");
        } else {
            System.out.println("找不到该商品！");
        }
    }

    private Product getProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
