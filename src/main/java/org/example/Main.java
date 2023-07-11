package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while(true) {
            System.out.println("请输入你的指令, exit退出");
            System.out.print("你当前在第一级目录下 >");
            userInput = scanner.nextLine();

            if(userInput.equals("exit")) {
                break;
            }

            System.out.println("你刚才输入的是:" + userInput);

            if (userInput.equals("help")) {
                Main.help();
            }
        }

        scanner.close();
        System.out.println("Done.");
    }

    private static void help() {
        System.out.print("欢迎进入帮助子菜单");

        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        while(true) {
            System.out.println("请输入你的指令,q 退出");
            System.out.print("你当前在 help 的二级子目录下 >");
            userInput = scanner.nextLine();

            if (userInput.equals("q")) {
                break;
            }

            System.out.println("其实吧，这个也就是做个样子给你看看，让你知道怎么做二级界面罢了");
        }
    }
}