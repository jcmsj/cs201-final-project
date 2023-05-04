package util;
public enum ANIM_MODE {
    AUTO,
    SEMI,
    MANUAL;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Automatic";
            case 1:
                return "Semi-Automatic";
            case 2:
                return "Manual";
            default:
                return null;
        }
    }
}