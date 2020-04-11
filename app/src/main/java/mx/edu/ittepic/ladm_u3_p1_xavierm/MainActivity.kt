package mx.edu.ittepic.ladm_u3_p1_xavierm

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var nombreBD = "TAREAS"
    var imagenes = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnImagen.setOnClickListener {
            cargarImagen()
        }

        btnInsertarA.setOnClickListener {
            insertarActividad()
        }

        btnBuscar.setOnClickListener {
            var otroActivity = Intent(this, Main2Activity :: class.java)
            startActivity(otroActivity)
        }

        layout_Img.setOnClickListener {
            cargarImagen()
        }
    }

    fun cargarImagen() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 10 && resultCode == Activity.RESULT_OK && data != null){
            var path = data.data
            var imgNew = ImageView(this)
            imgNew.setImageURI(path)
            imgNew.adjustViewBounds = true
            var param : LinearLayout.LayoutParams = LinearLayout.LayoutParams(500, 500)
            imgNew.layoutParams = param
            layout_Img.addView(imgNew)

            imagenes.add(imgNew)
            layout_Img.background = null
        }
    }

    fun insertarActividad() {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var insertar = baseDatos.writableDatabase
            var SQL = "INSERT INTO ACTIVIDADES VALUES(NULL, '${txtDescripcion.text.toString()}', '${txtCaptura.text.toString()}', '${txtEntrega.text.toString()}')"

            insertar.execSQL(SQL)
            insertar.close()
            baseDatos.close()

            insertarEvidencia()
        } catch (error : SQLiteException) {
            mensaje(error.message.toString())
        }
    }

    fun insertarEvidencia() {
        try {
            (0..imagenes.size-1).forEach{
                val bitmap = (imagenes.get(it).drawable as BitmapDrawable).bitmap
                var evidencia = Evidencias(Utilerias.getBytes(bitmap))
                evidencia.asignarPuntero(this)
                evidencia.idActividad = ultimoID()
                var resultado = evidencia.insertarImagen()

                if(resultado == true){
                    mensaje("Se insertó correctamente")
                }else{
                    mensaje("ERROR")
                }
            }
            limpiarCampos()

        } catch (error : SQLiteException) {
            mensaje(error.message.toString())
        }
    }

    fun ultimoID() : String{
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var columnas = arrayOf("ID_Actividad")

            var cursor = select.query("ACTIVIDADES", columnas, null, null,null,null,null)

            if(cursor.moveToLast()) {
                return cursor.getString(0)
            }

            select.close()
            baseDatos.close()
        } catch (error : SQLiteException){
            mensaje(error.message.toString())
        }
        return ""
    }

    fun limpiarCampos() {
        txtEntrega.setText("")
        txtCaptura.setText("")
        txtDescripcion.setText("")
        layout_Img.removeAllViews()
        imagenes = ArrayList<ImageView>()
        layout_Img.background = getDrawable(R.drawable.ic_add_a_photo_black_24dp)
    }

    fun mensaje(s : String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK") {d , i -> }
            .show()
    }
}
