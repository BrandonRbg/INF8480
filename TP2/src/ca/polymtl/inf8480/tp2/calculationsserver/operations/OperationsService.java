package ca.polymtl.inf8480.tp2.calculationsserver.operations;

import ca.polymtl.inf8480.tp2.shared.domain.Operation;

public class OperationsService {
    private float wrongAnswersRatio;

    public OperationsService(float wrongAnswersRatio) {
        this.wrongAnswersRatio = wrongAnswersRatio;
    }

    public int executeOperation(Operation operation) {
        int result = 0;
        switch (operation.getOperation()) {
            case PELL:
                result = Operations.pell(operation.getOperand());
                break;
            case PRIME:
                result = Operations.prime(operation.getOperand());
                break;
        }

        if (shouldReturnWrongAnswer()) {
            return getRandomAnswer();
        }
        return result;
    }

    private int getRandomAnswer() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    private boolean shouldReturnWrongAnswer() {
        return Math.random() < wrongAnswersRatio;
    }
}
