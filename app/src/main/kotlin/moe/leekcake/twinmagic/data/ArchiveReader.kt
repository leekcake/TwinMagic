package moe.leekcake.twinmagic.data

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ArchiveReader {
    interface ProgressReceiver {
        fun onArchiveReaderProgress(progress: Double, status: String)
    }

    val statuses = HashMap<Long, JSONObject>()

    var statuses_dates = ArrayList<String>()
    var statues_datefilter = HashMap<String, ArrayList<Long>>()

    companion object {
        private fun cutJsonVariableSetter(sJson: String): String {
            return sJson.substring(sJson.indexOf('=') + 1)
        }

        private val archiveFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z")
        private val twitter4jFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)

        private fun FixStatus(obj: JSONObject) {
            val stringDate = obj["created_at"] as String
            var created_at: Date? = null

            try {
                created_at = archiveFormat.parse(stringDate)
            } catch (e: ParseException) {
                try {
                    created_at = twitter4jFormat.parse(stringDate)
                } catch (e2: ParseException) {
                    throw e2
                }
            }

            obj["created_at"] = twitter4jFormat.format(created_at)
            if (obj.containsKey("entities")) {
                (obj["entities"] as JSONObject).remove("media")
            }
            if (obj.containsKey("retweeted_status")) {
                FixStatus(obj["retweeted_status"] as JSONObject)
            }
        }
    }

    fun export(outputDirectory: File) {
        //TODO: Copy archive reader (from twitter archive)

        val payload = JSONObject()
        payload["tweets"] = statuses.size
        val sdf = SimpleDateFormat("YYYY-MM-DD hh:mm:ss")
        payload["created_at"] = sdf.format(Date())
        payload["lang"] = "ko"

        val js = File(outputDirectory,"data/js")
        js.mkdirs()

        writeJson(File(outputDirectory, "data/js/payload_details.js"), payload.toJSONString(), "var payload_details = ")

        val tweet_index = JSONArray()
        val tweets = File(outputDirectory, "data/js/tweets")
        tweets.deleteRecursively()
        tweets.mkdirs()
        for (date in statuses_dates) {
            val data = JSONObject()
            data["year"] = java.lang.Long.parseLong(date.substring(0, 4))
            data["month"] = java.lang.Long.parseLong(date.substring(5, 7))
            data["file_name"] = "data/js/tweets/$date.js"
            data["var_name"] = "tweets_$date"
            data["tweet_count"] = statues_datefilter.get(date)?.size
            tweet_index.add(data)

            val tweets_json = File(tweets, "$date.js")
            val tweets_ja = JSONArray()
            for (ID in statues_datefilter.get(date)!!) {
                tweets_ja.add(statuses[ID])
            }
            writeJson(tweets_json, tweets_ja.toJSONString(), "Grailbird.data.tweets_$date = ")
        }
        writeJson(File(outputDirectory, "data/js/tweet_index.js"), tweet_index.toJSONString(), "var tweet_index = ")
    }

    private fun writeJson(json: File, jsonString: String, setter: String) {
        val fos = FileOutputStream(json)

        fos.write(setter.toByteArray())
        fos.write(jsonString.toByteArray())

        fos.close()
    }

    fun readFolder(directory: File, receiver: ProgressReceiver? = null) {
        val files = File(directory, "data/js/tweets").listFiles()!!

        for ( (i, file) in files.withIndex()) {
            receiver?.onArchiveReaderProgress(i / (files.size * 0.9), "데이터 읽는중: ${file.name}")
            readJson(file)
        }

        statuses_dates.sortWith(Collections.reverseOrder())
        for ((i, ids) in statues_datefilter.entries.withIndex()) {
            receiver?.onArchiveReaderProgress( 0.9 + (i / (statues_datefilter.size * 0.1)), "데이터 정렬중: ${ids.key}")
            ids.value.sortWith(Collections.reverseOrder())
        }

        receiver?.onArchiveReaderProgress(1.0, "완료")
    }

    private fun readJson(json: File) {
        val date = json.name.replace(".js", "")
        val sJson = cutJsonVariableSetter(json.readText())

        val parser = JSONParser()
        val statuses = parser.parse(sJson) as JSONArray

        for (obj in statuses) {
            val status = obj as JSONObject
            FixStatus(status)
            val id = status["id"] as Long

            if (!statues_datefilter.containsKey(date)) {
                statuses_dates.add(date)
                statues_datefilter[date] = ArrayList()
            }
            statues_datefilter[date]!!.add(id)

            if (!this.statuses.containsKey(id)) {
                this.statuses[id] = status
            }
        }
    }
}