package com.bitdecay.ludum.dare.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.bitdecay.ludum.dare.ResourceDir;

import java.util.HashMap;

public class SoundLibrary {
    private static final HashMap<String, SoundEffect> sounds = new HashMap<>();
    private static final HashMap<String, MusicEffect> musics = new HashMap<>();

    static {
        sounds.put("Badguy", new SoundEffect(0.15f));
        sounds.put("Default", new SoundEffect(0.04f));
        sounds.put("Groan", new SoundEffect(0.15f));
        sounds.put("Hit_Hurt2", new SoundEffect(0.1f));
        sounds.put("Hit_Hurt3", new SoundEffect(0.02f));
        sounds.put("HurtSound1", new SoundEffect(0.2f));
        sounds.put("HurtSound2", new SoundEffect(0.4f));
        sounds.put("Jetpack", new SoundEffect(0.25f));
        sounds.put("JetpackLong", new SoundEffect(0.05f));
        sounds.put("Laser_Shoot1", new SoundEffect(0.07f));
        sounds.put("Laser_Shoot2", new SoundEffect(0.05f));
        sounds.put("Laser_Shoot3", new SoundEffect(0.1f));
        sounds.put("Laser_Shoot4", new SoundEffect(0.07f));
        sounds.put("LaserHit1", new SoundEffect(0.07f));
        sounds.put("LaserHit2", new SoundEffect(0.07f));
        sounds.put("LaserHit3", new SoundEffect(0.07f));
        sounds.put("LaserKill1", new SoundEffect(0.07f));
        sounds.put("LaserShootHigh", new SoundEffect(0.07f));
        sounds.put("LongDeath", new SoundEffect(0.07f));
        sounds.put("MonkeyCall", new SoundEffect(0.07f));
        sounds.put("MonkeyHurt", new SoundEffect(0.5f));
        sounds.put("MonkeyVaporize", new SoundEffect(0.1f));
        sounds.put("Power", new SoundEffect(0.07f));
        sounds.put("Quake", new SoundEffect(0.7f));
        sounds.put("Randomize2", new SoundEffect(0.07f));
        sounds.put("Scwaaaap", new SoundEffect(0.07f));
        sounds.put("ShipBeacon", new SoundEffect(0.07f));
        sounds.put("ShipAlarm", new SoundEffect(0.1f));
        sounds.put("crashBig", new SoundEffect(0.1f));
        sounds.put("Vaporize", new SoundEffect(0.1f));

        musics.put("ambientGame", new MusicEffect(1.5f));
        musics.put("ambientIntro", new MusicEffect(1.5f));
        musics.put("AlarmExtended", new MusicEffect(.03f));
    }

    public static synchronized Sound playSound(String name) {
        return getSound(name).play();
    }

    public static synchronized Sound stopSound(String name) {
        return getSound(name).stop();
    }

    private static SoundEffect getSound(String name) {
        SoundEffect sound;

        sound = sounds.get(name);
        if (sound.sound == null) {
            sound.sound = Gdx.audio.newSound(ResourceDir.internal("sounds/" + name + ".wav"));
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
            FileHandle musicFile = ResourceDir.internal("music/" + name + ".mp3");
            if (!musicFile.exists()) {
                musicFile = ResourceDir.internal("music/" + name + ".wav");
            }
            music.music = Gdx.audio.newMusic(musicFile);
            musics.put(name, music);
        }

        return music;
    }

    public static void stopMusic(String name) {
        getMusic(name).music.stop();
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

        public Sound stop() {
            this.sound.stop();
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
