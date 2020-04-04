package expression;

import java.util.List;
import java.util.Objects;

public class SelectStatement {
    private WhereClause whereClause;

    private String tableName;

    private List<String> selectItems;

    private List<UnaryOperation> unaryOperations;

    public List<UnaryOperation> getUnaryOperations() {
        return unaryOperations;
    }

    public void setUnaryOperations(List<UnaryOperation> unaryOperations) {
        this.unaryOperations = unaryOperations;
    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(WhereClause whereClause) {
        this.whereClause = whereClause;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<String> selectItems) {
        this.selectItems = selectItems;
    }

    public SelectStatement() { }

    public SelectStatement(WhereClause whereClause, String tableName, List<String> selectItems) {
        this.whereClause = whereClause;
        this.tableName = tableName;
        this.selectItems = selectItems;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSelectItems(), getTableName(), getUnaryOperations(), getWhereClause());
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

        SelectStatement that = (SelectStatement) o;
        return Objects.equals(getSelectItems(), that.getSelectItems()) &&
                Objects.equals(getTableName(), that.getTableName()) &&
                Objects.equals(getUnaryOperations(), that.getUnaryOperations()) &&
                Objects.equals(getWhereClause(), that.getWhereClause());
    }
}
