package com.example.ikeda.ikedawork

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 画像表示、下移動、消える
        this.animateTranslationY(imageView1)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }
    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        return when(item.itemId){
            R.id.item1 -> {
                true
            }else -> super.onOptionsItemSelected(item)
        }

    }
    private fun animateTranslationY(img: ImageView){
        val animationList = mutableListOf<Animator>()

        // 表示
        val objectAnimator0 = ObjectAnimator.ofFloat(img,"alpha",0f,1f)
        objectAnimator0.duration = 2000
        // objectAnimator0.repeatCount = -1
        animationList.add(objectAnimator0)

        // Y軸（画面下方向）へ動く
        val objectAnimator1 = ObjectAnimator.ofFloat(img, "translationY",1500f)
        objectAnimator1.duration = 4000
        // objectAnimator.repeatCount = -1
        animationList.add(objectAnimator1)

        // 透過
        val objectAnimator2 = ObjectAnimator.ofFloat(img,"alpha",1f,0f)
        objectAnimator2.duration = 2000
        animationList.add(objectAnimator2)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animationList)
        animatorSet.start()

        // クリックで消える
        img.setOnClickListener{
            // タッチされたタイミングで下方向へのanimatorSetはcancel
            animatorSet.cancel()
            val animationList = mutableListOf<Animator>()

            val objectAnimator1 = ObjectAnimator.ofFloat( img, "alpha", 1f, 0f )
            objectAnimator1.duration = 2000
            animationList.add(objectAnimator1)

            val animatorSetTouch = AnimatorSet()
            animatorSetTouch.playSequentially(animationList)
            animatorSetTouch.start()
        }
    }

/*    private val onClickListener = { img:ImageView ->
        val objectAnimator = ObjectAnimator.ofFloat( img, "alpha", 1f, 0f )
        objectAnimator.duration = 2000
        objectAnimator.start()
    }*/

    fun changeTextView(view: View){
        messageTextView.text = "Hello, World"
    }


}
