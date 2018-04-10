package sample.dao;

import sample.controller.NGram;
import sample.model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WordDAO {
    private Connection connection;

    /*
    Получает подсоединение к базе данных. Через которое буду передаваться запросы к БД.
     */
    public WordDAO() {
        connection = DBUtil.getConnection();
    }

    /*
    Возвращает количество слов, которые начинаются на определенную букву.
     */
    public int getWordCount(String firstChar) {
        int count = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM corpus WHERE word RLIKE ?");
            preparedStatement.setString(1, firstChar);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                count = rs.getInt("COUNT(*)");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /*
    Проверяет есть ли такое слово в словаре.
     */
    public boolean isInDictionary(String word) {
        boolean status = true;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT word FROM corpus WHERE word = ?");
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                status = true;
            } else {
                status = false;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    /*
    Ищет слова, которые являются наиболее близкими к неправильному слову.
     */
    public ArrayList<Word> similarWord(String firstChar, String targetWord, int size) {
        ArrayList<Word> wordList = new ArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM corpus WHERE word RLIKE ? LIMIT 1,?");
            preparedStatement.setString(1, firstChar);
            preparedStatement.setInt(2, size);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Word word = new Word(rs.getString("word"), NGram.compareStrings(rs.getString("word"), targetWord, true));
                wordList.add(word);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    /*
    Показывает слово наиболее близкое, то есть с наибольшей стпенью похожести.
     */
    public Word getBestAlternative(Word word) {
        String targetWord = word.getWord();
        Word wordFinal;
        if (targetWord == null) {
            wordFinal = new Word("");
        } else {
            String firstChar = '^' + targetWord.substring(0, 1);
            int size = getWordCount(firstChar);
            List<Word> list = similarWord(firstChar, targetWord, size);
            wordFinal = Collections.max(list, Comparator.comparing(w -> w.getSimilarity()));
        }
        return wordFinal;
    }

}
