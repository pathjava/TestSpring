package ru.progwards.lesson1.calc.annotationsconfig;

public class SimpleCalculator implements ICalculator {
    public int sum(int a, int b) {
        return a + b;
    }

    public int diff(int a, int b) {
        return a - b;
    }

    public int div(int a, int b) {
        return a / b;
    }

    public int mult(int a, int b) {
        return a * b;
    }
}
