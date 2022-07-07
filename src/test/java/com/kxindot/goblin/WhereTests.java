package com.kxindot.goblin;

import java.util.Arrays;

import com.kxindot.goblin.database.sql.Sql;
import com.kxindot.goblin.database.sql.condition.Where;

public class WhereTests {

    public static void main(String[] args) {
        
        Sql sql = new Sql();
        sql.SELECT("test_c").FROM("test_t");
        Where where = Where.condition();
        where.and();
        where.or();
        where.column("ct1").operate(">=", 5)
            .column("ct2").eq(21).and().column("ct3").lt("2021-10-22").or().column("ct4").ne(null);
        where.toSql(sql);
        System.out.println(sql.toString());
        System.out.println(Arrays.toString(where.values()));
    }
    
}
