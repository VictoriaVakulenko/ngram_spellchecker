package sample.controller;

import java.util.ArrayList;
import java.util.Arrays;

public class NGram {

    public NGram() {
    }

    /*
    Формирует граммы для слова, с помощью type можно назначать биграммы и триграммы.
     */
    private static String[] letterPairs(String str, boolean type) {
        String[] pairs;
        if (type == true) {
            pairs = trigram(str);
        } else {
            pairs = bigram(str);
        }
        return pairs;

    }

    /*
    Создает биграммы.
     */
    public static String[] bigram(String word) {
        int numPairs = word.length() - 1;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = word.substring(i, i + 2);
        }
        return pairs;
    }

    /*
    Создает триграммы.
    */
    public static String[] trigram(String word) {
        int numPairs = word.length() - 2;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = word.substring(i, i + 3);
        }
        return pairs;
    }

    /*
    Создает граммы с типом ArrayList.
     */
    public static ArrayList wordLetterPairs(String str, boolean type) {
        ArrayList allPairs = new ArrayList();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("\\s");
        // For each word
        for (int w = 0; w < words.length; w++) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(words[w], type);
            for (int p = 0; p < pairsInWord.length; p++) {
                allPairs.add(pairsInWord[p]);
            }
        }
        return allPairs;
    }

    /*
    Сравнивает слова, возращает степень их похожести.
     */
    public static double compareStrings(String str1, String str2, boolean type) {
        ArrayList<String> pairs1 = wordLetterPairs(str1.toUpperCase(), type);
        ArrayList<String> pairs2 = wordLetterPairs(str2.toUpperCase(), type);
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i = 0; i < pairs1.size(); i++) {
            String pair1 = pairs1.get(i);
            String[] p1 = pair1.split("(?!^)");
            Arrays.sort(p1);
            for (int j = 0; j < pairs2.size(); j++) {
                String pair2 = pairs2.get(j);
                String[] p2 = pair2.split("(?!^)");
                Arrays.sort(p2);
                if (pair1.equals(pair2) || Arrays.equals(p1, p2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }
        return (2.0 * intersection) / union;
    }
}
