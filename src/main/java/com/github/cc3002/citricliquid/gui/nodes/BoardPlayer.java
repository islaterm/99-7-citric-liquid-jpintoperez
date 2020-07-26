package com.github.cc3002.citricliquid.gui.nodes;

import com.github.cc3002.citricjuice.model.board.IPanel;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricliquid.gui.CitricLiquid;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.github.cc3002.citricliquid.gui.CitricLiquid.TILE_SIZE;

public class BoardPlayer {

  ImageView imageView;
  Player player;

  public Player getPlayer() {
    return player;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public BoardPlayer(Player player, String sprite_path) throws FileNotFoundException {
    this.player = player;
    imageView = new ImageView(new Image(new FileInputStream(CitricLiquid.RESOURCE_PATH + sprite_path)));
    imageView.setFitWidth(TILE_SIZE*0.75);
    imageView.setFitHeight(TILE_SIZE*0.75);
    imageView.setOnMouseEntered(event -> mouseHoverInfoText());
    updatePosition();
  }

  public void updatePosition() {

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(final long now) {
        IPanel panel = player.getCurrentPanel();

        if (player.isKOd()) {
          imageView.setOpacity(0.65);
        } else {
          imageView.setOpacity(1);
        }

        if (panel.getPlayers().size() == 1) {

          double originOffset = (TILE_SIZE - TILE_SIZE*0.75)/2;

          imageView.setX(panel.getX()*TILE_SIZE + originOffset);
          imageView.setY(panel.getY()*TILE_SIZE + originOffset);
          imageView.setFitWidth(TILE_SIZE*0.75);
          imageView.setFitHeight(TILE_SIZE*0.75);}
        else {
          imageView.setFitWidth(TILE_SIZE/2);
          imageView.setFitHeight(TILE_SIZE/2);

          int index = panel.getPlayers().indexOf(player);
          double xOffset = 0;
          double yOffset = 0;

          if (!(index%2==0)) { xOffset = (0.5*TILE_SIZE); }
          if (!(index>=2)) { yOffset = (0.5*TILE_SIZE); }

          imageView.setX(panel.getX()*TILE_SIZE + xOffset);
          imageView.setY(panel.getY()*TILE_SIZE + yOffset);
        }
      }
    };
    timer.start();
  }

  public void mouseHoverInfoText() {
    String str = "";
    str = str + player.getName() + " | ";
    str = str + "HP: "+player.getCurrentHP()+"/"+player.getMaxHP()+" | ";
    str = str + "ATK: "+player.getAtk()+" | ";
    str = str + "DEF: "+player.getDef()+" | ";
    str = str + "EVD: "+player.getEvd()+" | ";
    str = str + "Stars: "+player.getStars()+" | ";
    str = str + "Wins: "+player.getWins()+" | ";
    str = str + "Norma: "+player.getNormaLevel();
    CitricLiquid.setBottomText(str);
  }

}
