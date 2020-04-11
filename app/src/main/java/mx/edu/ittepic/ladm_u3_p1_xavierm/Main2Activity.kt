package mx.edu.ittepic.ladm_u3_p1_xavierm

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var nombreBD = "TAREAS"
    var posicion=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        cargarCombo()
        cargarLista()

        btnBuscarA.setOnClickListener {
            when (posicion) {
                0 -> {
                    cargarLista()
                }
                1->{
                    cargarListaID()
                }
                2->{
                    cargarListaDescripcion()
                }
                3->{
                    cargarListaFechaCaptura()
                }
                4->{
                    cargarListaFechaEntrega()
                }
            }
        }
    }

    fun cargarListaID(){
        var baseDatos = BaseDatos(this, nombreBD, null, 1)
        var select = baseDatos.readableDatabase
        var columnas = arrayOf("*")
        var idBuscar = arrayOf(txtBuscar.text.toString())
        var cursor = select.query("ACTIVIDADES", columnas, "ID_Actividad = ?", idBuscar, null, null, null)
        listaID = ArrayList<String>()

        if(cursor.moveToFirst()) {
            var arreglo = ArrayList<String>()
            var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
            arreglo.add(data)
            listaID.add(cursor.getString(0))
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
            lista.setOnItemClickListener { parent, view, position, id ->
                llamarOtroIntent(listaID[position])
            }
        }
    }

    fun cargarListaDescripcion(){
        var baseDatos = BaseDatos(this, nombreBD, null, 1)
        var select = baseDatos.readableDatabase
        var columnas = arrayOf("*")
        var descripcion = arrayOf(txtBuscar.text.toString())
        var cursor = select.query("ACTIVIDADES", columnas, "Descripcion = ?", descripcion, null, null, null)
        listaID = ArrayList<String>()

        if(cursor.moveToFirst()) {
            var arreglo = ArrayList<String>()
            var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
            arreglo.add(data)
            listaID.add(cursor.getString(0))
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
            lista.setOnItemClickListener { parent, view, position, id ->
                llamarOtroIntent(listaID[position])
            }
        }
    }

    fun cargarListaFechaCaptura(){
        var baseDatos = BaseDatos(this, nombreBD, null, 1)
        var select = baseDatos.readableDatabase
        var columnas = arrayOf("*")
        var fechaCaptura = arrayOf(txtBuscar.text.toString())
        var cursor = select.query("ACTIVIDADES", columnas, "FechaCaptura = ?", fechaCaptura, null, null, null)
        listaID = ArrayList<String>()

        if(cursor.moveToFirst()) {
            var arreglo = ArrayList<String>()
            var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
            arreglo.add(data)
            listaID.add(cursor.getString(0))
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
            lista.setOnItemClickListener { parent, view, position, id ->
                llamarOtroIntent(listaID[position])
            }
        }
    }

    fun cargarListaFechaEntrega(){
        var baseDatos = BaseDatos(this, nombreBD, null, 1)
        var select = baseDatos.readableDatabase
        var columnas = arrayOf("*")
        var fechaEntrega = arrayOf(txtBuscar.text.toString())
        var cursor = select.query("ACTIVIDADES", columnas, "FechaEntrega = ?", fechaEntrega, null, null, null)
        listaID = ArrayList<String>()

        if(cursor.moveToFirst()) {
            var arreglo = ArrayList<String>()
            var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)} \nFecha captura: ${cursor.getString(2)}"
            arreglo.add(data)
            listaID.add(cursor.getString(0))
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
            lista.setOnItemClickListener { parent, view, position, id ->
                llamarOtroIntent(listaID[position])
            }
        }
    }

    fun cargarCombo() {
        val seleccion = resources.getStringArray(R.array.seleccion)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, seleccion
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long) {
                    posicion=position
                    when (position) {
                        0 -> {
                            txtBuscar.setText("")
                        }
                        1->{
                            txtBuscar.setText("")
                        }
                        2->{
                            txtBuscar.setText("")
                        }
                        3->{
                            txtBuscar.setText("")
                        }
                        4->{
                            txtBuscar.setText("")
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
    }

    fun cargarLista() {
        try {
            var baseDatos = BaseDatos(this, nombreBD, null, 1)
            var select = baseDatos.readableDatabase
            var SQL = "SELECT * FROM ACTIVIDADES"
            var cursor = select.rawQuery(SQL, null)
            listaID = ArrayList<String>()
            if(cursor.count > 0) {
                var arreglo = ArrayList<String>()
                this.listaID = ArrayList<String>()
                cursor.moveToFirst()
                var cantidad = cursor.count-1

                (0..cantidad).forEach {
                    var data = "ID: ${cursor.getString(0)}\nDescripcion: ${cursor.getString(1)}\nFecha captura: ${cursor.getString(2)}\nFecha entrega: ${cursor.getString(3)}"
                    arreglo.add(data)
                    listaID.add(cursor.getString(0))
                    cursor.moveToNext()
                }

                lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arreglo)
                lista.setOnItemClickListener { parent, view, position, id ->
                    llamarOtroIntent(listaID[position])
                }
            }

            select.close()
            baseDatos.close()
        } catch (error : SQLiteException){
            mensaje(error.message.toString())
        }
    }

    fun llamarOtroIntent(id : String) {
        var otroActivity = Intent(this, Main3Activity :: class.java)
        otroActivity.putExtra("id", id)
        startActivity(otroActivity)
    }

    fun mensaje(s : String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK") {d , i -> }
            .show()
    }
}
