package org.example.pexesotextove;

import javafx.scene.control.Button;

public class Card {
    private final int id;
    private boolean matched = false;
    private final Button button;

    public Card(int id) {
        this.id = id;
        this.button = new Button("?");
        this.button.setMinSize(60, 60);
    }

    public int getId() {
        return id;
    }

    public Button getButton() {
        return button;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public void flip() {
        button.setText(String.valueOf(id));
    }

    public void flipBack() {
        button.setText("?");
    }
}
