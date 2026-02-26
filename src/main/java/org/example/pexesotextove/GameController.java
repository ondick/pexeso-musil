package org.example.pexesotextove;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label labelScorePlayer1;
    @FXML
    private Label labelScorePlayer2;
    @FXML
    private Label labelScorePlayer3;
    @FXML
    private Label pocetTahu1;
    @FXML
    private Label pocetTahu2;
    @FXML
    private Label pocetTahu3;
    @FXML
    private Label labelCurrentPlayer;
    @FXML
    private Label labelWinner;
    @FXML
    private Button restartButton;

    private ArrayList<Card> cards = new ArrayList<>();
    private Card firstCard = null;
    private Card secondCard = null;
    private boolean canFlip = true;

    private int currentPlayer = 1;
    private int score1 = 0;
    private int score2 = 0;
    private int score3 = 0;
    private int tah1 = 0;
    private int tah2 = 0;
    private int tah3 = 0;

    @FXML
    public void initialize() {
        generateCards();
        Collections.shuffle(cards);
        displayCards();
        updateUI();
    }

    private void generateCards() {
        for (int i = 1; i <= 13; i++) {
            if (i == 13){
                cards.add(new Card(i));
            }
            else {
                cards.add(new Card(i));
                cards.add(new Card(i));
            }
        }
    }

    private void displayCards() {
        gridPane.getChildren().clear();
        int index = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Card card = cards.get(index++);
                Button btn = card.getButton();
                btn.setOnAction(e -> handleCardClick(card));
                gridPane.add(btn, col, row);
            }
        }
    }



    private void handleCardClick(Card card) {
        if (!canFlip || card.isMatched() || card.isFaceUp()) {
            return;
        }

        card.flip();

        if (firstCard == null) {
            firstCard = card;
        } else {
            secondCard = card;
            canFlip = false;

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> checkMatch());
            pause.play();
        }
    }



    private void checkMatch() {
        if (firstCard.getId()==13) {
            if (currentPlayer == 1) {
                score1 = score1 + 3;
                tah1++;
            } else if (currentPlayer == 2) {
                score2 =  score2 + 3;
                tah2++;
            }
            else  {
                score3 =  score3 + 3;
                tah3++;
            }
            secondCard.flipBack();
        }
        else if (secondCard.getId()==13) {
            if (currentPlayer == 1) {
                score1 = score1 + 3;
                tah1++;
            } else if (currentPlayer == 2) {
                score2 =  score2 + 3;
                tah2++;
            }
            else  {
                score3 =  score3 + 3;
                tah3++;
            }
            firstCard.flipBack();

        }

        else if (firstCard.getId() == secondCard.getId()) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            if (currentPlayer == 1) {
                score1++;
                tah1++;
            } else if (currentPlayer == 2) {
                score2++;
                tah2++;
            }
            else  if (currentPlayer == 3) {
                score3++;
                tah3++;
            }
        } else {
            firstCard.flipBack();
            secondCard.flipBack();
            if (currentPlayer == 1) {
                currentPlayer = 2;
                tah1++;
            } else if (currentPlayer == 2) {
                currentPlayer = 3;
                tah2++;
            }
            else  if (currentPlayer == 3) {
                currentPlayer = 1;
                tah3++;
            }
        }

        firstCard = null;
        secondCard = null;
        canFlip = true;
        updateUI();

        if (isGameOver()) {
            showWinner();
        }
    }
    //cigan konci hra???
    private boolean isGameOver() {
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.get(i).isMatched()) {
                return false;
            }
        }
        return true;
    }

    private void showWinner() {
        String message;

        if (score1 > score2) {
            if (score1 > score3) {
                message = "Vyhrál hráč 1!";
            } else {
                message = "";
            }

        } else if (score2 > score1) {
            if (score2 > score3) {
                message = "Vyhrál hráč 2!";
            } else {
                message = "";
            }
        } else if (score3>score1) {
            if (score3 > score2) {
                message = "Vyhrál hráč 3!";
            } else {
                message = "";
            }

        } else {
            message = "Remíza!";
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Konec hry");
            alert.setHeaderText(message);
            alert.setContentText("Chceš hrát znovu?");
            alert.showAndWait();

            restartGame();
        });
    }

    private void restartGame() {
        // reset skóre
        score1 = 0;
        score2 = 0;
        score3 = 0;
        tah1 = 0;
        tah2 = 0;
        tah3 = 0;
        currentPlayer = 1;

        // vyčistit staré karty
        cards.clear();
        firstCard = null;
        secondCard = null;
        canFlip = true;

        // znovu vytvořit hru
        generateCards();
        Collections.shuffle(cards);
        displayCards();
        updateUI();

        labelWinner.setText("");
    }

    private void updateUI() {
        labelScorePlayer1.setText("Skóre hráče 1: " + score1);
        labelScorePlayer2.setText("Skóre hráče 2: " + score2);
        labelScorePlayer3.setText("Skóre hráče 3: " + score3);
        labelCurrentPlayer.setText("Na tahu hráč: " + currentPlayer);
        pocetTahu1.setText("Hráč 1 má: "+tah1+" tahů.");
        pocetTahu2.setText("Hráč 2 má: "+tah2+" tahů.");
        pocetTahu3.setText("Hráč 3 má: "+tah3+" tahů.");
    }
}
