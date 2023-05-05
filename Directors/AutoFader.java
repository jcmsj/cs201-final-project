package Directors;

public class AutoFader extends RowFader {
    public AutoFader(int[] ints) {
        super(ints);
    }

    @Override
    public void mergeSort() {
        super.mergeSort();
        anim.schedule(t -> nextRow(() -> keepAnimating()), 500);
    }

    public void keepAnimating() {
        nextRow(() -> keepAnimating());
    }

    @Override
    public void addListeners() {
        // PASS
    }
}