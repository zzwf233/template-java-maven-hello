package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyAdmin {
    private final List<Customer> customers;
    private final List<Product> products;
    private Customer loggedInCustomer;

    private Admin admin;
    public MyAdmin() {
        customers = new ArrayList<>();
        products = new ArrayList<>();
        loggedInCustomer = null;
        admin = null;
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
                    if (adminLogin(scanner)) {
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
            String username = scanner.next();
            if (!isValidUsername(username)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            System.out.print("请输入管理员密码：");
            String password = scanner.next();
            if (!isValidUserpassword(username)) {
                throw new IllegalArgumentException("无效的用户名！请输入数字或英文");
            }
            admin = new Admin(username, password);
            System.out.println("管理员账户注册成功！");
        }catch (IllegalArgumentException e) {
            System.out.println("错误： " + e.getMessage());
        }
    }
    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        return username.matches(regex);
    }
    private boolean isValidUserpassword(String userpassword) {
        String regex = "^[a-zA-Z0-9]+$";
        return userpassword.matches(regex);
    }


    private boolean adminLogin(Scanner scanner) {
        if (admin == null) {
            System.out.println("管理员账户不存在，请先注册管理员账户！");
            return false;
        }
        System.out.print("请输入管理员用户名：");
        String username = scanner.next();
        System.out.print("请输入管理员密码：");
        String password = scanner.next();
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            System.out.println("管理员登录成功！");
            return true;
        } else {
            System.out.println("管理员用户名或密码错误！");
            return false;
        }
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
                    loggedInCustomer = new Customer(loggedInCustomer.getName(), newPassword);
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