package moe.leekcake.twinmagic.view

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import moe.leekcake.twinmagic.data.ArchiveReader
import moe.leekcake.twinmagic.data.CheckableStatus
import moe.leekcake.twinmagic.view.data.StatusCell
import org.json.simple.JSONObject
import java.io.File

class EraseByArchiveViewController : StatusCell.StatusChangeListener {
    override fun onCheckedStatusRemove(status: CheckableStatus) {
        removeTarget.remove(status.id)
        updateDeleteStatus()
    }

    override fun onCheckedStatusAdd(status: CheckableStatus) {
        removeTarget[status.id] = status
        updateDeleteStatus()
    }

    @FXML
    lateinit var listView : ListView<CheckableStatus>

    private val statuses = ArrayList<CheckableStatus>()
    private val displayItems = FXCollections.observableArrayList<CheckableStatus>()

    private val removeTarget = HashMap<Long, CheckableStatus>()

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

    fun updateDeleteStatus() {
        Platform.runLater {
            statusLabel.text = "트윗 ${statuses.size}개 중 ${removeTarget.size} 개가 삭제 대기중입니다"
        }
    }

    @FXML
    private fun initialize() {
        listView.cellFactory = StatusCell.newStatusCellFactory(this)
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
                val status = CheckableStatus(u)
                statuses.add( status )
                removeTarget[status.id] = status
            }
            statuses.sortWith(compareByDescending { it.id })

            Platform.runLater {
                displayItems.addAll(statuses)
                progressLabel.isVisible = false

                statusLabel.text = "트윗 ${statuses.size}개 중 ${statuses.size} 개가 삭제 대기중입니다"
            }
        }

        reader.start()
    }
}