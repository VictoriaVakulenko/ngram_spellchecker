package sample.model;

import java.util.ArrayList;

/*
Модель для работы со словом.
 */
public class Word {
    private String word;
    private ArrayList bigram;
    private ArrayList trigram;
    private double similarity;

    public Word(){}

    public Word(String word) {
        this.word = word;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public Word(String word, double similarity){
        this.word = word;
        this.similarity = similarity;

    }

    public Word(ArrayList bigram, ArrayList trigram) {
        this.bigram = bigram;
        this.trigram = trigram;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setBigram(ArrayList bigram) {
        this.bigram = bigram;
    }

    public void setTrigram(ArrayList trigram) {
        this.trigram = trigram;
    }

    public ArrayList getBigram() {

        return bigram;
    }

    public ArrayList getTrigram() {
        return trigram;
    }

    public String getWord() {

        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
