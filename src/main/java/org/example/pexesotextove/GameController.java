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
    private Label labelCurrentPlayer;
    @FXML
    private Label labelWinner;

    private ArrayList<Card> cards = new ArrayList<>();
    private Card firstCard = null;
    private Card secondCard = null;
    private boolean canFlip = true;

    private int currentPlayer = 1;
    private int score1 = 0;
    private int score2 = 0;

    @FXML
    public void initialize() {
        generateCards();
        Collections.shuffle(cards);
        displayCards();
        updateUI();
    }

















    private void generateCards() {
        for (int i = 1; i <= 8; i++) {
            cards.add(new Card(i));
            cards.add(new Card(i));
        }
    }

    private void displayCards() {
        gridPane.getChildren().clear();
        int index = 0;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
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

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> checkMatch());
            pause.play();
        }
    }






    private void checkMatch() {
        if (firstCard.getId() == secondCard.getId()) {
            firstCard.setMatched(true);
            secondCard.setMatched(true);
            if (currentPlayer == 1) {
                score1++;
            } else {
                score2++;
            }
        } else {
            firstCard.flipBack();
            secondCard.flipBack();
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                currentPlayer = 1;
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
            message = "Vyhrál hráč 1!";
        } else if (score2 > score1) {
            message = "Vyhrál hráč 2!";
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
        labelCurrentPlayer.setText("Na tahu hráč: " + currentPlayer);
    }
}
