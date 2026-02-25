package org.example.pexesotextove;

import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
    private final int id;
    private boolean matched = false;
    private boolean faceUp = false;
    private final Button button;

    private final Image frontImage;
    private final Image backImage;

    // velikost obrázku na kartě (a zároveň velikost tlačítka)
    private static final double SIZE = 60;

    public Card(int id) {
        this.id = id;

        // načtení obrázků z resources
        this.backImage = loadImage("/images/back.png");
        this.frontImage = loadImage("/images/" + id + ".png");

        this.button = new Button();
        this.button.setMinSize(SIZE, SIZE);
        this.button.setMaxSize(SIZE, SIZE);

        // aby tlačítko neměnilo velikost podle grafiky
        this.button.setStyle("-fx-padding: 0;");

        // start: rub
        flipBack();
    }

    private Image loadImage(String path) {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Nenalezen obrázek v resources: " + path);
        }
        return new Image(stream);
    }

    private ImageView imageView(Image img) {
        ImageView iv = new ImageView(img);
        iv.setFitWidth(SIZE);
        iv.setFitHeight(SIZE);
        iv.setPreserveRatio(true); // když chceš přesně čtverec, dej false
        return iv;
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
        if (matched) {
            // volitelné: zablokovat klikání na spárovanou kartu
            button.setDisable(true);
            // nebo vizuálně "ztlumit"
            // button.setOpacity(0.6);
        }
    }

    public void flip() {
        button.setGraphic(imageView(frontImage));
        faceUp = true;
    }

    public void flipBack() {
        button.setGraphic(imageView(backImage));
        faceUp = false;
    }

    public boolean isFaceUp() {
        return faceUp;
    }
}