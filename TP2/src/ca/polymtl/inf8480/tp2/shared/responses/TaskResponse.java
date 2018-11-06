package ca.polymtl.inf8480.tp2.shared.responses;

import java.util.List;
import java.util.Objects;

public class TaskResponse {
    private List<Integer> result;

    public TaskResponse(List<Integer> result) {
        this.result = result;
    }

    public List<Integer> getResult() {
        return result;
    }

    public void setResult(List<Integer> result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskResponse)) return false;
        TaskResponse response = (TaskResponse) o;
        return Objects.equals(getResult(), response.getResult());
    }
}
