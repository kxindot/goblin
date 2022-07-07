package com.kxindot.goblin.database.sql.condition;

import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.database.sql.condition.Connector.And;
import static com.kxindot.goblin.database.sql.condition.Connector.Or;
import static com.kxindot.goblin.database.sql.condition.Operator.EQ;
import static com.kxindot.goblin.database.sql.condition.Operator.GT;
import static com.kxindot.goblin.database.sql.condition.Operator.GTE;
import static com.kxindot.goblin.database.sql.condition.Operator.LIKE;
import static com.kxindot.goblin.database.sql.condition.Operator.LT;
import static com.kxindot.goblin.database.sql.condition.Operator.LTE;
import static com.kxindot.goblin.database.sql.condition.Operator.NE;

import java.util.List;

import com.kxindot.goblin.EnumValue;
import com.kxindot.goblin.database.sql.Sql;

/**
 * @author zhaoqingjiang
 */
public interface Where {
    
    public static Where condition() {
        return new WhereImpl();
    }
    
    Operate column(String columnName);
    
    Where and();
    
    Where or();
    
    Sql toSql(Sql sql);
    
    Object[] values();
    
    /**
     * @author zhaoqingjiang
     */
    static class WhereImpl implements Operate, Where {
        
        private int index;
        private List<Condition> cs;
        private List<Connector> cns;

        private WhereImpl() {
            this.index = 0;
            this.cs = newArrayList();
            this.cns = newArrayList();
        }

        @Override
        public Operate column(String columnName) {
            if (index > cns.size()) {
                cns.add(And);
            }
            this.cs.add(index++, new Condition(columnName));
            return this;
        }

        @Override
        public Where and() {
            if (index > cns.size()) {
                cns.add(And);
            }
            return this;
        }

        @Override
        public Where or() {
            if (index > cns.size()) {
                cns.add(Or);
            }
            return this;
        }

        @Override
        public Sql toSql(Sql sql) {
            if (cs.isEmpty()) return sql;
            for (int i = 0; i < cs.size(); i++) {
                sql.WHERE(cs.get(i).statement());
                Connector c = null;
                if (i < cns.size()) {
                    c = cns.get(i);
                }
                if (c != null) {
                    switch (c) {
                        case And: sql.AND();break;
                        case Or: sql.OR();break;
                        default: throw new IllegalArgumentException("Unknown SQL Connector " + c.value());
                    }
                }
            }
            return sql;
        }

        @Override
        public Object[] values() {
            if (cs.isEmpty()) {
                return new Object[0];
            }
            List<Object> vals = newArrayList();
            for (Condition c : cs) {
                if (c.isNotNullValue()) {
                    vals.add(c.value());
                }
            }
            return vals.toArray(new Object[vals.size()]);
        }

        @Override
        public Where operate(String operator, Object value) {
            operator = requireNotBlank(operator, "operator can't be null or blank").trim();
            Operator op = EnumValue.valueOf(operator, Operator.class);
            if (op == null) {
                throw new IllegalArgumentException("Unknown operator " + operator);
            }
            return setValue(op, value);
        }

        @Override
        public Where eq(Object value) {
            return setValue(EQ, value);
        }

        @Override
        public Where ne(Object value) {
            return setValue(NE, value);
        }

        @Override
        public Where gt(Object value) {
            return setValue(GT, value);
        }

        @Override
        public Where gte(Object value) {
            return setValue(GTE, value);
        }

        @Override
        public Where lt(Object value) {
            return setValue(LT, value);
        }

        @Override
        public Where lte(Object value) {
            return setValue(LTE, value);
        }

        @Override
        public Where like(String value) {
            return setValue(LIKE, value);
        }
        
        Where setValue(Operator op, Object val) {
            cs.get(index - 1).setValue(op, val);
            return this;
        }
    }

}
