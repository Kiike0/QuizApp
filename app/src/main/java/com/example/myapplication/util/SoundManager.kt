package com.example.myapplication.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Gestor de efectos de sonido con sonido nativo del sistema para QuizDesfakea.
 */
object SoundManager {

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 50)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Clic nativo del sistema al pulsar opciones o botones (sonido por defecto del móvil).
     */
    fun playClickSound(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.6f)
                } else {
                    playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 40)
                }
            } catch (_: Exception) {
                playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 40)
            }
        }
    }

    /**
     * Tono grave y sutil al teclear en campos de texto (sonido nativo de teclado).
     */
    fun playTypingSound(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 0.4f)
                } else {
                    playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 30)
                }
            } catch (_: Exception) {
                playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 30)
            }
        }
    }

    /**
     * Clic de confirmación para guardar preguntas o botones principales.
     */
    fun playActionConfirmSound(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.9f)
                } else {
                    playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 60)
                }
            } catch (_: Exception) {
                playLowTone(ToneGenerator.TONE_CDMA_LOW_L, 60)
            }
        }
    }

    private fun playLowTone(toneType: Int, durationMs: Int) {
        try {
            if (toneGenerator == null) {
                toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 50)
            }
            toneGenerator?.startTone(toneType, durationMs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
