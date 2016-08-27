package com.bitdecay.ludum.dare.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundLibrary {
    private static final HashMap<String, SoundEffect> sounds = new HashMap<>();
    private static final HashMap<String, MusicEffect> musics = new HashMap<>();

    static {
        sounds.put("cashRegister", new SoundEffect(0.15f));
        sounds.put("Coin", new SoundEffect(0.04f));
        sounds.put("flight", new SoundEffect(0.15f));
        sounds.put("highJump", new SoundEffect(0.1f));
        sounds.put("Jump1", new SoundEffect(0.02f));
        sounds.put("Jump2", new SoundEffect(0.04f));
        sounds.put("Jump3", new SoundEffect(0.02f));
        sounds.put("Jump4", new SoundEffect(0.02f));
        sounds.put("Powerup", new SoundEffect(0.05f));
        sounds.put("Punch1", new SoundEffect(0.07f));
        sounds.put("Punch2", new SoundEffect(0.05f));
        sounds.put("Punch3", new SoundEffect(0.1f));
        sounds.put("Punch4", new SoundEffect(0.07f));
        sounds.put("Select_change", new SoundEffect(0.05f));
        sounds.put("Select_confirm", new SoundEffect(0.03f));
        sounds.put("Slide", new SoundEffect(0.3f));
        sounds.put("slotMachine", new SoundEffect(0.2f));
        sounds.put("slowdown", new SoundEffect(0.07f));
        sounds.put("speed", new SoundEffect(0.1f));
        sounds.put("stun", new SoundEffect(0.02f));

        musics.put("a_journey_awaits", new MusicEffect(0.05f));
        musics.put("fight", new MusicEffect(0.07f));
        musics.put("hero_immortal_short_intro", new MusicEffect(0.05f));
        musics.put("Ouroboros", new MusicEffect(0.05f));
    }


    public static synchronized Sound playSound(String name) {
        return getSound(name).play();
    }

    private static SoundEffect getSound(String name) {
        SoundEffect sound;

        sound = sounds.get(name);
        if (sound.sound == null) {
            sound.sound = Gdx.audio.newSound(Gdx.files.internal("sfx/" + name + ".ogg"));
        }

        return sound;
    }

    public static synchronized Music playMusic(String name) {
        return getMusic(name).play();
    }

    public static synchronized Music loopMusic(String name) {
        return getMusic(name).loop();
    }


    private static MusicEffect getMusic(String name) {
        MusicEffect music;

        music = musics.get(name);
        if (music.music == null) {
            music.music = Gdx.audio.newMusic(Gdx.files.internal("music/" + name + ".mp3"));
            musics.put(name, music);
        }

        return music;
    }


    private static class SoundEffect {
        public Sound sound;
        public float volume;

        public SoundEffect(Sound sound, float volume) {
            this.sound = sound;
            this.volume = volume;
        }

        public SoundEffect(float volume) {
            this.volume = volume;
        }

        public Sound play() {
            this.sound.play(this.volume);
            return this.sound;
        }
    }

    private static class MusicEffect {
        public Music music;
        public float volume;

        public MusicEffect(Music music, float volume) {
            this.music = music;
            this.volume = volume;
        }

        public MusicEffect(float volume) {
            this.volume = volume;
        }

        public Music play() {
            this.music.play();
            this.music.setVolume(this.volume);
            return this.music;
        }

        public Music loop() {
            this.music.play();
            this.music.setVolume(this.volume);
            this.music.setLooping(true);
            return this.music;
        }
    }
}
