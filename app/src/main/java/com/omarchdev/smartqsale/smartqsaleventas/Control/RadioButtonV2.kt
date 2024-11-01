package com.omarchdev.smartqsale.smartqsaleventas.Control
import android.content.Context
import android.text.Html
import android.util.AttributeSet
import com.omarchdev.smartqsale.smartqsaleventas.R

class RadioButtonV2:androidx.appcompat.widget.AppCompatRadioButton {

    var sTitle=""
    var sSubtitle=""

    constructor(context: Context,attrs:AttributeSet):super(context,attrs){
            context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.RadioButtonV2,
                    0, 0).apply {

                try {
                    sTitle = getString(R.styleable.RadioButtonV2_title).toString()
                    sSubtitle = getString(R.styleable.RadioButtonV2_subtitle).toString()
                    text=Html.fromHtml("<b><big>" + sTitle+ "</big></b>" +
                            "<br />" +
                            "<font>" + sSubtitle + "</font>" + "<br />")
                } finally {
                    recycle()
                }
        }
    }
    constructor(context: Context):super(context)

    fun setSubtitle(subtitle:String){
        sSubtitle=subtitle
        invalidate()
        requestLayout()
    }

    override fun getText(): CharSequence {
        return super.getText()
    }

    fun SetTitle(title:String){
        sTitle=title
        invalidate()
        requestLayout()
    }

    fun textButton(title:String,subtitle:String):String{
        var r=""
        if(!subtitle.isNullOrBlank()){
            setText(Html.fromHtml("<b><big>       " + title+ "</big></b>" +
                    "<br />" +
                    "<font>          " + subtitle + "</font>" + "<br />"))
        }else{
            setText(title)
        }
        return r
    }

    fun cabecera(){
    }
}