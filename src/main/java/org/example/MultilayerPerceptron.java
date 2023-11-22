package org.example;

import org.example.activation.ActivationFunction;
import org.example.activation.TanhActivationFunction;
import org.example.activation.TanhDerivativeFunction;

import java.util.Arrays;

public class MultilayerPerceptron {
    private double[][] hiddenWeights;
    private double[][] outputWeights;

    private double learningRate;
    private final TanhActivationFunction activationFunction = new TanhActivationFunction();
    private final TanhDerivativeFunction derivativeFunction = new TanhDerivativeFunction();

    public MultilayerPerceptron(int inputSize, int hiddenSize, int outputSize, double learningRate) {
        this.learningRate = learningRate;
        hiddenWeights = new double[hiddenSize][inputSize];
        outputWeights = new double[outputSize][hiddenSize];
        initializeWeights(hiddenWeights);
        initializeWeights(outputWeights);
    }

    private void initializeWeights(double[][] weights) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] = Math.random() / 2;
            }
        }
    }

    public double[] calculate(double[] input, int outputSize, ActivationFunction function, double[][] weightsArray) {
        double[] result = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            result[i] = 0;
            for (int j = 0; j < input.length; j++) {
                result[i] += weightsArray[i][j] * input[j];
            }
            result[i] = function.calculate(result[i]);
        }
        return result;
    }

    public double[] forward(double[] input) {
        double[] hiddenOutput = calculate(input, hiddenWeights.length, activationFunction, hiddenWeights);
        return calculate(hiddenOutput, outputWeights.length, activationFunction, outputWeights);
    }

//    public double[] backward(double[] output, double[] idealOutput) {
//        int size = output.length;
//        double[] loss = new double[size];
//        for (int i = 0; i < size; i++) {
//            loss[i] = 0.5 * Math.pow(output[i] - idealOutput[i], 2);
//        }
//        double[] outputLoss = calculate(loss, size, derivativeFunction, outputWeights);
//        return calculate(outputLoss, size, derivativeFunction, hiddenWeights);
//    }

    public void adjustOutputWeights(double[] calcOutput, double[] idealOutput, double[] hiddenOutputForward, double[] outputLoss) {
        for (int i = 0; i < outputWeights.length; i++) {
            for (int j = 0; j < outputWeights[0].length; j++) {
                outputWeights[i][j] -= learningRate * (calcOutput[i] - idealOutput[i]) * outputLoss[i] * hiddenOutputForward[j];
            }
        }
    }

    public void adjustHiddenWeights(double[] input, double[] calcOutput, double[] idealOutput, double[] outputLoss, double[] hiddenOutputLoss) {
        for (int i = 0; i < hiddenWeights.length; i++) {
            for (int j = 0; j < hiddenWeights[0].length; j++) {
                double derivative = 0;
                for (int s = 0; s < outputLoss.length; s++) {
                    derivative += (calcOutput[s] - idealOutput[s]) * outputLoss[s] * outputWeights[s][j] * hiddenOutputLoss[s] * input[i];
                }
                hiddenWeights[i][j] -= learningRate * derivative;
            }
        }
    }

    public void train(double[] input, double[] idealOutput) {
        double[] hiddenOutput = calculate(input, hiddenWeights.length, activationFunction, hiddenWeights);
        double[] calcOutput = calculate(hiddenOutput, outputWeights.length, activationFunction, outputWeights);
        int size = calcOutput.length;
        double[] loss = new double[size];
        for (int i = 0; i < size; i++) {
            loss[i] = 0.5 * Math.pow(calcOutput[i] - idealOutput[i], 2);
        }
        double[] outputLoss = calculate(loss, size, derivativeFunction, outputWeights);
        double[] hiddenLoss = calculate(outputLoss, size, derivativeFunction, hiddenWeights);
        adjustHiddenWeights(input, calcOutput, idealOutput, outputLoss, hiddenLoss);
        adjustOutputWeights(calcOutput, idealOutput, hiddenOutput, outputLoss);
//        System.out.println(Arrays.toString(calcOutput));
//        System.out.println(Arrays.toString(outputLoss));
    }

}
