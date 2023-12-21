package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;

public class Projection {
    public static void main(String[] args) {
        try {
            List<Point> originalDataset = readDataset("C:\\Users\\Downloads\\output_dataset.txt");
            double[][] projectionMatrix = calculateProjectionMatrix(540, 960);
            List<Point> projectedDataset = projectDataset(originalDataset, projectionMatrix);
            writeDataset(projectedDataset, "C:\\Users\\Downloads\\new_dataset.txt");
            drawAndSaveImage(projectedDataset, "C:\\Users\\Downloads\\new_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static List<Point> readDataset(String filePath) throws IOException {
        List<Point> dataset = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(" ");
                double x = Double.parseDouble(values[0]);
                double y = Double.parseDouble(values[1]);
                dataset.add(new Point(x, y));
            }
        }
        return dataset;
    }

    private static double[][] calculateProjectionMatrix(double cx, double cy) {
        double[][] matrix = new double[3][3];
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[0][2] = -cx;
        matrix[1][2] = -cy;
        return matrix;
    }

    private static List<Point> projectDataset(List<Point> dataset, double[][] matrix) {
        List<Point> projectedDataset = new ArrayList<>();
        for (Point point : dataset) {
            double x = matrix[0][0] * point.x + matrix[0][1] * point.y + matrix[0][2];
            double y = matrix[1][0] * point.x + matrix[1][1] * point.y + matrix[1][2];
            projectedDataset.add(new Point(x, y));
        }
        return projectedDataset;
    }

    private static void writeDataset(List<Point> dataset, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Point point : dataset) {
                writer.write(point.x + " " + point.y);
                writer.newLine();
            }
        }
    }

    private static void drawAndSaveImage(List<Point> dataset, String filePath) throws IOException {
        int width = 960;
        int height = 540;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        for (Point point : dataset) {
            int x = (int) Math.round(point.x);
            int y = (int) Math.round(point.y);
            g.fillRect(x, y, 2, 2);
        }

        ImageIO.write(image, "png", new File(filePath));

    }
}
