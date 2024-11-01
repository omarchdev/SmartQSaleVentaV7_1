package com.omarchdev.smartqsale.smartqsaleventas.Model

class RptaSunat(){

    var numeroSerie:(Int,String)->String={a,b->"$a ,$b" }
    var tipoComprobante:Int=0
    var serie:String=""
    var numero:Int=0
    var enlace:String?=""
    var aceptada_sunat:Boolean=false
    var sunat_descripcion:String?=""
    var sunat_nota:String?=""
    var sunat_responsecode:String?=""
    var sunat_soap_error:String?=""
    var pdf_zip_base64:String?=""
    var xml_zip_base64:String?=""
    var cdr_zip_base64:String?=""
    var cadena_codigo_qr:String?=""
    var codigo_hash:String?=""
    var codigo_barras:String?=""
    var codError:String?=""
    var mensajeError:String?=""

}


