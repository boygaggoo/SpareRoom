package com.spareroom.android.ui.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.spareroom.android.R
import com.spareroom.android.model.DateList
import com.spareroom.android.model.DetailList
import com.spareroom.android.model.SpareRoomModel
import com.spareroom.android.model.UpcomingList
import com.spareroom.android.utils.Util
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UpcomingEventListAdapter(arrayList: ArrayList<UpcomingList>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var upcomingEvents: ArrayList<UpcomingList>? = arrayList

    fun updateUpcomingEvents(newUpcomingEvents: ArrayList<UpcomingList>?) {
        upcomingEvents!!.clear()
        upcomingEvents!!.addAll(newUpcomingEvents!!)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            UpcomingList.TYPE_GENERAL -> {
                val v1: View = inflater.inflate(
                    R.layout.upcoming_list, parent,
                    false
                )
                viewHolder = GeneralViewHolder(v1,upcomingEvents)
            }
            UpcomingList.TYPE_DATE -> {
                val v2: View = inflater.inflate(R.layout.upcoming_date_list, parent, false)
                viewHolder = DateViewHolder(v2)
            }
        }
        return viewHolder!!
    }
    override fun getItemViewType(position: Int): Int {
        return upcomingEvents!![position].type
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            UpcomingList.TYPE_GENERAL -> {
                try {
                    val generalItem = upcomingEvents!![position] as DetailList
                    val generalViewHolder = holder as GeneralViewHolder
                    generalViewHolder.bind(generalItem.upcomingModel!!)
                }catch (e: Exception){

                }

            }
            UpcomingList.TYPE_DATE -> {
                try {
                    val dateItem: DateList = upcomingEvents!![position] as DateList
                    val dateViewHolder = holder as DateViewHolder
                    dateViewHolder.dateHeader!!.text = dateItem.date
                    if (position > 0) dateViewHolder.dividerView!!.visibility = View.VISIBLE
                }catch (e: Exception){

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (upcomingEvents != null) upcomingEvents!!.size else 0
    }

    internal class DateViewHolder(v: View?) : RecyclerView.ViewHolder(
        v!!
    ) {

        val dateHeader = itemView.findViewById(R.id.date_header) as TextView

        val dividerView = itemView.findViewById(R.id.divider_view) as View

    }


    // View holder for general row item
    internal class GeneralViewHolder(v: View?,upcomingEvents:ArrayList<UpcomingList>?) : RecyclerView.ViewHolder(
        v!!
    ) {

        val eventImage = itemView.findViewById(R.id.event_image) as ImageView

        val eventVenue = itemView.findViewById(R.id.venue) as TextView

        val eventLocation = itemView.findViewById(R.id.event_location) as TextView

        val eventDate = itemView.findViewById(R.id.event_date) as TextView

        val eventTime = itemView.findViewById(R.id.event_time) as TextView

        val eventCost = itemView.findViewById(R.id.event_cost) as TextView

        val cardView = itemView.findViewById(R.id.card_view) as CardView
        fun bind(upcomingModel: SpareRoomModel) {
            if (upcomingModel.image_url != null && upcomingModel.image_url !== "" && upcomingModel.start_time != null && upcomingModel.start_time != null && upcomingModel.end_time !== "" && upcomingModel.end_time !== "") {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                var dateFormatter = SimpleDateFormat("MMMM-dd")
                val startTimeFormatter = SimpleDateFormat("h:mm")
                val endTimeFormatter = SimpleDateFormat("h:mm aaa")
                var eventDateStr: String? = ""
                var startTime = ""
                var endTime = ""
                var endDate: Date? = null
                try {
                    val startDate = dateFormat.parse(upcomingModel.start_time)
                    endDate = dateFormat.parse(upcomingModel.end_time)
                    val date = dateFormatter.format(startDate)
                    dateFormatter =
                        if (date.endsWith("01") && !date.endsWith("11")) SimpleDateFormat("d'st' MMMM") else if (date.endsWith(
                                "02"
                            ) && !date.endsWith("12")
                        ) SimpleDateFormat("d'nd' MMMM") else if (date.endsWith("03") && !date.endsWith(
                                "13"
                            )
                        ) SimpleDateFormat("d'rd' MMMM") else SimpleDateFormat("d'th' MMMM")
                    eventDateStr = dateFormatter.format(startDate)
                    startTime = startTimeFormatter.format(startDate)
                    endTime = endTimeFormatter.format(endDate)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                eventVenue!!.text= upcomingModel.venue
                eventLocation!!.text= upcomingModel.location
                eventDate!!.text = eventDateStr
                eventTime!!.text = "$startTime - $endTime"
                eventCost!!.text= upcomingModel.cost
                Util.loadImage(
                    eventImage!!, upcomingModel.image_url, Util.getProgressDrawable(
                        eventImage!!.context
                    )
                )
            }
            try {
                cardView!!.setOnClickListener { v ->
                    val callPermissionCheck =
                        ContextCompat.checkSelfPermission(v.context, Manifest.permission.CALL_PHONE)
                    if (callPermissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            (v.context as Activity),
                            arrayOf(Manifest.permission.CALL_PHONE),
                            0
                        )
                    } else {
                        val phoneNo: String = upcomingModel.phone_number
                        if (phoneNo != null) {
                            val intent = Intent(Intent.ACTION_CALL)
                            intent.data = Uri.parse("tel:$phoneNo")
                            v.context.startActivity(intent)
                        }
                    }
                }
            }catch (e: Exception){

            }
        }

    }
}



