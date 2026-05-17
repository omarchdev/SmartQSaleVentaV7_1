package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.omarchdev.smartqsale.smartqsaleventas.R
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*

class DialogScannerCam : DialogFragment(), ZXingScannerView.ResultHandler {
    private var scanner: ZXingScannerView? = null
    private var scannerResult: ScannerResult? = null

    fun setScannerResult(scannerResult: ScannerResult?) {
        this.scannerResult = scannerResult
    }

    fun interface ScannerResult {
        fun ResultadoScanner(resultText: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val barcodeFormatList: MutableList<BarcodeFormat> = ArrayList()
        barcodeFormatList.add(BarcodeFormat.EAN_13)
        barcodeFormatList.add(BarcodeFormat.QR_CODE)
        barcodeFormatList.add(BarcodeFormat.CODABAR)
        scanner = ZXingScannerView(activity)
        scanner!!.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        //scanner.setFormats(new ArrayList<BarcodeFormat>());
        scanner!!.setActivated(true)
        scanner!!.setResultHandler(this)
        scanner!!.startCamera()
        scanner!!.setAutoFocus(true)
        scanner!!.setAspectTolerance(0.5f)
        builder.setView(scanner)
        val dialog = builder.create()
        dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        return dialog
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        scanner!!.stopCamera()
    }

    override fun handleResult(result: Result) {
        scannerResult?.ResultadoScanner(result.text.toString())
        this.dialog?.dismiss()
    }
}
