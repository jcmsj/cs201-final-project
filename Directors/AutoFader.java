package Directors;

public class AutoFader extends RowFader {
    public AutoFader(int[] ints) {
        super(ints);
    }

    @Override
    public void mergeSort() {
        super.mergeSort();
        anim.schedule(t -> keepAnimating(), 500);
    }

    public void keepAnimating() {
        nextRow(this::keepAnimating);
    }

    @Override
    public void addListeners() {
        // PASS
    }
}