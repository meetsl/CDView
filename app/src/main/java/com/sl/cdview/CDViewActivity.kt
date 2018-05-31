package com.sl.cdview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.sl.cdview.widget.CDView
import org.jetbrains.anko.dip
import org.jetbrains.anko.setContentView

class CDViewActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var mSBOutBorderWidth: SeekBar
    private lateinit var mSBInnerBorderWidth: SeekBar
    private lateinit var mSBInnerPointRadius: SeekBar
    private lateinit var mSBInnerCircleRadius: SeekBar
    private lateinit var mCDView: CDView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CDViewActivityUI().setContentView(this)
        initView()
        newCDView()
    }

    private fun initView() {
        mCDView = findViewById(R.id.cdview)
        mCDView.startRotateAnimation()
        mSBOutBorderWidth = findViewById(R.id.sb_out_border_width)
        mSBOutBorderWidth.max = 100
        mSBOutBorderWidth.setOnSeekBarChangeListener(this)
        mSBInnerBorderWidth = findViewById(R.id.sb_inner_border_width)
        mSBInnerBorderWidth.max = 100
        mSBInnerBorderWidth.setOnSeekBarChangeListener(this)
        mSBInnerPointRadius = findViewById(R.id.inner_point_radius)
        mSBInnerPointRadius.max = 100
        mSBInnerPointRadius.setOnSeekBarChangeListener(this)
        mSBInnerCircleRadius = findViewById(R.id.sb_inner_circle_radius)
        mSBInnerCircleRadius.max = 100
        mSBInnerCircleRadius.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.sb_out_border_width -> {
                mCDView.setOuterBorderWidth(dip(seekBar.progress))
            }
            R.id.sb_inner_border_width -> {
                mCDView.setInnerBorderWidth(dip(seekBar.progress))
            }
            R.id.inner_point_radius -> {
                mCDView.setInnerPointRadius(dip(seekBar.progress))
            }
            R.id.sb_inner_circle_radius -> {
                mCDView.setInnerCircleRadius(dip(seekBar.progress))
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun newCDView() {
        val root = findViewById<RelativeLayout>(R.id.rl_root)
        val cd = CDView(this)
        cd.apply {
            setImageResource(R.mipmap.cover)
            setBackgroundColor(Color.WHITE)
            setCDBgColor(Color.CYAN)
            startRotateAnimation()
        }
        val params = RelativeLayout.LayoutParams(dip(180), dip(180))
        cd.layoutParams = params
        root.addView(cd)
    }
}
