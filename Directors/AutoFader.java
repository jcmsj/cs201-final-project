package Directors;

public class AutoFader extends Fader {
    public AutoFader(int[] ints) {
        super(ints);
        anim.interval*=2;
    }

    @Override
    public void mergeSort() {
        super.mergeSort();
        keepAnimating();
    }

    public void keepAnimating() {
        nextRow(() -> keepAnimating());
    }

    @Override
    public void addListeners() {
        // PASS
    }
}
