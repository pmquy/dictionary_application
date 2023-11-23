package com.example.dictionary.utils;

import javafx.scene.Node;

class Delta { double x, y; }
public class Utils {
    public static void Drag(Node node) {
        Delta delta = new Delta();
        node.setOnMousePressed(mouseEvent -> {
            delta.x = node.getLayoutX() - mouseEvent.getSceneX();
            delta.y = node.getLayoutY() - mouseEvent.getSceneY();
        });

        node.setOnMouseDragged( mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() + delta.x);
            node.setLayoutY(mouseEvent.getSceneY() + delta.y);
        });
    }
}
