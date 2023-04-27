import java.util.LinkedList;

public interface DirectorLike {
    public int calcSplit();
    public LinkedList<Integer> calcSplits();
    public void addListeners();
    public void mergeStep();
    public void splitStep();
}