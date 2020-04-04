package expression;

import java.util.Objects;

public class ComparisonOperation {
    private String operation;

    private String leftOperand;

    private String rightOperand;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }

    public ComparisonOperation(String operation, String leftOperand, String rightOperand) {
        this.operation = operation;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOperation(), getLeftOperand(), getRightOperand());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        ComparisonOperation that = (ComparisonOperation) o;

        return Objects.equals(getOperation(), that.getOperation()) &&
                Objects.equals(getLeftOperand(), that.getLeftOperand()) &&
                Objects.equals(getRightOperand(), that.getRightOperand());
    }
}
