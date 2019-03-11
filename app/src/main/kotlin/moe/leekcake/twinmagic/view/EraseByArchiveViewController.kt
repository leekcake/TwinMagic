package moe.leekcake.twinmagic.view

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import moe.leekcake.tweettail.desktop.view.data.status.StatusCell
import moe.leekcake.twinmagic.data.ArchiveReader
import org.json.simple.JSONObject
import java.io.File

class EraseByArchiveViewController {
    @FXML
    lateinit var listView : ListView<JSONObject>
    private val listViewItems = FXCollections.observableArrayList<JSONObject>()

    @FXML
    lateinit var progressLabel : Label
    @FXML
    lateinit var statusLabel : Label

    @FXML
    lateinit var searchTextField : TextField
    @FXML
    lateinit var searchButton : Button

    @FXML
    lateinit var removeProgressBar : ProgressBar

    @FXML
    lateinit var removeProgressLabel : Label

    @FXML
    private fun initialize() {
        listView.cellFactory = StatusCell.newStatusCellFactory()
        listView.items = listViewItems
    }

    fun setArchiveDirectory(directory: File) {
        val reader = Thread {
            val archiveReader = ArchiveReader()
            archiveReader.readFolder(directory, object: ArchiveReader.ProgressReceiver {
                override fun onArchiveReaderProgress(progress: Double, status: String) {
                    Platform.runLater {
                        progressLabel.text = "$status (${progress * 100}%)"
                    }
                }
            })

            val items = ArrayList<JSONObject>(archiveReader.statuses.size)
            items.addAll( archiveReader.statuses.values )

            items.sortWith(compareByDescending { it["id"] as Long })

            Platform.runLater {
                listViewItems.addAll(items)
                progressLabel.isVisible = false

                statusLabel.text = "트윗 ${items.size}개 중 ${items.size} 개가 삭제 대기중입니다"
            }
        }

        reader.start()
    }
}