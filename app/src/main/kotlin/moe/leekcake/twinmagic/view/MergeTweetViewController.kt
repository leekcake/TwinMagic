package moe.leekcake.twinmagic.view

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.ProgressBar
import javafx.stage.DirectoryChooser
import moe.leekcake.twinmagic.data.ArchiveReader
import java.io.File
import java.io.FileOutputStream


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
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "아카시브 폴더 선택"
        val directory = directoryChooser.showDialog( addArchiveButton.scene.window ) ?: return

        archiveListViewItems.add(directory.path)
    }

    fun handleMergeArchiveButton() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "합친 아카시브가 저장될 폴더 선택"
        val directory = directoryChooser.showDialog( addArchiveButton.scene.window ) ?: return

        val flag = File(directory, "combined.bin")

        if(directory.list().isNotEmpty()) {
            if( !flag.exists() ) {
                //TODO: Warning for Not empty folder without flag
                return
            }
        }

        val archiveReader = ArchiveReader()

        for(path in archiveListViewItems) {
            archiveReader.readFolder( File(path) )
        }

        archiveReader.export(directory)
        //Generate flag(empty) file
        FileOutputStream(flag).close()
    }
}