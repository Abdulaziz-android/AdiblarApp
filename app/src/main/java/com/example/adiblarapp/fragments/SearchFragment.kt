package com.example.adiblarapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adiblarapp.MainActivity
import com.example.adiblarapp.R
import com.example.adiblarapp.adapter.WriterAdapter
import com.example.adiblarapp.database.AppDatabase
import com.example.adiblarapp.database.dao.WriterDao
import com.example.adiblarapp.databinding.FragmentSearchBinding
import com.example.adiblarapp.models.Writer
import com.google.firebase.firestore.FirebaseFirestore

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private  lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: WriterAdapter
    private lateinit var list: ArrayList<Writer>
    private lateinit var searchlist: ArrayList<Writer>
    private lateinit var dao: WriterDao



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()
        dao = AppDatabase.getInstance(binding.root.context).writerDao()

        initAdapter()
        list = arrayListOf()
        searchlist = arrayListOf()

        val page = arguments?.getString("page")
        firebaseFirestore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.forEach { qds->
                        val writer = qds.toObject(Writer::class.java)
                        if (page == "saved") {
                            val writerByName = dao.getWriterByName(writer.fullname!!)
                            if (writerByName != null) {
                                list.add(writer)
                            }
                        } else list.add(writer)
                    }
                }

                searchlist = list
                adapter.submitList(searchlist, true)
            }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") {
                    searchlist = arrayListOf()
                    searchlist.addAll(list)
                    adapter.filterList(searchlist)
                } else {
                    searchlist = arrayListOf()
                    list.forEach { writer ->
                        if ((" ${writer.fullname!!.lowercase()}")
                                .contains(" ${s.toString().lowercase()}"))
                            searchlist.add(writer)
                    }
                    adapter.filterList(searchlist)
                }
            }

        })

        binding.closeIv.setOnClickListener {
            if (binding.searchEt.text.toString().isNotEmpty()) {
                binding.searchEt.text.clear()
            } else {
                requireActivity().currentFocus?.let { view ->
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                (activity as MainActivity?)?.onBackPressed()
            }
        }


        return binding.root
    }

    private fun initAdapter() {
        adapter = WriterAdapter(
            dao,
            object : WriterAdapter.OnItemClickListener {
                override fun onItemClick(writer: Writer) {
                    val bundle = Bundle()
                    bundle.putSerializable("writer", writer)
                    if (findNavController().currentDestination?.id == R.id.searchFragment) {
                        findNavController().navigate(
                            R.id.action_searchFragment_to_writerFragment,
                            bundle
                        )
                    }
                }

                override fun onItemSaveClick(writer: Writer, position: Int) {}
            })
        binding.rv.adapter = adapter
    }


}