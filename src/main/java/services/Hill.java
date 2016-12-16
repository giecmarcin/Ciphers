package services;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;

/**
 * Created by Marcin on 12.11.2016.
 */
public class Hill {
    private TreeMap<Character, Integer> map;
    private String text;

    public Hill() {
        //Create A=0, B=1, C=2, D=3.....
        this.map = new TreeMap<Character, Integer>();
        int j = 0;
        for (int i = 65; i <= 90; i++) {
            char temp = (char) i;
            map.put(temp, j);
            j++;
        }
    }

    public TreeMap<Character, Integer> getMap() {
        return map;
    }

    public void setMap(TreeMap<Character, Integer> map) {
        this.map = map;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String encrypt(String text, double[][] matrixKey, int dimensionOfMatrix) {
        //HELLO
        String originalText = text.toUpperCase().replaceAll("\\s+",""); //Change to upper case

        RealMatrix keyAsMatrix = MatrixUtils.createRealMatrix(matrixKey);

        List<Integer> originialTextNumerically = new ArrayList<Integer>();

        if(dimensionOfMatrix==2){
            if (originalText.length() % 2 != 0) {
                originalText += 'X';
            }
        }else if(dimensionOfMatrix==3){
            if (originalText.length() % 3 != 0) {
                originalText += 'X';
            }
        }


        //Change the letters on the number of
        for (int i = 0; i < originalText.length(); i++) {
            Integer numberOfLetter = map.get(originalText.charAt(i)); //Get the number for the letter
            originialTextNumerically.add(numberOfLetter);
        }

        //Grouping text in blocks
        List<Helper> originialTextNumericallyGroupedInBlocks = new ArrayList<Helper>();
        Iterator<Integer> iterator = originialTextNumerically.iterator();
        while (iterator.hasNext()) {
            if (dimensionOfMatrix == 2) {
                originialTextNumericallyGroupedInBlocks.add(new Helper(iterator.next(), iterator.next()));
            }else if(dimensionOfMatrix==3){
                originialTextNumericallyGroupedInBlocks.add(new Helper(iterator.next(), iterator.next(), iterator.next()));
            }

        }
        //Crate list of Matrix
        List<RealMatrix> listOfMatrix = new ArrayList<RealMatrix>();
        for (Helper h : originialTextNumericallyGroupedInBlocks) {
            if(dimensionOfMatrix==2){
                listOfMatrix.add(new Array2DRowRealMatrix(new double[]{h.first, h.second}));
            }else if(dimensionOfMatrix==3){
                listOfMatrix.add(new Array2DRowRealMatrix(new double[]{h.first, h.second, h.third}));
            }

        }

        List<RealMatrix> listTheMatrixMultiplication = new ArrayList<RealMatrix>();
        for (RealMatrix r : listOfMatrix) {
            RealMatrix temp = keyAsMatrix.multiply(r); //Matrix multiplication by a key
            listTheMatrixMultiplication.add(temp); //Add result to list
        }

        List<Integer> secretTextNumerically = new ArrayList<Integer>();
        //Checking the matrix multiplication as a result there is a number greater than 26
        for (RealMatrix r : listTheMatrixMultiplication) {
            for (int i = 0; i < r.getRowDimension(); i++) {
                double[] row = r.getRow(i);
                Double d = row[0]; //change double to Double
                Integer n = d.intValue(); //change to Integer
                if (n < 26) {
                    secretTextNumerically.add(n);
                } else {
                    secretTextNumerically.add(n % 26);
                }
            }
        }

        String encryptedText = "";
        //Conversion of numbers to letters
        for (Integer i : secretTextNumerically) {
            encryptedText += getKeyFromValue(i);
        }
        System.out.println(encryptedText);
        return  encryptedText;
    }

    public String  decrypt(String text, double[][] matrixKey, int dimensionOfMatrix){
        System.out.println(text);
        //text = "PIHZIN";
        String decryptedText = text.toUpperCase().trim();
        List<Integer> decryptedTextNumerically = new ArrayList<Integer>();
        //Change the letters on the number of
        for (int i = 0; i < decryptedText.length(); i++) {
            Integer numberOfLetter = map.get(decryptedText.charAt(i)); //Get the number for the letter
            decryptedTextNumerically.add(numberOfLetter);
        }

        //Grouping text in blocks
        List<Helper> decryptedTextNumericallyGroupedInBlocks = new ArrayList<Helper>();
        Iterator<Integer> iterator = decryptedTextNumerically.iterator();
        while (iterator.hasNext()) {
            if (dimensionOfMatrix == 2) {
                decryptedTextNumericallyGroupedInBlocks.add(new Helper(iterator.next(), iterator.next()));
            }else if(dimensionOfMatrix==3){
                decryptedTextNumericallyGroupedInBlocks.add(new Helper(iterator.next(), iterator.next(), iterator.next()));
            }
        }

        //Crate list of Matrix
        List<RealMatrix> listOfMatrix = new ArrayList<RealMatrix>();
        for (Helper h : decryptedTextNumericallyGroupedInBlocks) {
            if(dimensionOfMatrix==2){
                listOfMatrix.add(new Array2DRowRealMatrix(new double[]{h.first, h.second}));
            }else if(dimensionOfMatrix==3){
                listOfMatrix.add(new Array2DRowRealMatrix(new double[]{h.first, h.second, h.third}));
            }
        }
        System.out.println("Text as numeric before multiply: " + Arrays.asList(listOfMatrix));
        //Multiply
        RealMatrix keyAsMatrix = MatrixUtils.createRealMatrix(matrixKey);




        //Inverse Matrix x each matrix
        RealMatrix inverseMatrix = MatrixUtils.inverse(keyAsMatrix);

        for (int i = 0; i < inverseMatrix.getData().length; i++) {
            for (int j = 0; j < inverseMatrix.getData()[0].length; j++) {
                if (inverseMatrix.getData()[i][j] < 0) {
                    double asd = inverseMatrix.getData()[i][j] += 26;
                    inverseMatrix.setEntry(i, j, asd);
                }
            }
        }
        List<RealMatrix> listTheMatrixMultiplication = new ArrayList<RealMatrix>();
        for (RealMatrix r : listOfMatrix) {
            RealMatrix temp = inverseMatrix.multiply(r); //Matrix multiplication
            listTheMatrixMultiplication.add(temp); //Add result to list
        }

        List<Integer> originalTextNumerically = new ArrayList<Integer>();
        //Checking the matrix multiplication as a result there is a number greater than 26
        for (RealMatrix r : listTheMatrixMultiplication) {
            double[][] tempData = r.getData();
            for(int i=0;i<tempData.length;i++){
                for(int j=0; j<tempData[0].length;j++){
                    int number = (int) Math.round(tempData[i][j]);
                    if(number>26){
                        number = number%26;
                    }
                    if(number<0){
                        number = number+26;
                    }
                    originalTextNumerically.add(number);

                }
            }
        }
        //Conversion of numbers to letters
        String originalText="";
        for (Integer i : originalTextNumerically) {
            originalText += getKeyFromValue(i);
        }
        System.out.println(originalText);
        return  originalText;
    }

    public Object getKeyFromValue(Integer v) {
        Character ch = null;
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (v == value) {
                ch = (Character) key;
            }
        }
        return ch;
    }

    class Helper {
        private Integer first;
        private Integer second;
        private Integer third;

        public Helper(Integer first, Integer second) {
            this.first = first;
            this.second = second;
        }

        public Helper(Integer first, Integer second, Integer third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public Integer getThird() {
            return third;
        }

        public void setThird(Integer third) {
            this.third = third;
        }

        public Integer getFirst() {
            return first;
        }

        public void setFirst(Integer first) {
            this.first = first;
        }

        public Integer getSecond() {
            return second;
        }

        public void setSecond(Integer second) {
            this.second = second;
        }

        @Override
        public String toString() {
            return "{" + first + " " + second + "}";
        }
    }
}


