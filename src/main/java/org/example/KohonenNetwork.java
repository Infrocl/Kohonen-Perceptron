package org.example;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KohonenNetwork {
    private double[][] weights;
    private int inputSize;
    private int outputSize;
    private double[] output;
    private double[] winnerPotential;
    private final double MIN_POTENTIAL = 0.75;
    private boolean freezeWeights;

    public KohonenNetwork(int inputSize, int outputSize) {
        this.winnerPotential = new double[]{MIN_POTENTIAL, MIN_POTENTIAL, MIN_POTENTIAL, MIN_POTENTIAL, MIN_POTENTIAL};
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.weights = new double[outputSize][inputSize];
        this.output = new double[outputSize];
        initializeWeights();
    }

    private void initializeWeights() {
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = Math.random() / 2;
            }
        }
    }

    public int findClosest(double[] input) {
        int winner = 0;
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < outputSize; i++) {
            double dist = 0;
            for (int j = 0; j < inputSize; j++) {
                dist += Math.pow((input[j] - weights[i][j]), 2);
            }
            dist = Math.sqrt(dist);
            if (dist < minDist && winnerPotential[i] >= MIN_POTENTIAL) {
                minDist = dist;
                winner = i;
            }
        }
        if (!freezeWeights) {
            for (int i = 0; i < outputSize; i++) {
                winnerPotential[i] += i == winner ? -MIN_POTENTIAL : 1.0 / outputSize;
            }
        }
//        System.out.println(winner);
        return winner;
    }

    public void adjustWeights(double[] input, int winner, double learningRate) {
        for (int i = 0; i < inputSize; i++) {
            weights[winner][i] +=  learningRate * (input[i] - weights[winner][i]);
        }
    }

    public void calculateOutput(double[] input) {
        for (int i = 0; i < outputSize; i++) {
            output[i] = 0;
            for (int j = 0; j < inputSize; j++) {
                output[i] += weights[i][j] * input[j];
            }
        }
    }

    public void train(double[][] trainingData, int epochs, double initialLearningRate) {
        double learningRate = initialLearningRate;
        freezeWeights = false;
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (double[] data : trainingData) {
                int winner = findClosest(data);
                adjustWeights(data, winner, learningRate);
            }
            learningRate *= 0.9;
        }
    }

    public double[] getNormalizedOutput(int winner) {
        double max = output[winner];
        double sigma = 4;
        for (int i = 0; i < outputSize; i++) {
            output[i] = i != winner ? Math.exp(-Math.pow(output[i] - max, 2)) / Math.pow(sigma, 2) : 1.0;
        }
        return output;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[] getOutput() {
        return output;
    }

    public void freezeWeights() {
        freezeWeights = true;
        Arrays.fill(winnerPotential, MIN_POTENTIAL);
    }
}
