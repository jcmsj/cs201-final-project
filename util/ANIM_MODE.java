package util;

public enum ANIM_MODE {
    AUTO,
    SEMI,
    SEMI_OLD,
    MANUAL;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Automatic";
            case 1:
                return "Semi-Automatic";
            case 2:
                return "Semi-Automatic (Reversible)";
            case 3:
                return "Manual (Reversible)";
            default:
                return null;
        }
    }
}