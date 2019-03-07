package moe.leekcake.twinmagic.view

import javafx.application.Platform
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

    //For remember last opened directory
    val directoryChooser = DirectoryChooser()


    private var mergeThread: Thread? = null

    val mergeText = "목록에 있는 아카시브를 합치기"
    val mergeCancelText = "취소"

    @FXML
    private fun initialize() {
        directoryChooser.title = "아카시브 폴더 선택"

        archiveListView.items = archiveListViewItems
    }

    fun handleAddArchiveButton() {
        val directory = directoryChooser.showDialog( addArchiveButton.scene.window ) ?: return

        directoryChooser.initialDirectory = directory.parentFile
        archiveListViewItems.add(directory.path)
    }

    fun updateProgress(progress: Int, max: Int, text: String) {
        Platform.runLater {
            progressBar.progress = progress / (max * 1.0)
            progressLabel.text = text
        }
    }

    fun handleMergeArchiveButton() {
        val rollback = Runnable {
            mergeThread?.interrupt()
            Platform.runLater {
                addArchiveButton.isDisable = false
                mergeArchiveButton.text = mergeText
            }
        }

        if(mergeThread != null) {
            rollback.run()
            return
        }

        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "합친 아카시브가 저장될 폴더 선택"
        val directory = directoryChooser.showDialog( addArchiveButton.scene.window ) ?: return

        addArchiveButton.isDisable = true
        mergeArchiveButton.text = mergeCancelText

        mergeThread = Thread {
            val flag = File(directory, "combined.bin")

            if(directory.list().isNotEmpty()) {
                if( !flag.exists() ) {
                    //TODO: Warning for Not empty folder without flag
                    return@Thread
                }
            }

            val archiveReader = ArchiveReader()

            val maxProgress = archiveListViewItems.size
            var progress = 0

            for(path in archiveListViewItems) {
                if(Thread.interrupted()) {
                    return@Thread
                }
                val file = File(path)
                updateProgress(progress, maxProgress, "읽는중: ${file.name}")
                archiveReader.readFolder( file )
                progress++
            }

            updateProgress(progress, maxProgress, "병합 및 내보내기...")
            archiveReader.export(directory)
            progress++
            updateProgress(progress, maxProgress, "완료")
            //Generate flag(empty) file
            FileOutputStream(flag).close()

            mergeThread = null
            rollback.run()
        }
        mergeThread!!.start()
    }
}