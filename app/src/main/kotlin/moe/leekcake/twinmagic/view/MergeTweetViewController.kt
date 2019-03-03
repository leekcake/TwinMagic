package moe.leekcake.twinmagic.view

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.ProgressBar
import javafx.stage.FileChooser

class MergeTweetViewController {
    @FXML
    lateinit var addArchiveButton: Button

    @FXML
    lateinit var mergeArchiveButton: Button

    @FXML
    lateinit var archiveListView: ListView<String>

    private val archiveListViewItems = FXCollections.observableArrayList<String>()

    @FXML
    lateinit var progressBar: ProgressBar

    @FXML
    lateinit var progressLabel: Label

    @FXML
    private fun initialize() {
        archiveListView.items = archiveListViewItems
    }

    fun handleAddArchiveButton() {
        val fileChooser = FileChooser()
        fileChooser.title = "아카시브 파일 선택"
        val file = fileChooser.showOpenDialog( addArchiveButton.scene.window ) ?: return

        archiveListViewItems.add(file.path)
    }
}