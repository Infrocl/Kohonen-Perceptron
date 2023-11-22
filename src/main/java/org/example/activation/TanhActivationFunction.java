package org.example.activation;

public class TanhActivationFunction implements ActivationFunction{
    @Override
    public double calculate(double input) {
        return Math.tanh(input);
    }
}
