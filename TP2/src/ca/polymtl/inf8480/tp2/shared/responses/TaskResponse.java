package ca.polymtl.inf8480.tp2.shared.responses;

import java.io.Serializable;

public class TaskResponse implements Serializable {
    private int result;

    public TaskResponse(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskResponse)) return false;
        TaskResponse that = (TaskResponse) o;
        return getResult() == that.getResult();
    }
}
