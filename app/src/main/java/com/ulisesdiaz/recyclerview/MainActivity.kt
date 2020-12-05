package com.ulisesdiaz.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ulisesdiaz.ClickListener
import com.ulisesdiaz.LongClickListener
import com.ulisesdiaz.recyclerview.adapters.AdaptadorCustom
import com.ulisesdiaz.recyclerview.models.Platillo

class MainActivity : AppCompatActivity() {

    var listaRecycler: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var isActionMode = false
    var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listaRecycler = findViewById<RecyclerView>(R.id.recycler)
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        listaRecycler?.setHasFixedSize(true)

        val platillos = ArrayList<Platillo>()
        platillos.add(Platillo("Platillo1", 230.6, 0.5F, R.drawable.platillo01))
        platillos.add(Platillo("Platillo2", 110.5, 1.20F, R.drawable.platillo02))
        platillos.add(Platillo("Platillo3", 320.4, 3.60F, R.drawable.platillo03))
        platillos.add(Platillo("Platillo4", 310.3, 3.5F, R.drawable.platillo04))
        platillos.add(Platillo("Platillo5", 420.2, 4.5F, R.drawable.platillo05))
        platillos.add(Platillo("Platillo6", 530.0, 5.5F, R.drawable.platillo06))
        platillos.add(Platillo("Platillo7", 650.0, 4.5F, R.drawable.platillo07))
        platillos.add(Platillo("Platillo8", 240.0, 3.5F, R.drawable.platillo08))
        platillos.add(Platillo("Platillo9", 130.0, 2.5F, R.drawable.platillo09))
        platillos.add(Platillo("Platillo10", 320.0, 1.5F, R.drawable.platillo10))


        layoutManager = LinearLayoutManager(this)
        listaRecycler?.layoutManager = layoutManager

        val callBack = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                // Inicializar actiomode
                adaptador?.iniciarActionMode()
                actionMode = mode
                // Inflar menu
                menuInflater.inflate(R.menu.menu_contextual, menu)

                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.title = "0 Seleccionadoas"
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                // Cuando se le da click a un elemento del toolbar
                when(item?.itemId){
                    R.id.menu_eliminar->{
                        adaptador?.eliminarSeleccionados()

                    }
                    else->{return true }
                }

                adaptador?.terminarActionMode()
                mode?.finish()
                isActionMode = false

                return  true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                // Destrulle el actiomode
                adaptador?.destruirActionMode()
                isActionMode = false
            }
        }

        adaptador = AdaptadorCustom(platillos, object : ClickListener{
            override fun onClick(view: View, index: Int) {
                Toast.makeText(applicationContext, platillos.get(index).nombre, Toast.LENGTH_SHORT).show()
            }
        }, object : LongClickListener{
            override fun longClick(view: View, index: Int) {
                Log.d("LONGSSS", "AQUII")
                if (!isActionMode){
                    startSupportActionMode(callBack)
                    isActionMode = true
                    adaptador?.selecionarItem(index)

                }else{
                    // Hacer selecciones o deselecciones
                    adaptador?.selecionarItem(index)
                }
                actionMode?.title = adaptador?.obtenerElelementosSelecionados().toString() + " Seleccionados"
            }
        })
        listaRecycler?.adapter = adaptador


        swipeRefresh.setOnRefreshListener {
            for (i in 1..1000000000){
            }
            platillos.add(Platillo("Nuggets", 230.6, 0.5F, R.drawable.platillo01))
            swipeRefresh.isRefreshing = false
            adaptador?.notifyDataSetChanged()
        }
    }
}