package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.omarchdev.smartqsale.smartqsaleventas.Model.TimeData
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_date_time_picker.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class DateTimePicker : BottomSheetDialogFragment() {


    interface ChangeDateTimePicker : Serializable {
        fun ResultDateTimePicker(timeData: TimeData)
    }

    private var changeDateTimePicker: ChangeDateTimePicker? = null
    private var param1: String? = null
    private var param2: String? = null
    private var textDate = ""
    private var textTime = ""
    private var textoInformacion1=""
    private var timeData = TimeData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            changeDateTimePicker = it.getSerializable("INTERFACE") as ChangeDateTimePicker
            timeData = it.getSerializable("TIME") as TimeData
            textoInformacion1= it.getString("INFO").toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_time_picker, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        calendarSelect.visibility = View.VISIBLE
        timeSelect.visibility = View.GONE
        btnSalir.setOnClickListener {
            dialog?.dismiss()
        }
        btnGuardar.setOnClickListener {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                timeData.minute = timeSelect.minute
                timeData.hour = timeSelect.hour
            } else {
                timeData.minute = timeSelect.currentMinute
                timeData.hour = timeSelect.currentHour
            }







            changeDateTimePicker?.ResultDateTimePicker(timeData)
            dialog?.dismiss()
        }
        tabDatePicker.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0!!.position) {
                    0 -> {
                        calendarSelect.visibility = View.VISIBLE
                        timeSelect.visibility = View.GONE
                    }
                    1 -> {
                        calendarSelect.visibility = View.GONE
                        timeSelect.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                when (p0!!.position) {
                    0 -> {
                        calendarSelect.visibility = View.VISIBLE
                        timeSelect.visibility = View.GONE
                    }
                    1 -> {
                        calendarSelect.visibility = View.GONE
                        timeSelect.visibility = View.GONE
                    }
                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        }
        )
        timeSelect.layoutMode = 4
        tabDatePicker.setScrollPosition(0, 0f, true)

        timeSelect.setOnTimeChangedListener { view, hourOfDay, minute ->
            val sdf = SimpleDateFormat("hh:mm a")
            val calendar = Calendar.getInstance()
            calendar.set(0, 0, 0, hourOfDay, minute, 0)
            val timeMilis = calendar.timeInMillis
            val date = Date()
            date.time = timeMilis
            textTime = sdf.format(date)
            textTime(textDate, textTime)
            timeData.hour = hourOfDay
            timeData.minute = minute


        }

        if (timeData.year != 0) {

            calendarSelect.init(timeData.year,timeData.month,timeData.day) { view, year, month, dayOfMonth ->
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                val timeMilis = calendar.timeInMillis
                val date = Date()
                date.time = timeMilis
                textDate = sdf.format(date)
                textTime(textDate, textTime)

                timeData.year = year
                timeData.month = month
                timeData.day = dayOfMonth



            }
  /*          calendarSelect.date(timeData.datemilis)
*/
            val calTemp = Calendar.getInstance()
            calTemp.timeInMillis = timeData.datemilis


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                timeSelect.hour = calTemp.get(Calendar.HOUR_OF_DAY)
                timeSelect.minute = calTemp.get(Calendar.MINUTE)
            }
            val sdf1 = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            val sdf2 = SimpleDateFormat("dd/MM/yyyy")
            val sdf3 = SimpleDateFormat("hh:mm a")
            val date = Date()
            date.time = timeData.datemilis
            textDate=sdf2.format(date)
            textTime=sdf3.format(date)
            val finaltext=textoInformacion1+"\n"+" "+sdf1.format(date)
            txtDateTime.text = finaltext
        }

        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val coordinatorLayout = bottomSheet!!.parent as CoordinatorLayout
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            bottomSheetBehavior.peekHeight = bottomSheet!!.height
            coordinatorLayout.parent.requestLayout()
            bottomSheetBehavior.isHideable=false
        }


    }

    fun textTime(dateText: String, timeText: String) {
        val textFinal =textoInformacion1+"\n"+ dateText + " " + timeText
        txtDateTime.text = textFinal
    }

    companion object {
        @JvmStatic
        fun newInstance(timeData: TimeData, textoInformacion1:String,changeDateTimePicker: ChangeDateTimePicker) =
                DateTimePicker().apply {
                    arguments = Bundle().apply {
                        putSerializable("INTERFACE", changeDateTimePicker)
                        putSerializable("TIME", timeData)
                        putString("INFO",textoInformacion1)
                        /*putString("ARG_PARAM1", param1)
                        putString("ARG_PARAM2", param2)*/
                    }
                }
    }
}