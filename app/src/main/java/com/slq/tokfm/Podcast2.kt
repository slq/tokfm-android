package com.slq.tokfm

import com.slq.tokfm.api.Podcast

class Podcast2 : Podcast {
    override fun getTargetFilename() = "targetFilename"

    override fun getSeriesName() = "seriesName"

    override fun getName() = "name"

    override fun getId() = "id"

    override fun getEmissionText() = "emissionText"

    override fun getAudio() = "audio"
}

