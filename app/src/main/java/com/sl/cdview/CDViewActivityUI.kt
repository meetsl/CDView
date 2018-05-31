package com.sl.cdview

import android.app.Activity
import android.graphics.Color
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.sl.cdview.widget.CDView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView

/**
 * Created by shilong
 *  2018/5/30.
 */
class CDViewActivityUI : AnkoComponent<Activity> {
    override fun createView(ui: AnkoContext<Activity>) = ui.apply {
        relativeLayout {
            id = R.id.rl_root
            setBackgroundColor(Color.WHITE)
            cdView {
                id = R.id.cdview
                setImageResource(R.mipmap.cover_1)
                setInnerBorderWidth(dip(1))
                setOuterBorderWidth(dip(10))
                setCDShaderWidth(dip(5))
                setCDBgColor(Color.WHITE)
                setBackgroundColor(Color.WHITE)
            }.lparams {
                width = dip(400)
                height = dip(400)
                centerInParent()
            }

            linearLayout {
                orientation = LinearLayout.VERTICAL
                linearLayout {
                    textView("outBorderWidth:")
                    seekBar {
                        id = R.id.sb_out_border_width
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                }.lparams {
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                    margin = dip(10)
                }
                linearLayout {
                    textView("innerBorderWidth:")
                    seekBar {
                        id = R.id.sb_inner_border_width
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                }.lparams {
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                    margin = dip(10)
                }
                linearLayout {
                    textView("innerCircleRadius:")
                    seekBar {
                        id = R.id.sb_inner_circle_radius
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                }.lparams {
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                    margin = dip(10)
                }
                linearLayout {
                    textView("innerPointRadius:")
                    seekBar {
                        id = R.id.inner_point_radius
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                    }
                }.lparams {
                    width = LinearLayout.LayoutParams.MATCH_PARENT
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                    margin = dip(10)
                }
            }.lparams {
                width = RelativeLayout.LayoutParams.MATCH_PARENT
                height = RelativeLayout.LayoutParams.WRAP_CONTENT
                alignParentBottom()
            }
        }
    }.view

    private inline fun ViewManager.cdView(init: CDView.() -> Unit): CDView {
        return ankoView({ CDView(it) }, theme = 0, init = init)
    }
}