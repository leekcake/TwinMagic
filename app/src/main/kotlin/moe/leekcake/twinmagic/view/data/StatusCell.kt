package moe.leekcake.tweettail.desktop.view.data.status

import java.lang.ref.WeakReference
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import moe.leekcake.twinmagic.data.CheckableStatus
import moe.leekcake.twinmagic.view.data.StatusViewController
import org.json.simple.JSONObject

class StatusCell : ListCell<CheckableStatus>() {
    private var wsvc: WeakReference<StatusViewController> = WeakReference(StatusViewController.newStatusView())

    override fun updateItem(item: CheckableStatus?, empty: Boolean) {
        super.updateItem(item, empty)
        if ( !isEmpty ) {
            val svc = wsvc.get()!!
            svc.display(item!!)
            graphic = svc.rootView
        } else {
            graphic = null
        }
    }

    companion object {
        fun newStatusCellFactory(): Callback<ListView<CheckableStatus>, ListCell<CheckableStatus>> {
            return Callback<ListView<CheckableStatus>, ListCell<CheckableStatus>> { StatusCell() }
        }
    }


}
