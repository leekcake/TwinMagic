package moe.leekcake.twinmagic.view.data

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.json.simple.JSONObject

class StatusViewController {
    companion object {
        fun newStatusView(): StatusViewController {
            val loader = FXMLLoader()
            loader.location = StatusViewController::class.java.getResource("/view/data/StatusView.fxml")
            loader.load<Any>()
            return loader.getController()
        }
    }

    @FXML
    lateinit var rootView: VBox

    @FXML
    lateinit var headView: HBox

    @FXML
    lateinit var headIconImageView: ImageView
    @FXML
    lateinit var headTextLabel: Label

    @FXML
    lateinit var userLabel: Label

    @FXML
    lateinit var createdDateLabel: Label

    @FXML
    lateinit var textLabel: Label

    fun display(statusJson: JSONObject) {
        val display: JSONObject

        if(statusJson.containsKey("retweeted_status")) {
            display = statusJson["retweeted_status"] as JSONObject
            headView.isVisible = true

            headTextLabel.text = (statusJson["user"] as JSONObject)["name"].toString() + " 님이 리트윗 했습니다"
        } else {
            headView.isVisible = false
            display = statusJson
        }

        val user = display["user"] as JSONObject
        userLabel.text = user["name"].toString() + " @" + user["screen_name"].toString()
        textLabel.text = display["text"].toString()
        createdDateLabel.text = display["created_at"].toString()
    }
}