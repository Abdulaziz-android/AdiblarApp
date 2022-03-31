package com.example.adiblarapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.adiblarapp.R
import com.example.adiblarapp.database.dao.WriterDao
import com.example.adiblarapp.database.entity.WriterEntity
import com.example.adiblarapp.databinding.ItemWriterBinding
import com.example.adiblarapp.models.Writer
import com.squareup.picasso.Picasso

class WriterAdapter(
    private var dao: WriterDao,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<WriterAdapter.WriterVH>() {

    private var list = arrayListOf<Writer>()
    private var animation = true

    inner class WriterVH(private val itemBinding: ItemWriterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(writer: Writer, position: Int) {
            val context = itemBinding.root.context
            val itemAnimation = AnimationUtils.loadAnimation(context, R.anim.my_animation)
            val saveAnimation = AnimationUtils.loadAnimation(context, R.anim.save_anim)
            if (animation) itemBinding.root.startAnimation(itemAnimation)
            var saved = false
            //   val sPref = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
            val localWriter = dao.getWriterByName(writer.fullname!!)
            if (localWriter != null) {
                itemBinding.saveCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.save_green_back
                    )
                )
                itemBinding.saveIv.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_saved
                    )
                )
                saved = true
            } else {
                itemBinding.saveCard.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.save_back
                    )
                )
                itemBinding.saveIv.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_save_black
                    )
                )
                saved = false
            }

            Picasso.get().load(writer.photoUrl).placeholder(R.drawable.placeholder)
                .into(itemBinding.imageView)

            val fullname = writer.fullname!!.replace(" ", "\n")
            itemBinding.nameTv.text = fullname
            itemBinding.yearTv.text = "(${writer.bornyear}-${writer.deathyear})"
            itemBinding.saveCard.setOnClickListener {
                if (!saved) {
                    itemBinding.saveCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.save_green_back
                        )
                    )
                    itemBinding.saveIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_saved
                        )
                    )
                    itemBinding.saveIv.startAnimation(saveAnimation)
                    saved = true
                    dao.insert(WriterEntity(writer.fullname!!))

                    //  sPref.edit().putString(writer.fullname, "true").apply()
                } else {
                    itemBinding.saveCard.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.save_back
                        )
                    )
                    itemBinding.saveIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_save_black
                        )
                    )
                    itemBinding.saveIv.startAnimation(saveAnimation)
                    // sPref.edit().putString(writer.fullname, "false").apply()
                    dao.remove(WriterEntity(writer.fullname!!))
                    saved = false
                }
                listener.onItemSaveClick(writer, position)

            }

            itemBinding.root.setOnClickListener {
                listener.onItemClick(writer)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriterVH {
        return WriterVH(
            ItemWriterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WriterVH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size


    interface OnItemClickListener {
        fun onItemClick(writer: Writer)
        fun onItemSaveClick(writer: Writer, position: Int)
    }

    fun filterList(fList: ArrayList<Writer>) {
        list = fList
        this.notifyDataSetChanged()
    }

    fun submitList(list: ArrayList<Writer>, animation:Boolean){
        this.animation = animation
        this.list = list
        this.notifyDataSetChanged()
    }

}