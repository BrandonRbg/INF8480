package ca.polymtl.inf8480.tp1.calculationserver.operations;

public class ResourcesService {
    public static float getRefusalRatio(int operationCount, int maxOperations) {
        return (float) (operationCount - maxOperations) / (4 * maxOperations);
    }
}
