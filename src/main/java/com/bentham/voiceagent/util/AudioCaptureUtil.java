package com.bentham.voiceagent.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Utility class for capturing audio from the microphone.
 * This class provides methods for recording audio in various formats.
 * 
 * Note: This utility is primarily for desktop applications or testing.
 * For web applications, audio capture would happen in the browser.
 */
@Component
public class AudioCaptureUtil {

    private static final Logger logger = LoggerFactory.getLogger(AudioCaptureUtil.class);
    
    // Default audio format settings
    private static final float SAMPLE_RATE = 16000.0F;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    
    private TargetDataLine line;
    private boolean isCapturing = false;
    
    /**
     * Gets the default audio format for capturing.
     *
     * @return the default audio format
     */
    public AudioFormat getDefaultAudioFormat() {
        return new AudioFormat(
                SAMPLE_RATE,
                SAMPLE_SIZE_IN_BITS,
                CHANNELS,
                SIGNED,
                BIG_ENDIAN
        );
    }
    
    /**
     * Starts capturing audio with the default format.
     *
     * @param audioConsumer consumer for audio data chunks
     * @return CompletableFuture that completes when capturing stops
     */
    public CompletableFuture<Void> startCapturing(Consumer<byte[]> audioConsumer) {
        return startCapturing(getDefaultAudioFormat(), audioConsumer);
    }
    
    /**
     * Starts capturing audio with a specified format.
     *
     * @param format audio format to use
     * @param audioConsumer consumer for audio data chunks
     * @return CompletableFuture that completes when capturing stops
     */
    public CompletableFuture<Void> startCapturing(AudioFormat format, Consumer<byte[]> audioConsumer) {
        if (isCapturing) {
            throw new IllegalStateException("Audio capture is already in progress");
        }
        
        return CompletableFuture.runAsync(() -> {
            try {
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                
                if (!AudioSystem.isLineSupported(info)) {
                    throw new IllegalStateException("Audio line not supported: " + format);
                }
                
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                
                isCapturing = true;
                logger.info("Started audio capture with format: {}", format);
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                
                while (isCapturing) {
                    bytesRead = line.read(buffer, 0, buffer.length);
                    
                    if (bytesRead > 0) {
                        // Copy the buffer to avoid reuse issues
                        byte[] audioChunk = new byte[bytesRead];
                        System.arraycopy(buffer, 0, audioChunk, 0, bytesRead);
                        
                        // Pass the audio chunk to the consumer
                        audioConsumer.accept(audioChunk);
                    }
                }
                
            } catch (LineUnavailableException e) {
                logger.error("Error accessing audio line", e);
                throw new RuntimeException("Failed to access audio line", e);
            } finally {
                stopCapturing();
            }
        });
    }
    
    /**
     * Captures audio for a specified duration.
     *
     * @param durationMillis duration to capture in milliseconds
     * @return byte array containing the captured audio
     * @throws IOException if an I/O error occurs
     */
    public byte[] captureAudio(int durationMillis) throws IOException {
        AudioFormat format = getDefaultAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            
            logger.info("Started audio capture for {} ms", durationMillis);
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            long startTime = System.currentTimeMillis();
            
            while (System.currentTimeMillis() - startTime < durationMillis) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            
            return out.toByteArray();
            
        } catch (LineUnavailableException e) {
            logger.error("Error accessing audio line", e);
            throw new IOException("Failed to access audio line", e);
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }
    
    /**
     * Stops the current audio capture.
     */
    public void stopCapturing() {
        isCapturing = false;
        
        if (line != null) {
            line.stop();
            line.close();
            line = null;
            logger.info("Stopped audio capture");
        }
    }
    
    /**
     * Checks if audio is currently being captured.
     *
     * @return true if capturing, false otherwise
     */
    public boolean isCapturing() {
        return isCapturing;
    }
}
