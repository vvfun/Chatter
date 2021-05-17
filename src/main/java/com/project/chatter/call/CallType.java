package com.project.chatter.call;

public enum CallType {
    NONE(-1),
    Call(0),
    Answer(1);

    private int type;

    CallType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static CallType getEnum(int type) {
        if (type == 1) {
            return CallType.Answer;
        }
        return CallType.Call;
    }

}
