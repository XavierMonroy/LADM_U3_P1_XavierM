package mx.edu.ittepic.ladm_u3_p1_xavierm

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main3.*
import java.lang.Exception

class Main3Activity : AppCompatActivity() {
    var nombreBD = "TAREAS"
    var idFoto = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extras = intent.extras
        var idEliminar = extras?.getString("id").toString()

        cargarRegistro(idEliminar)

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("ELIMINAR")
                .setMessage("¿Desea eliminar la actividad?")
                .setPositiveButton("SI"){d,i->
                    eliminarRegistro(idEliminar)
                    var otroActivity = Intent(this, Main2Activity :: class.java)
                    startActivity(otroActivity)
                }
                .setNegativeButton("NO", DialogInterface.OnClickListener {
                        dialog, id ->
                })
                .show()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    fun cargarRegistro(id : String) {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES WHERE Id_Actividad = ?"
            var parametros = arrayOf(id)
            var cursor = select.rawQuery(SQL, parametros)

            if(cursor.moveToFirst()){
                //SI HAY RESULTADO
                lblID.setText("ID:" + cursor.getString(0))
                lblDes.setText("Descripcion: " + cursor.getString(1))
                lblFC.setText("Fecha Captura: " + cursor.getString(2))
                lblFE.setText("Fecha Entrega: " + cursor.getString(3))
            } else {
                //NO HAY RESULTADO
                mensaje("NO SE ENCONTRO COINCIDENCIA")
            }
            select.close()
            baseDatos.close()

            recuperarImg(id)

        } catch (error : SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun recuperarImg(id : String) {
        var nulo : ByteArray? = null
        var evidencia = Evidencias(nulo)
        evidencia!!.asignarPuntero(this)
        var imagenes = evidencia.buscarImagen(id)
        var img = ArrayList<ImageView>()

        try {
            (0..imagenes.size-1).forEach {
                var imgNew = ImageView(this)
                imgNew.adjustViewBounds = true
                val bitmap = Utilerias.getImage(imagenes[it])
                imgNew.setImageBitmap(bitmap)
                var param : LinearLayout.LayoutParams = LinearLayout.LayoutParams(500, 500)
                imgNew.layoutParams = param
                imagen.addView(imgNew)
            }
        } catch (error : Exception){
        }
    }
    fun eliminarRegistro(id : String) {
        try {
            var base = BaseDatos(this, nombreBD, null, 1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())
            var respuesta = eliminar.delete("EVIDENCIAS", "ID_Actividad = ?", idEliminar)

            if(respuesta.toInt() == 0) {
                mensaje("NO SE HA ELIMINADO")
            }

            var respuesta2 = eliminar.delete("ACTIVIDADES", "ID_Actividad = ?", idEliminar)

            if(respuesta2.toInt() == 0) {
                mensaje("NO SE HA ELIMINADO")
            }
        } catch (e : SQLiteException) {
            mensaje(e.message.toString())
        }
    }

    fun mensaje(s : String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK") {d , i -> }
            .show()
    }
}
