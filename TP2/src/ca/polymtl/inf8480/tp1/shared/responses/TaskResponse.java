package ca.polymtl.inf8480.tp1.shared.responses;

public class TaskResponse {
    private int[] result;

    public TaskResponse(int[] result) {
        this.result = result;
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }
}
