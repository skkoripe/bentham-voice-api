package com.bentham.voiceagent.service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for text-to-speech synthesis.
 * This interface defines methods for converting text to speech.
 */
public interface SpeechSynthesisService {

    /**
     * Synthesizes speech from text.
     *
     * @param text the text to synthesize
     * @return a CompletableFuture that will be completed with an input stream of the synthesized speech
     */
    CompletableFuture<InputStream> synthesizeSpeech(String text);

    /**
     * Synthesizes speech from text with a specific voice.
     *
     * @param text the text to synthesize
     * @param voiceId the ID of the voice to use
     * @return a CompletableFuture that will be completed with an input stream of the synthesized speech
     */
    CompletableFuture<InputStream> synthesizeSpeech(String text, String voiceId);

    /**
     * Gets a list of available voices.
     *
     * @return a CompletableFuture that will be completed with an array of available voice IDs
     */
    CompletableFuture<String[]> getAvailableVoices();

    /**
     * Synthesizes speech from SSML (Speech Synthesis Markup Language).
     *
     * @param ssml the SSML text to synthesize
     * @return a CompletableFuture that will be completed with an input stream of the synthesized speech
     */
    CompletableFuture<InputStream> synthesizeSpeechFromSsml(String ssml);
}
