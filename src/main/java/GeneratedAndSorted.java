import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GeneratedAndSorted {

    private static final ArrayList<String> stringList = new ArrayList<>();

    public static void main(String[] args) {

        try {
            Long beginTime = System.currentTimeMillis();
            Integer countLine = 2_000_000;
            String nameFile = generatedFile(countLine);
            ArrayList<String> files = splitFile(nameFile, 100_000);
            ArrayList<String> sortedFiles = sortedFiles(files);
            getSortedFile(sortedFiles, countLine);

            deleteFiles(files);
            deleteFiles(sortedFiles);

            System.out.println((System.currentTimeMillis() - beginTime));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFiles(ArrayList<String> sortedFiles) throws IOException {
        for (String nameFile:sortedFiles) {
            Files.delete(Paths.get(nameFile));
        }
    }

    private static void getSortedFile(ArrayList<String> sortedFiles, Integer countLine) throws IOException {

        String nameSortedFile = "sortedFile.txt";
        FileWriter writer = new FileWriter(nameSortedFile);

        HashMap<String, BufferedReader> nameFileReaderHashMap = new HashMap<>();

        for (String nameFile : sortedFiles) {
            nameFileReaderHashMap.put(nameFile, new BufferedReader(new FileReader(nameFile)));
        }

        TreeMap<String, String> valueNameFileTreeMap = new TreeMap<>();
        String removeNameFile = "";
        for (int v = 0; v < countLine; v++) {
            for (String nameFile : sortedFiles) {
                if (nameFile.equals(removeNameFile) || removeNameFile.length() == 0) {
                    BufferedReader reader = nameFileReaderHashMap.get(nameFile);
                    String line = reader.readLine();
                    if (line != null) {
                        valueNameFileTreeMap.put(line + ":" + nameFile, nameFile);
                    }
                }
            }
            String s = valueNameFileTreeMap.firstKey();

            Integer indexSep_1 = s.indexOf(":")+1;
            Integer indexSep_2 = s.substring(indexSep_1+1).indexOf(":") + indexSep_1 + 1;
            String result = s.substring(indexSep_1, indexSep_2) + ":" + s.substring(0,indexSep_1-1);

            writer.append(result);
            writer.append("\n");
            removeNameFile = valueNameFileTreeMap.get(valueNameFileTreeMap.firstKey());
            valueNameFileTreeMap.remove(valueNameFileTreeMap.firstKey());
        }

        for (String nameFile : sortedFiles) {
            nameFileReaderHashMap.get(nameFile).close();
        }

        writer.close();
    }

    private static ArrayList<String> sortedFiles(ArrayList<String> files) throws IOException {

        ArrayList<String> sortedFiles = new ArrayList<>();

        for (String nameFile : files) {
            BufferedReader reader = new BufferedReader(new FileReader(nameFile));
            String line = "";
            ArrayList<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Integer indexSeparator = line.indexOf(":");
                String code = line.substring(0, indexSeparator);
                if (code.length() < 4) {
                    code = "0" + code;
                }

                line = line.substring(indexSeparator + 1) + ":" + code;
                lines.add(line);
            }
            reader.close();

            Comparator<? super String> comparator = Comparator.naturalOrder();
            lines.sort(comparator);

            String fileName = nameFile + "_sorted.txt";
            try (FileWriter writer = new FileWriter(fileName, false)) {
                for (String line1 : lines) {
                    writer.append(line1);
                    writer.append("\n");
                }
            }
            sortedFiles.add(fileName);
        }
        return sortedFiles;
    }

    private static ArrayList<String> splitFile(String nameFile, Integer sizeSplit) throws IOException {
        ArrayList<String> files = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(nameFile));

        Integer count = 0;
        String line = "";
        while ((line = reader.readLine()) != null) {
            String fileName = "tested/dumpFile_" + count + ".txt";
            try (FileWriter writer = new FileWriter(fileName, false)) {
                writer.append(line);
                writer.append("\n");
                for (int i = 0; i < sizeSplit; i++) {
                    line = reader.readLine();
                    if (line != null) {
                        writer.append(line);
                        writer.append("\n");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            files.add(fileName);
            count++;
        }
        return files;
    }

    private static String generatedFile(Integer countLine) {
        fillStringList(100);
        String fileName = "tested/" + countLine + ".txt";
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (int i = 0; i < countLine; i++) {
                writer.append(getRandomNumber() + ":" + getRandomString());
                writer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

    private static void fillStringList(Integer lengthList) {
        String s = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456";
        char[] chars = s.toCharArray();
        //20-100
        for (int i = 0; i < lengthList; i++) {
            StringBuffer text = new StringBuffer();
            Integer v = ((Double) (Math.random() * (100d - 20d) + 20d)).intValue();
            for (int j = 0; j < v; j++) {
                Integer charNumber = ((Double) (Math.random() * s.length())).intValue();
                String s1 = String.valueOf(chars[charNumber]);
                text.append(s1);
            }
            stringList.add(text.toString());
        }

    }

    private static String getRandomString() {
        Double index = (Double) (Math.random() * stringList.size());
        return stringList.get(index.intValue());
    }

    private static String getRandomNumber() {
        Double index = Math.random() * 9999;
        return String.valueOf(index.intValue());
    }


}
