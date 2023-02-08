package com.kxindot.goblin.join;

/**
 * @author ZhaoQingJiang
 */
@FunctionalInterface
public interface Comparetor<O1, O2> {

    
    int compare(O1 o1, O2 o2); 
    
}
