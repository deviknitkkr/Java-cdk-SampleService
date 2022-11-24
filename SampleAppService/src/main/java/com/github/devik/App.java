package com.github.devik;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public String handle(Map<Object, Object> input, Context context) {
        System.out.println(input);
        return "Hello";
    }
}
