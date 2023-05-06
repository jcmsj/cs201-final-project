package util;

public enum ANIM_MODE {
    ALL,
    ROW,
    ROW_OLD,
    BLOCK;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Until Sorted";
            case 1:
                return "By Row";
            case 2:
                return "By Row (Reversible)";
            case 3:
                return "By block (Reversible)";
            default:
                return null;
        }
    }
}