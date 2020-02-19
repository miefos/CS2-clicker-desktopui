package clicker.view

import clicker.controller.{GoldButtonListener, NewEquipmentButtonListener}
import play.api.libs.json.{JsValue, Json}
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import javafx.application.Platform
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextArea, TextField}
import scalafx.scene.layout.{HBox, VBox}

class HandleMessagesFromServer() extends Emitter.Listener {
  override def call(objects: Object*): Unit = {

    // Use runLater when interacting with the GUI
    Platform.runLater(() => {
      // This method is called whenever a new game state is received from the server
      val jsonGameState = objects.apply(0).toString
//      println(jsonGameState)

      val parsed: JsValue = Json.parse(jsonGameState)

      val username: String = (parsed \ "username").as[String]
      val gold: Double = (parsed \ "gold").as[Double]
      val equipment: String = (parsed \ "equipment").as[String]
      val parsedEquipment: JsValue = Json.parse(jsonGameState)
      val shovel: String = (parsed \ "shovel").as[String]
//      println(username)
//      println(gold)
      println(shovel)
      // TODO: Display the game state on your GUI
      // You must display: current gold, and the name, number owned, and cost for each type of equipment

      // You can access any variables/methods in the DesktopGUI object from this class
      // ex. DesktopGUI.goldTextField.text = goldFromGameState

    })

  }
}


object DesktopGUI extends JFXApp {

  var socket: Socket = IO.socket("https://tictactoe.info/")
  socket.on("gameState", new HandleMessagesFromServer)
  socket.connect()

  // Change "test" to any username you'd like to start a new game
  socket.emit("register", "test")

  // Call this method whenever the user clicks your gold button
  def clickGold(): Unit = {
    println("Gold clicked!")
    socket.emit("clickGold")
  }

  // Call this method whenever the user clicks to purchase equipment
  // The parameter is the id of the equipment type to purchase
  def buyEquipment(equipmentId: String): Unit = {
    println("Equipment " + equipmentId + " bought!")
    socket.emit("buy", equipmentId)
  }

  // TODO: Setup your GUI
  // You may create and place all GUI elements (TextFields, Buttons, etc) then only change the text on
  // each element when a new game state is received
  // You may assume that there will be exactly 3 types of equipment with ids of "shovel", "excavator", and "mine"
  val goldButton: Button = new Button {
    minWidth = 100
    minHeight = 100
    style = "-fx-font: 28 ariel;"
    text = "Gold"
    onAction = new GoldButtonListener()
  }

  val buyShovel: Button = new Button {
    minWidth = 100
    minHeight = 100
    style = "-fx-font: 28 ariel;"
    text = "Buy Shovel"
    onAction = new NewEquipmentButtonListener("shovel")
  }

  val buyExcavator: Button = new Button {
    minWidth = 100
    minHeight = 100
    style = "-fx-font: 28 ariel;"
    text = "Buy excavator"
    onAction = new NewEquipmentButtonListener("excavator")
  }

  val buyMine: Button = new Button {
    minWidth = 100
    minHeight = 100
    style = "-fx-font: 28 ariel;"
    text = "Buy Mine"
    onAction = new NewEquipmentButtonListener("mine")
  }

  val StatsDisplay: TextArea = new TextArea {
    editable = false
    style = "-fx-font: 18 ariel;"
    text = "Current Gold: <current gold> \n" +
          "Equipment1: shovel (owned: <num shovel owned>) \n" +
          "Equipment2: excavator (owned <num excavator owned>) \n" +
          "Equipment3: mine (owned <num mine owned>) \n" +
          "Price for shovel: <price for shovel> \n" +
          "Price for excavator: <price for excavator> \n" +
          "Price for mine: <price for mine> \n"
  }
  val verticalBox: VBox = new VBox(){
    children = List(StatsDisplay, goldButton, buyShovel, buyExcavator, buyMine)
  }

  this.stage = new PrimaryStage {
    title = "CSE Clicker"
    scene = new Scene() {
      content = List(
        verticalBox
      )
    }

  }

}
