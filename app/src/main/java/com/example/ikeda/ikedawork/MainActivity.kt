package com.example.ikeda.ikedawork

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    // 色々な音色に後から変えられる用にvarで
    var soundId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundId = soundPool.load(this, R.raw.se_bell, 0)
        soundPool.setOnLoadCompleteListener({ soundPool, sampleId, status ->
            if ( status == 0 )
            // 画像表示、下移動、消える
            this.animateTranslationY(imageView1, soundPool, soundId)
        })

    }

    // menu用
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

    // 引数にsoundPoolとsoundIdを入れてるが、グローバル変数にしたので実質不要
    // 画像ランダム生成処理とマージしたときに消す予定
    private fun animateTranslationY(img: ImageView, soundPool: SoundPool?, soundId:Int){
        val animationList = mutableListOf<Animator>()

        // 表示
        val objectAnimator0 = ObjectAnimator.ofFloat(img,"alpha",0f,1f)
        objectAnimator0.duration = 2000
        animationList.add(objectAnimator0)

        // Y軸（画面下方向）へ動く
        val objectAnimator1 = ObjectAnimator.ofFloat(img, "translationY",1500f)
        objectAnimator1.duration = 4000
        animationList.add(objectAnimator1)

        // 透過
        val objectAnimator2 = ObjectAnimator.ofFloat(img,"alpha",1f,0f)
        objectAnimator2.duration = 2000
        animationList.add(objectAnimator2)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animationList)
        animatorSet.addListener(object : Animator.AnimatorListener{
            // アニメーションが終了したときに音を鳴らす
            override fun onAnimationEnd(animation: Animator?) {
                soundPool?.play(soundId,1.0f,1.0f,0,0,1.0f)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        animatorSet.start()

        img.setOnClickListener{
            // クリックで消える
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

    // アプリ終了時にリソース解放処理
    override fun onDestroy() {
        super.onDestroy()
        if (soundPool != null)
            soundPool.unload(soundId)
            soundPool.release()
    }

    // テスト　戻るボタン押したとき、とりあえず残してます
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK ->{
                val homeIntent = Intent(Intent.ACTION_MAIN)
                homeIntent.addCategory(Intent.CATEGORY_HOME)
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                return super.onKeyDown(keyCode, event)
                finish()
            }
            else -> {
                return super.onKeyDown(keyCode, event)
            }
        }
    }

}
