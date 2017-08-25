/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.things.contrib.driver.voicehat;


import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import com.google.android.things.pio.I2sDevice;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class Max98357AInstrumentationTest {
    private VoiceHat mVoiceHat;

    @Before
    public void createVoiceHat() throws IOException {
        InstrumentationTestUtils.assertRaspberryPiOnly();
        mVoiceHat = new VoiceHat(InstrumentationTestUtils.VOICE_HAT_I2S_RPI,
          InstrumentationTestUtils.VOICE_HAT_TRIGGER_GPIO_RPI,
          InstrumentationTestUtils.AUDIO_FORMAT_STEREO);
    }

    /**
     * Verify the existence of particular pins when the chip is constructed through the VoiceHat
     * driver. This means that {@code NOT_SD_MODE} exists, but {@code GAIN_SLOT} is {@code null}.
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Max98357A dac = mVoiceHat.getDac();
        Assert.assertNotNull(dac.getI2sDevice());
        Assert.assertNotNull(dac.getNotSdModePin());
        Assert.assertNull(dac.getGainSlotPin());
    }

    /**
     * Verify the functionality of this device when interfaced with directly instead of through the
     * VoiceHat.
     */
    @Test
    public void testCustomInterface() throws Exception {
        Max98357A dac = new Max98357A(Mockito.mock(I2sDevice.class),
                InstrumentationTestUtils.VOICE_HAT_BUTTON_GPIO_RPI,
                InstrumentationTestUtils.VOICE_HAT_LED_GPIO_RPI);
        Assert.assertNotNull(dac.getI2sDevice());
        Assert.assertNotNull(dac.getNotSdModePin());
        Assert.assertNotNull(dac.getGainSlotPin());
        dac.close();
    }

    @After
    public void close() throws IOException {
        if (mVoiceHat != null) {
            mVoiceHat.close();
        }
    }
}
