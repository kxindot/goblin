package com.kxindot.goblin.source;

import static com.kxindot.goblin.Objects.Dot;
import static com.kxindot.goblin.Objects.LF;
import static com.kxindot.goblin.Objects.TAB;
import static com.kxindot.goblin.Objects.WS;
import static com.kxindot.goblin.Objects.requireNotNull;
import static java.lang.String.format;

import java.io.Serializable;

/**
 * 
 * @author ZhaoQingJiang
 */
public abstract class Source implements Serializable {

    private static final long serialVersionUID = -66872432133198238L;

    private StringBuilder builder;
    
    public Source() {
        builder = new StringBuilder(256);
    }
    
    
    protected Source append(CharSequence code) {
        builder.append(requireNotNull(code));
        return this;
    }
    
    
    protected Source append(String format, Object... args) {
        builder.append(format(format, args));
        return this;
    }
    
    
    protected Source ws() {
        builder.append(WS);
        return this;
    }
    
    
    protected Source tab() {
        builder.append(TAB);
        return this;
    }
    
    
    protected Source lineFeed() {
        builder.append(LF);
        return this;
    }
    
    protected String joinWithDot(CharSequence... css) {
        return String.join(Dot, css);
    }
    
    public String toSource() {
        return builder.toString();
    }
    
    @Override
    public abstract boolean equals(Object obj);
    
    
    @Override
    public abstract int hashCode();
    
    
    @Override
    public String toString() {
        return toSource();
    }
}
