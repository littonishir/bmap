package com.example.bmap.baidu

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.baidu.mapapi.BMapManager
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.poi.PoiResult
import com.example.bmap.R
import java.util.*

/**
 * 用于显示poi的overly
 */
class PoiOverlay
/**
 * 构造函数
 *
 * @param baiduMap   该 PoiOverlay 引用的 BaiduMap 对象
 */
    (baiduMap: BaiduMap?) : OverlayManager(baiduMap) {
    /**
     * 获取该PoiOverlay的poi数据
     *
     * @return     POI数据
     */
    var poiResult: PoiResult? = null
        private set

    /**
     * 设置POI数据
     *
     * @param poiResult    设置POI数据
     */
    fun setData(poiResult: PoiResult?) {
        this.poiResult = poiResult
    }


    override fun overlayOptions(): List<OverlayOptions>? {
        if (poiResult == null || poiResult!!.allPoi == null) {
            return null
        }
        val markerList: MutableList<OverlayOptions> = ArrayList()
        var markerSize = 0
        var i = 0
        while (i < poiResult!!.allPoi.size && markerSize < MAX_POI_SIZE) {
            if (poiResult!!.allPoi[i].location == null) {
                i++
                continue
            }
            markerSize++
            val bundle = Bundle()
            bundle.putInt("index", i)
            bundle.putParcelable("poiPoint", poiResult!!.allPoi[i])
            markerList.add(
                MarkerOptions()
                    .icon(getBitmapDes(poiResult!!.allPoi[i].name))
                    .extraInfo(bundle)
                    .position(poiResult!!.allPoi[i].location)
            )
            i++
        }
        return markerList
    }

    /**
     * 覆写此方法以改变默认点击行为
     *
     * @param i    被点击的poi在
     * [PoiResult.getAllPoi] 中的索引
     * @return     true--事件已经处理，false--事件未处理
     */
    fun onPoiClick(i: Int): Boolean {
        return false
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        if (!mOverlayList!!.contains(marker)) {
            return false
        }
        return if (marker.extraInfo != null) {
            onPoiClick(marker.extraInfo.getInt("index"))
        } else false
    }

    override fun onPolylineClick(polyline: Polyline): Boolean {
        return false
    }

    /**
     * 获取每个聚合点的绘制样式
     */
    private fun getBitmapDes(num: String): BitmapDescriptor {
        val textView = TextView(BMapManager.getContext())
        // 应用字体
        textView.text = num
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        textView.setBackgroundResource(R.mipmap.ditu_bg)
        return BitmapDescriptorFactory.fromView(textView)
    }

    companion object {
        private const val MAX_POI_SIZE = 10
    }
}