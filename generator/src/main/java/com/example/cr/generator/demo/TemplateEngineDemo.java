package com.example.cr.generator.demo;

public class TemplateEngineDemo {
    public static void main(String[] args) {
        String template = "我是 XXX";
        String data = "SB";
        if (args != null && args.length > 0){
            data = args[0];
        }
        String result = template.replace("XXX", data);
        System.out.println(result);
    }
}
