package moe.leekcake.twinmagic.data

import org.json.simple.JSONObject

data class CheckableStatus(val json: JSONObject, var check: Boolean = true)