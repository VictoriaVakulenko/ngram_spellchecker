package sample.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.dao.WordDAO;
import sample.model.Word;

import java.util.ArrayList;
import java.util.List;


public class SChOverviewController {

    /*
    Экземляр класса для работы с БД.
     */
    private static WordDAO dao;

    /*
    Аннотацией @FXML отмечены элементы на форме, с помощью этих переменных связывается форма и контроллер.
     */

    @FXML
    private TextArea textArea;
    @FXML
    private TableView<Word> wordTable;
    @FXML
    private TableColumn<Word, String> wordColumn;
    @FXML
    private TableColumn<Word, String> alternativeColumn;
    @FXML
    private ListView listView;

    /*
    Листы, который содержат данные для таблицы и листа (списка).
     */
    public ObservableList<Word> wordData = FXCollections.observableArrayList();

    ArrayList<String> targetWords = new ArrayList<>();

    List<String> targetList = new ArrayList<>();

    public SChOverviewController() {
    }

    /*
    Инициализирует элементы на форме, используются лямбды. Так же содержить прослушиватель событий addListener
    (при нажатии на строку таблицы - в правой части экрана появляются все возможные альтернативы для слова.
    В таблице отображено слово неправильное слово, рядом с ним наиболее подходящий ответ.
     */

    @FXML
    private void initialize() {
        wordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWord()));
        alternativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dao.getBestAlternative(cellData.getValue()).toString()));
        showAlternatives(null);
        wordTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAlternatives(newValue.getWord()));
    }


    /*
    Обработчик кнопки Check. Изначально убирает из текста знаки пунктуации и цифры.
    Переводит в нижний регистр и делить слова по пробелам. Далее проводится проверка есть ли такое слово в словаре.
    Если нет, то слово заносится в список, который потом будет отобрахаться в TableView.
     */
    @FXML
    public void handleTextArea() {
        dao = new WordDAO();
        String text = textArea.getText();
        text = text.replaceAll("[\\?\\!\\.\\,\\:\\;\\-]", " ").replaceAll("\\d", "");
        //text = text.replaceAll("\\p{Punct}", " ").replaceAll("\\d", "");
        text = text.toLowerCase();
        String[] words = text.split("\\s+");
        for (String w : words) {
            if (!dao.isInDictionary(w) && w != " ") {
                targetWords.add(w);
                System.out.println(w);
            }
        }
        for (String w : targetWords) {
            wordData.add(new Word(w));
        }
        wordTable.setItems(wordData);
    }


    /*
    Обработчик кнопки Clear, чистит все представляния, нужно нажимать на кнопку при вводе нового текста для проверки
     */

    @FXML
    public void handleClear() {
        targetWords.clear();
        wordTable.getItems().clear();
        listView.getItems().clear();
        targetList.clear();
    }


    /*
    Функиция, которая заполянет listView словами, которые имеют степень похожести с исходным словом больше 0.2
    и разницу в длине не больше одного.
    Параметры можно изменить в строке (w.getSimilarity() >= 0.2 && wordDistance(w.getWord(), targetWord) <= 1)
     */

    private void showAlternatives(String targetWord) {
        if (targetWord != null) {
            listView.getItems().clear();
            targetList.clear();
            String firstChar = '^' + targetWord.substring(0, 1);
            int size = dao.getWordCount(firstChar);
            ArrayList<Word> list = dao.similarWord(firstChar, targetWord, size);
            for (Word w : list) {
                if (w.getSimilarity() >= 0.2 && wordDistance(w.getWord(), targetWord) <= 1) {
                    targetList.add(w.toString());
                }
            }
            listView.getItems().addAll(targetList);
        } else {
            targetList.clear();
            listView.getItems().clear();
        }

    }


    /*
    Функция считает на какое количество символов отличается слово1 от слово 2.
    Нужна для того, чтобы отсеивать слова
    с большой разницей в длине.
     */
    private static int wordDistance(String word1, String word2) {
        int distance = 0;
        if (word1.length() > word2.length()) {
            distance = word1.length() - word2.length();
        } else if (word2.length() > word1.length()) {
            distance = word2.length() - word1.length();
        }
        return distance;
    }
}
