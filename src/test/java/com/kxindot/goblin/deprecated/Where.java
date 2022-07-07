package com.kxindot.goblin.deprecated;

import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;

import java.util.Map;

import com.kxindot.goblin.database.sql.Sql;
import com.kxindot.goblin.database.sql.condition.Condition;
import com.kxindot.goblin.database.sql.condition.Connector;

/**
 * @author zhaoqingjiang
 */
@Deprecated
public class Where {

    private Sql sql;
    private Map<Condition, Connector> condition;

    private Where() {
        this.sql = new Sql();
        this.condition = newLinkedHashMap();
    }
    
    Sql sql() {
        return sql;
    }

    public static Where insert(String table) {
        checkTable(table);
        Where c = new Where();
        c.sql().INSERT_INTO(table);
        return c;
    }

    public static Where delete(String table) {
        checkTable(table);
        Where c = new Where();
        c.sql().DELETE_FROM(table);
        return c;
    }

    public static Where update(String table) {
        checkTable(table);
        Where c = new Where();
        c.sql().UPDATE(table);
        return c;
    }

    public static Where select(String table) {

        return null;
    }

    static void checkTable(String table) {
        requireNotBlank(table, "table name can't be null or blank");
    }
    
}
