package moe.leekcake.twinmagic.view.data

import java.lang.ref.WeakReference
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import moe.leekcake.twinmagic.data.CheckableStatus
import moe.leekcake.twinmagic.view.data.StatusViewController
import org.json.simple.JSONObject

class StatusCell(listener: StatusChangeListener) : ListCell<CheckableStatus>() {
    interface StatusChangeListener {
        fun onCheckedStatusAdd(status: CheckableStatus)
        fun onCheckedStatusRemove(status: CheckableStatus)
    }

    private var svc: StatusViewController = StatusViewController.newStatusView()

    init {
        svc.listener = listener
    }

    override fun updateItem(item: CheckableStatus?, empty: Boolean) {
        super.updateItem(item, empty)
        if ( !isEmpty ) {
            svc.display(item!!)
            graphic = svc.rootView
        } else {
            graphic = null
        }
    }

    companion object {
        fun newStatusCellFactory(listener: StatusChangeListener): Callback<ListView<CheckableStatus>, ListCell<CheckableStatus>> {
            return Callback { StatusCell(listener) }
        }
    }
}
