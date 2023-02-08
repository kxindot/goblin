package com.kxindot.goblin.join;

import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ZhaoQingJiang
 */
public class Joiner<Left, Right> {
    
    private Collection<Left> left;
    private Collection<Right> right;

    private Joiner(Collection<Left> left, Collection<Right> right) {
        this.left = requireNotNull(left);
        this.right = requireNotNull(right);
    }
    

    public static <Left, Right> Joiner<Left, Right> 
        join(Collection<Left> left, Collection<Right> right) {
        return new Joiner<>(left, right);
    }
    
    
    public <E> JoinResult<Left, Right> on(
            Function<Left, E> leftIdentity, Function<Right, E> rightIdentity) {
        requireNotNull(leftIdentity);
        requireNotNull(rightIdentity);
        
        int ls = left.size();
        int rs = right.size();
        JoinResult<Left, Right> result = new JoinResult<>(ls > rs ? ls : rs);
        if (ls == 0 || rs == 0) {
            result.setLeftRemain(left);
            result.setRightRemain(right);
            return result;
        }
        
        Set<Left> leftRemain = newHashSet(ls);
        Map<Left, Right> intersection = result.intersection();
        Map<E, Right> rm = right.stream().collect(Collectors.toMap(rightIdentity, Function.identity()));
        left.forEach(e -> {
            E identity = leftIdentity.apply(e);
            Right r = rm.get(identity);
            if (r != null) {
                intersection.put(e, r);
                rm.remove(identity);
            } else {
                leftRemain.add(e);
            }
        });
        result.setLeftRemain(leftRemain);
        result.setRightRemain(newHashSet(rm.values()));
        return result;
    }
    
}