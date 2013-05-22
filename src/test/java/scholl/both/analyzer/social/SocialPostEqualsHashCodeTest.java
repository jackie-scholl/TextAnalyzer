package scholl.both.analyzer.social;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pobox.cbarham.testhelpers.EqualsHashCodeTestCase;

public class SocialPostEqualsHashCodeTest extends EqualsHashCodeTestCase {
    private static SocialPost a = new SocialPost("hello");
    private static SocialPost b = new SocialPost("world");
    
    @Override
    protected Object createInstance() throws Exception {
        return new SocialPost(a);
    }

    @Override
    protected Object createNotEqualInstance() throws Exception {
        return new SocialPost(b);
    }
    
}
