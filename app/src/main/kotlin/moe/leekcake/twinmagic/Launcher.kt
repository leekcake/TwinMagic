package moe.leekcake.twinmagic

import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import moe.leekcake.twinmagic.view.MainViewController

lateinit var app: Application
    private set

fun main(args: Array<String>) {
    launch(LaunchPoint::class.java, *args)
}

class LaunchPoint : Application() {
    override fun start(stage: Stage) {
        app = this

        val loader = FXMLLoader()
        loader.location = MainViewController::class.java.getResource("/view/MainView.fxml")
        val scene = Scene(loader.load())
        stage.title = "트윈★매직"
        stage.scene = scene

        stage.show()
    }
}