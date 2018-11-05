package ca.polymtl.inf8480.tp1.shared.domain;

import java.io.Serializable;

public class Operation implements Serializable {
    public enum OperationType {
        PELL, PRIME
    }

    private OperationType operation;
    private int operand;

    public Operation(OperationType operation, int operand) {
        this.operation = operation;
        this.operand = operand;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public int getOperand() {
        return operand;
    }

    public void setOperand(int operand) {
        this.operand = operand;
    }
}
