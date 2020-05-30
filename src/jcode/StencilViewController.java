package jcode;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class StencilViewController {
    @FXML
    GridPane grid1;
    @FXML
    GridPane fill_grid;
    @FXML
    GridPane en_grid;

    Matrix stencil;
    boolean dir;
    boolean process;

    public void setData(Matrix stencil, boolean dir, boolean process){
         this.stencil = stencil;
         this.dir = dir;
         this.process = process;
         init();
    }

    public void init(){
        int w = (int) (grid1.getPrefWidth()/stencil.m);
        grid1.setPrefWidth(stencil.m*w);
        fill_grid.setPrefWidth(stencil.m*w);
        int h = (int) (grid1.getPrefHeight()/stencil.n);
        grid1.setPrefHeight(stencil.n*h);
        fill_grid.setPrefHeight(stencil.n*h);
        ObservableList<ColumnConstraints> gridColumns = grid1.getColumnConstraints();
        gridColumns.clear();
        for(int i=0;i<stencil.m;i++){
            gridColumns.add(new ColumnConstraints(w));

        }
        gridColumns = fill_grid.getColumnConstraints();
        gridColumns.clear();
        for(int i=0;i<stencil.m;i++){
            gridColumns.add(new ColumnConstraints(w));

        }
        ObservableList<RowConstraints> gridRows = grid1.getRowConstraints();
        gridRows.clear();
        for(int i=0;i<stencil.n;i++){
            gridRows.add(new RowConstraints(h));
        }
        gridRows = fill_grid.getRowConstraints();
        gridRows.clear();
        for(int i=0;i<stencil.n;i++){
            gridRows.add(new RowConstraints(h));
        }
        Background background = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
        for(int i=0;i<stencil.n;i++){
            for(int j=0;j<stencil.m;j++){
                Label l = new Label();
                l.setMaxHeight(Double.MAX_VALUE);
                l.setMaxWidth(Double.MAX_VALUE);
                l.setBackground(background);
                if(stencil.getElement(i,j)==1)grid1.add(l,j,i);
            }
        }
        Matrix m = AlgoFunc.fillMatrixByRotating(stencil,dir);
        int count4 = stencil.n*stencil.m/4;
        Background background1 = new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY));
        Background background2 = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));
        Background background3 = new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY));
        Background background4 = new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY));
        for(int i=0;i<m.n;i++){
            for(int j=0;j<m.m;j++){
                Label l = new Label();
                l.setFont(new Font("Arial",14));
                l.setMaxHeight(Double.MAX_VALUE);
                l.setMaxWidth(Double.MAX_VALUE);
                l.setAlignment(Pos.CENTER);
                l.setText(String.valueOf(m.getElement(i,j)));
                switch ((m.getElement(i,j)-1)/count4){
                    case 0:
                        l.setBackground(background1);
                        break;
                    case 1:
                        l.setBackground(background2);
                        break;
                    case 2:
                        l.setBackground(background3);
                        break;
                    case 3:
                        l.setBackground(background4);
                        break;
                }
                fill_grid.add(l,j,i);
            }
        }
        Matrix enMatrix = process?AlgoFunc.getEncryptMatrix(m):AlgoFunc.getDecryptMatrix(m);
        int _w = (int) (en_grid.getPrefWidth()/(enMatrix.m+1));
        en_grid.setPrefWidth(_w*(enMatrix.m+1));
        int _h = (int) (en_grid.getPrefHeight()/(enMatrix.n+1));
        en_grid.setPrefHeight(_h*(enMatrix.n+1));
        en_grid.getColumnConstraints().clear();
        en_grid.getRowConstraints().clear();
        for(int i=0;i<(enMatrix.n+1);i++){
            en_grid.getColumnConstraints().add(new ColumnConstraints(_w));
            en_grid.getRowConstraints().add(new RowConstraints(_h));
        }
        for(int i=0;i<enMatrix.n;i++){
            for(int j=0;j<enMatrix.m;j++){
                if(enMatrix.getElement(i,j)==1){
                    Label l = new Label();
                    l.setMaxHeight(Double.MAX_VALUE);
                    l.setMaxWidth(Double.MAX_VALUE);
                    l.setBackground(background);
                    en_grid.add(l,j+1,i+1);
                }
            }
        }
        for(int i=0;i<enMatrix.n;i++){
            Label l = new Label();
            l.setMaxHeight(Double.MAX_VALUE);
            l.setMaxWidth(Double.MAX_VALUE);
            l.setText(String.valueOf(i+1));
            Label l1 = new Label();
            l1.setMaxHeight(Double.MAX_VALUE);
            l1.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER);
            l1.setAlignment(Pos.CENTER);
            l1.setText(String.valueOf(i+1));
            en_grid.add(l,0,i+1);
            en_grid.add(l1,i+1,0);
        }
    }

}
