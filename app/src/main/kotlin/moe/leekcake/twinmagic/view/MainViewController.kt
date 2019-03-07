package moe.leekcake.twinmagic.view

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainViewController {
    fun handleOpenMergeTweetView() {
        val stage = Stage()

        val loader = FXMLLoader()
        loader.location = MainViewController::class.java.getResource("/view/MergeTweetView.fxml")
        val scene = Scene(loader.load())
        stage.title = "아카시브 합치기"
        stage.scene = scene

        stage.show()
    }

    fun handleOpenEraseByArchiveView() {
        val stage = Stage()

        val loader = FXMLLoader()
        loader.location = MainViewController::class.java.getResource("/view/EraseByArchiveView.fxml")
        val scene = Scene(loader.load())
        stage.title = "아카시브 트청기"
        stage.scene = scene

        stage.show()
    }
}