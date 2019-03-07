package moe.leekcake.twinmagic.view

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.stage.DirectoryChooser
import moe.leekcake.tweettail.desktop.view.data.status.StatusCell
import moe.leekcake.twinmagic.data.ArchiveReader
import org.json.simple.JSONObject
import java.io.File

class EraseByArchiveViewController {
    @FXML
    lateinit var listView : ListView<JSONObject>
    private val listViewItems = FXCollections.observableArrayList<JSONObject>()

    @FXML
    private fun initialize() {
        listView.cellFactory = StatusCell.newStatusCellFactory()
        listView.items = listViewItems
    }

    fun setArchiveDirectory(directory: File) {
        val archiveReader = ArchiveReader()
        archiveReader.readFolder(directory)

        val items = ArrayList<JSONObject>(archiveReader.statuses.size)
        items.addAll( archiveReader.statuses.values )

        items.sortWith(compareBy { it["id"] as Long })

        listViewItems.addAll(items)
    }
}