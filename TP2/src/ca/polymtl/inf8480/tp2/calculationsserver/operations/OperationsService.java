package ca.polymtl.inf8480.tp2.calculationsserver.operations;

import ca.polymtl.inf8480.tp2.shared.domain.Operation;

public class OperationsService {
    private float wrongAnswersRatio;

    public OperationsService(float wrongAnswersRatio) {
        this.wrongAnswersRatio = wrongAnswersRatio;
    }

    public int executeOperation(Operation operation) {
        if (shouldReturnWrongAnswer()) {
            return getRandomAnswer();
        }

        switch (operation.getOperation()) {
            case PELL:
                return Operations.pell(operation.getOperand());
            case PRIME:
                return Operations.prime(operation.getOperand());
            default:
                return 0;
        }
    }

    private int getRandomAnswer() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    private boolean shouldReturnWrongAnswer() {
        return Math.random() > wrongAnswersRatio;
    }
}
