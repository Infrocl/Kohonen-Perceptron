package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        DatasetService service = new DatasetService();
        service.loadData("dataset/aapl_raw_data.csv");
        int inputSize = 5;
        int outputSize = 5;
        int epochs = 100;
        double initialLearningRate = 0.1;
        double[][] trainData = service.getTrainX(inputSize);
        double[] trainY = service.getTrainY(inputSize);
        double[][] testData = service.getTestX(inputSize);
        double[] testY = service.getTestY(inputSize);
        double normalizationDivider = service.getNormalizationDivider();

        KohonenNetwork kohonenNetwork = new KohonenNetwork(inputSize, outputSize);
        MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron(inputSize, outputSize, 1, initialLearningRate);

        kohonenNetwork.train(trainData, epochs, initialLearningRate);
        kohonenNetwork.freezeWeights();

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < trainY.length; j++) {
                int winner = kohonenNetwork.findClosest(trainData[j]);
                kohonenNetwork.calculateOutput(trainData[j]);
                multilayerPerceptron.train(kohonenNetwork.getNormalizedOutput(winner), new double[]{trainY[j]});
            }
        }

        for (double[] input : testData) {
            int winner = kohonenNetwork.findClosest(input);
            kohonenNetwork.calculateOutput(input);
            double[] result = multilayerPerceptron.forward(kohonenNetwork.getNormalizedOutput(winner));
            for (int i = 0; i < result.length; i++) {
                result[i] *= normalizationDivider;
            }
            System.out.println(Arrays.toString(result));
        }

        for (int i = 0; i < testY.length; i++) {
            testY[i] *= normalizationDivider;
        }
//        for (int i = 0; i < trainY.length; i++) {
//            trainY[i] *= normalizationDivider;
//        }
        System.out.println(Arrays.toString(testY));
    }
}