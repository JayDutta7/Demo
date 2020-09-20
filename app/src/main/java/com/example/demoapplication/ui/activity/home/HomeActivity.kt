package com.example.demoapplication.ui.activity.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.DemoApplication.Companion.getPref
import com.example.demoapplication.R
import com.example.demoapplication.localDatabase.staticValue.StaticValue
import com.example.demoapplication.ui.BaseActivity
import com.example.demoapplication.ui.activity.home.adapter.MainAdapter
import com.example.demoapplication.viewModel.home.HomeViewModel

class HomeActivity : BaseActivity() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mainAdapter: MainAdapter

    private lateinit var headerMap: HashMap<String, String>

    private lateinit var listMain: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listMain = (findViewById(R.id.list_main))
        headerMap = HashMap()
        /*ViewModel*/
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        if (this::listMain.isInitialized)
            listMain.layoutManager = LinearLayoutManager(this)


        if (this::homeViewModel.isInitialized) {

            /*Hit Api*/
            getPref().getStringValue(StaticValue.headerKey)?.let {

                headerMap[StaticValue.Header] = "application/json"
                headerMap["ACCESSTOKEN"] = it
                homeViewModel.requestResponse(
                    headerMap,
                    timeZone = "Asia/Kolkata",
                    pageNo = 1,
                    action = "homePage"
                )
            }

            /*Get-Data*/
            homeViewModel.mutableLiveData.observe(this, androidx.lifecycle.Observer {
                mainAdapter =
                    MainAdapter(
                        context = this@HomeActivity,
                        list = it.postList
                    )
                listMain.adapter = mainAdapter
            })
        }


    }

    override fun setContentView(): Int? {
        return R.layout.activity_main
    }


}