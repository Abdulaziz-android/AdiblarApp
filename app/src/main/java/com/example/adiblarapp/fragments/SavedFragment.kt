package com.example.adiblarapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adiblarapp.MainActivity
import com.example.adiblarapp.R
import com.example.adiblarapp.adapter.WriterAdapter
import com.example.adiblarapp.database.AppDatabase
import com.example.adiblarapp.database.dao.WriterDao
import com.example.adiblarapp.databinding.FragmentSavedBinding
import com.example.adiblarapp.models.Writer
import com.google.firebase.firestore.FirebaseFirestore

class SavedFragment : Fragment(), MainActivity.OnBackPressedListener {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: WriterAdapter
    private lateinit var dao: WriterDao
    private var savedList: ArrayList<Writer> = arrayListOf()
    private var list: ArrayList<Writer> = arrayListOf()
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        dao = AppDatabase.getInstance(binding.root.context).writerDao()


        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.searchCard.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("page", "saved")
            findNavController().navigate(R.id.action_mainFragment_to_searchFragment, bundle)
        }

        initAdapter()
        loadData()

        return binding.root
    }

    private fun initAdapter() {
        adapter = WriterAdapter(
            dao,
            object : WriterAdapter.OnItemClickListener {
                override fun onItemClick(writer: Writer) {
                    val bundle = Bundle()
                    bundle.putSerializable("writer", writer)
                    findNavController().navigate(
                        R.id.action_mainFragment_to_writerFragment,
                        bundle
                    )
                }

                override fun onItemSaveClick(writer: Writer, position: Int) {
                    savedList.remove(writer)
                    binding.rv.adapter?.notifyItemRemoved(position)
                    binding.rv.adapter?.notifyItemRangeChanged(position, list.size)
                }
            })
        binding.rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setOnBackPressedListener(this@SavedFragment)
        if (isDataLoaded) refreshList()
    }

    override fun onBackPressed() {
        if (isDataLoaded) refreshList()
    }

    private fun refreshList() {
        savedList = arrayListOf()
        list.forEach {writer->
            val writerByName = dao.getWriterByName(writer.fullname!!)
            if (writerByName != null) {
                savedList.add(writer)
            }
        }
        adapter.submitList(savedList, false)
    }

    private fun loadData() {
        list = arrayListOf()
        savedList = arrayListOf()
        firebaseFirestore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.forEach { qds ->
                        val writer = qds.toObject(Writer::class.java)
                        val writerByName = dao.getWriterByName(writer.fullname!!)
                        list.add(writer)
                        if (writerByName != null) {
                            savedList.add(writer)
                        }
                    }
                    adapter.submitList(savedList, true)
                    isDataLoaded = true
                }
            }
    }


}