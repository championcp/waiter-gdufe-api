package com.waiterxiaoyy.backandroiddesign.utils;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        double[] demo = {10.0, 5.0, 1, 0.25, 0.1, 0.05, 0.01};
        Scanner scan = new Scanner(System.in);
//        System.out.print("请输入金额：");
//        double money = scan.nextDouble();
//        for(int i = 0; i < demo.length; i++) {
//            System.out.println((int)(money / demo[i]));
//            money = money - (int)(money / demo[i]) * demo[i];
//        }
        double a;
        int b, c, d, e, f, g, h;
        System.out.print("请输入金额：");
        a = scan.nextDouble();
        b =(int)(a / 10);
        c = (int)(( a - (int)a ) * 100 % 25);
        System.out.println(b + " ten dollar bills ");
        System.out.println(c + " five dollar bills ");
    }
}
