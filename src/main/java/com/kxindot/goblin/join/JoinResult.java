package com.kxindot.goblin.join;

import static com.kxindot.goblin.Objects.isEmpty;
import static com.kxindot.goblin.Objects.isNotEmpty;
import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Objects.newHashSet;
import static com.kxindot.goblin.Objects.requireNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.kxindot.goblin.Objects;

/**
 * @author ZhaoQingJiang
 */
public class JoinResult<Left, Right> {

    private Collection<Left> leftRemain;
    private Collection<Right> rightRemain;
    private Map<Left, Right> intersection;
    
    JoinResult() {
        this(100);
    }
    
    JoinResult(int capacity) {
        this.intersection = Objects.newHashMap(capacity);
    }
    
    public Collection<Left> leftRemain() {
        return leftRemain;
    }
    
    public Set<Left> leftRemainSet() {
        return leftRemain instanceof Set ? (Set<Left>) leftRemain : newHashSet(leftRemain);
    }
    
    public List<Left> leftRemainList() {
        return leftRemain instanceof List ? (List<Left>) leftRemain : newArrayList(leftRemain);
    }
    
    public Collection<Right> rightRemain() {
        return rightRemain;
    }
    
    public Set<Right> rightRemainSet() {
        return rightRemain instanceof Set ? (Set<Right>) rightRemain : newHashSet(rightRemain);
    }
    
    public List<Right> rightRemainList() {
        return rightRemain instanceof List ? (List<Right>) rightRemain : newArrayList(rightRemain);
    }
    
    public Map<Left, Right> intersection() {
        return intersection;
    }
    
    public void leftRemainForEach(Consumer<Left> consumer) {
        requireNotNull(consumer);
        if (isNotEmpty(leftRemain)) {
            leftRemain.forEach(e -> consumer.accept(e));
        }
    }
    
    public <E> List<E> leftRemainMap(Function<Left, E> function) {
        requireNotNull(function);
        return isEmpty(leftRemain) ? newArrayList() 
                : leftRemain.stream().map(e -> function.apply(e)).collect(Collectors.toList());
    }
    
    public void rightRemainForEach(Consumer<Right> consumer) {
        requireNotNull(consumer);
        if (isNotEmpty(rightRemain)) {
            rightRemain.forEach(e -> consumer.accept(e));
        }
    }
    
    public <E> List<E> rightRemainMap(Function<Right, E> function) {
        requireNotNull(function);
        return isEmpty(rightRemain) ? newArrayList() 
                : rightRemain.stream().map(e -> function.apply(e)).collect(Collectors.toList()); 
    }
    
    public <E> List<E> merge(BiFunction<Left, Right, E> biFunction) {
        requireNotNull(biFunction);
        List<E> list = newArrayList(intersection.size());
        if (isEmpty(intersection)) return list;
        intersection.forEach((k, v) -> list.add(biFunction.apply(k, v)));
        return list;
    }
    

    void setLeftRemain(Collection<Left> leftRemain) {
        this.leftRemain = leftRemain;
    }

    void setRightRemain(Collection<Right> rightRemain) {
        this.rightRemain = rightRemain;
    }

    void setIntersection(Map<Left, Right> intersection) {
        this.intersection = intersection;
    }
}
