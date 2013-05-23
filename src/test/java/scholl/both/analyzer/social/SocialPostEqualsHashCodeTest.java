package scholl.both.analyzer.social;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

public class SocialPostEqualsHashCodeTest extends EqualsHashCodeTestCase {
    private static Post a = new Post("hello");
    private static Post b = new Post("world");
    
    @Override
    protected Object createInstance() throws Exception {
        return new Post(a);
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new Post(b);
    }
    
}
