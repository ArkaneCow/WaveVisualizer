package edu.gatech.wavevis.app;

public enum LabelState {
    NONE,
    TALKING,
    BREATHING,
    LEFT_BLOW,
    RIGHT_BLOW,;

    @Override
    public String toString() {
        switch (this) {
            case TALKING:
                return "Talking";
            case BREATHING:
                return "Breathing";
            case LEFT_BLOW:
                return "Left Blow";
            case RIGHT_BLOW:
                return "Right Blow";
            case NONE:
                return "None";
            default:
                throw new IllegalArgumentException("Invalid LabelState");
        }
    }
}
