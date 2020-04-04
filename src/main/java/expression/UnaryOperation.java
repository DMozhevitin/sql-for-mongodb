package expression;

import java.util.Objects;

public class UnaryOperation {

    private String name;

    private Long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public UnaryOperation(String name) {
        this(name, null);
    }

    public UnaryOperation(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getValue());
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

        UnaryOperation that = (UnaryOperation) o;

        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getValue(), that.getValue());
    }
}
