package org.example.activation;

public class TanhDerivativeFunction implements ActivationFunction{
    @Override
    public double calculate (double input) {
        return Math.tanh(input) * (1 - Math.tanh(input));
    }
}
