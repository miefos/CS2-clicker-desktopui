package clicker.controller

import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.control.TextField

class NewEquipmentButtonListener (id: String) extends EventHandler[ActionEvent] {
  override def handle(event: ActionEvent): Unit = {
    clicker.view.DesktopGUI.buyEquipment(id)
  }
}
