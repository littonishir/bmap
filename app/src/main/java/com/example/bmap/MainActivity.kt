package com.example.bmap

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.example.bmap.baidu.PoiOverlay

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mPoiSearch = PoiSearch.newInstance()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv1 -> {
                poiSearch("地铁")
            }
            R.id.tv2 -> {
                poiSearch("公交")
            }
            R.id.tv3 -> {
                poiSearch("学校")
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv1 = findViewById<TextView>(R.id.tv1)
        val tv2 = findViewById<TextView>(R.id.tv2)
        val tv3 = findViewById<TextView>(R.id.tv3)
        tv1.setOnClickListener(this)
        tv2.setOnClickListener(this)
        tv3.setOnClickListener(this)
        initMap()

    }

    private fun initMap() {
        val bMapView = findViewById<MapView>(R.id.bMapView)
        val mBaiduMap = bMapView.map
        //poi搜索监听
        val listener: OnGetPoiSearchResultListener = object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(poiResult: PoiResult?) {
                if (poiResult!!.error === SearchResult.ERRORNO.NO_ERROR) {
                    mBaiduMap.clear()
                    //创建PoiOverlay对象
                    val poiOverlay = PoiOverlay(mBaiduMap)
                    //设置Poi检索数据
                    poiOverlay.setData(poiResult)
                    //将poiOverlay添加至地图并缩放至合适级别
                    poiOverlay.addToMap()
                    poiOverlay.zoomToSpan()
                }
            }

            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult?) {}
            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult?) {}
            override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult?) {}
        }
        mPoiSearch.setOnGetPoiSearchResultListener(listener);

        //Marker 覆盖物点击事件监听函数
        val clickListener: BaiduMap.OnMarkerClickListener = BaiduMap.OnMarkerClickListener {
            val extraInfo = it.extraInfo
            val get = extraInfo.get("poiPoint") as PoiInfo
            Toast.makeText(this, get.name, Toast.LENGTH_SHORT).show()
            true //是否捕获点击事件
        }
        mBaiduMap.setOnMarkerClickListener(clickListener)
    }

    /**
     * poi附近搜索
     * location 圆心点
     * radius 检索半径
     * keyword 检索内容关键字
     * pageNum 分页页码
     */
    private fun poiSearch(
        str: String,
        latLng: LatLng = LatLng(40.005451, 116.382222),
        radius: Int = 1000,
    ) {
        mPoiSearch.searchNearby(
            PoiNearbySearchOption()
                .location(LatLng(40.005451, 116.382222))
                .radius(radius) //支持多个关键字并集检索，不同关键字间以$符号分隔，最多支持10个关键字检索。如:”银行$酒店”
                .keyword(str)
                .pageNum(0)
        )
    }


}