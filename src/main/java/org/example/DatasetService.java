package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DatasetService {
    private double normalizationDivider;
    private ArrayList<Double> data;

    private final int TEST_SIZE = 10;

    public DatasetService() {
        data = new ArrayList<>();
    }

    enum Headers {
        date, open, high, low, close, volume, adjusted_close, change_percent, avg_vol_20d
    }

    public void loadData(String filePath) {
        ChartService service = new ChartService();
        try {
            Reader in = new FileReader(filePath);
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(Headers.class)
                    .setSkipHeaderRecord(true)
                    .build();
            Iterable<CSVRecord> records = csvFormat.parse(in);
//            ArrayList<String> dates = new ArrayList<>();
            for (CSVRecord record : records) {
  //              dates.add(record.get("date"));
                data.add(Double.parseDouble(record.get("close")));
            }
 //           service.draw(dates, data);
            data = (ArrayList<Double>) normalizeData(data);
//            service.draw(dates, data);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<Double> normalizeData(List<Double> list) {
        normalizationDivider = 0;
        for (double value : list) {
            normalizationDivider += Math.pow(value, 2);
        }
        normalizationDivider = Math.sqrt(normalizationDivider);
        list.replaceAll(value -> value/normalizationDivider);
        return list;
    }

    public double[][] getTrainX(int inputSize) {
        double[][] result = new double[data.size() - TEST_SIZE - inputSize][inputSize];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < inputSize; j++) {
                result[i][j] = data.get(i + j);
            }
        }
        return result;
    }

    public double[] getTrainY(int inputSize) {
        double[] result = new double[data.size() - TEST_SIZE - inputSize];
        for (int i = 0; i < result.length; i++) {
            result[i] = data.get(i + inputSize);
        }
        return result;
    }

    public double[][] getTestX(int inputSize) {
        double[][] result = new double[TEST_SIZE][inputSize];
        int index = data.size() - TEST_SIZE - inputSize;
        for (int i = 0; i < result.length; i++, index++) {
            for (int j = 0; j < inputSize; j++) {
                result[i][j] = data.get(index + j);
            }
        }
        return result;
    }

    public double[] getTestY(int inputSize) {
        double[] result = new double[TEST_SIZE];
        int index = data.size() - TEST_SIZE;
        for (int i = 0; i < result.length; i++, index++) {
            result[i] = data.get(index);
        }
        return result;
    }

    public ArrayList<Double> getData() {
        return data;
    }

    public double getNormalizationDivider() {
        return normalizationDivider;
    }
}
