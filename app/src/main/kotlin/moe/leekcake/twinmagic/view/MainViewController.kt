package moe.leekcake.twinmagic.view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage

class MainViewController {
    @FXML
    lateinit var rootView: GridPane

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
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "지울 트윗을 불러올 아카시브를 선택하세요"
        val directory = directoryChooser.showDialog( rootView.scene.window ) ?: return

        val stage = Stage()

        val loader = FXMLLoader()
        loader.location = MainViewController::class.java.getResource("/view/EraseByArchiveView.fxml")
        val pane = loader.load() as Pane
        val scene = Scene(pane)

        loader.getController<EraseByArchiveViewController>().setArchiveDirectory(directory)

        stage.title = "아카시브 트청기"
        stage.scene = scene

        stage.show()
    }
}