package com.bentham.voiceagent.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AudioCaptureUtil class.
 * Note: Some tests are disabled on CI environments as they require audio hardware.
 */
class AudioCaptureUtilTest {

    private final AudioCaptureUtil audioCaptureUtil = new AudioCaptureUtil();
    
    @Test
    void getDefaultAudioFormatShouldReturnValidFormat() {
        AudioFormat format = audioCaptureUtil.getDefaultAudioFormat();
        
        assertNotNull(format, "Audio format should not be null");
        assertEquals(16000.0f, format.getSampleRate(), "Sample rate should be 16000 Hz");
        assertEquals(16, format.getSampleSizeInBits(), "Sample size should be 16 bits");
        assertEquals(1, format.getChannels(), "Channel count should be 1 (mono)");
        assertTrue(format.isBigEndian() == false, "Format should be little endian");
    }
    
    @Test
    void isCapturingShouldReturnFalseInitially() {
        assertFalse(audioCaptureUtil.isCapturing(), "Should not be capturing initially");
    }
}
