package com.ulisesdiaz.recyclerview.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ulisesdiaz.ClickListener
import com.ulisesdiaz.LongClickListener
import com.ulisesdiaz.recyclerview.R
import com.ulisesdiaz.recyclerview.models.Platillo

class AdaptadorCustom(items: ArrayList<Platillo>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items: ArrayList<Platillo>? = null
    var itemsSeleccionados: ArrayList<Int>? = null
    var multiSeleccion = false
    var viewHolder: ViewHolder? = null

    init {
        this.items = items
        this.itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_platillo, parent, false)
        viewHolder = ViewHolder(view, listener, longClickListener)

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items?.get(position)
        holder.foto?.setImageResource(item?.foto!!)
        holder.nombre?.text = item?.nombre
        holder.precio?.text = "$" + item?.precio.toString()
        holder.rating?.rating = item?.rating!!

        if (itemsSeleccionados?.contains(position)!!){
            holder.view.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.view.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(view: View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
        val view = view
        var foto: ImageView? =  null
        var nombre: TextView? = null
        var precio: TextView? = null
        var rating: RatingBar? = null
        var listener: ClickListener? = null
        var longListener: LongClickListener? = null

        init {
            foto = view.findViewById(R.id.imgFoto)
            nombre = view.findViewById(R.id.txtNombre)
            precio = view.findViewById(R.id.txtPrecio)
            rating = view.findViewById(R.id.ratingBar)
            this.listener = listener
            this.longListener = longClickListener
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }
    }

    fun  iniciarActionMode(){
        multiSeleccion = true
    }

    fun  destruirActionMode(){
        multiSeleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        // Eliminar elementos selecionados
        for (item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()
    }

    fun selecionarItem(index: Int){
        if (multiSeleccion){
            if (itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }
            notifyDataSetChanged()
        }
    }

    fun obtenerElelementosSelecionados(): Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if (itemsSeleccionados?.count()!! > 0){
            var itemsEliminados = ArrayList<Platillo>()

            for (index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }

            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

}