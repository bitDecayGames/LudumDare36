package com.bitdecay.ludum.dare.control;

public class GameControl {
    private String name;
    private MultiKeyState state;

    public GameControl(String name, int... keys){
        this.name = name;
        this.state = new MultiKeyState(keys);
    }

    public String name(){
        return name;
    }

    public MultiKeyState state(){
        return state;
    }

    public String toString(){
        return name();
    }
}
