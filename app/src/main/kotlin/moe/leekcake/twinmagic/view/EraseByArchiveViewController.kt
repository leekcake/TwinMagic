package moe.leekcake.twinmagic.view

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import moe.leekcake.tweettail.desktop.view.data.status.StatusCell
import moe.leekcake.twinmagic.data.ArchiveReader
import moe.leekcake.twinmagic.data.CheckableStatus
import org.json.simple.JSONObject
import java.io.File

class EraseByArchiveViewController {
    @FXML
    lateinit var listView : ListView<CheckableStatus>

    private val statuses = ArrayList<CheckableStatus>()
    private val displayItems = FXCollections.observableArrayList<CheckableStatus>()

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
        listView.items = displayItems
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

            statuses.ensureCapacity(archiveReader.statuses.size)
            archiveReader.statuses.forEach { t, u ->
                statuses.add( CheckableStatus(u) )
            }
            statuses.sortWith(compareByDescending { it.json["id"] as Long })

            Platform.runLater {
                displayItems.addAll(statuses)
                progressLabel.isVisible = false

                statusLabel.text = "트윗 ${statuses.size}개 중 ${statuses.size} 개가 삭제 대기중입니다"
            }
        }

        reader.start()
    }
}