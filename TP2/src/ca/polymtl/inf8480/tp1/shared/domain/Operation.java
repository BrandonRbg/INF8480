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

    private static OperationType parseOperationType(String operationType) {
        switch (operationType) {
            case "pell":
                return OperationType.PELL;
            case "prime":
                return OperationType.PRIME;
        }
        return null;
    }

    public static Operation parseOperation(String operation) {
        try {
            String[] parts = operation.split(" ");
            return new Operation(parseOperationType(parts[0]), Integer.parseInt(parts[1]));
        } catch (Exception e) {
            return null;
        }
    }
}
