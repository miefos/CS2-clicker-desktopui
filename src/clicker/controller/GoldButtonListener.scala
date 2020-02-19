package clicker.controller

import javafx.event.{ActionEvent, EventHandler}

class GoldButtonListener() extends EventHandler[ActionEvent] {
  override def handle(event: ActionEvent): Unit = {
    clicker.view.DesktopGUI.clickGold()
  }
}
