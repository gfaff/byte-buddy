package net.bytebuddy.instrumentation.attribute.annotation;

import net.bytebuddy.instrumentation.type.TypeDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractEnumerationValueTest {

    protected abstract AnnotationDescription.EnumerationValue describe(Enum<?> enumeration);

    @Test
    public void testPrecondition() throws Exception {
        assertThat(describe(Sample.FIRST).getEnumerationType().represents(Sample.class), is(true));
        assertThat(describe(Other.INSTANCE).getEnumerationType().represents(Other.class), is(true));
    }

    @Test
    public void assertValue() throws Exception {
        assertThat(describe(Sample.FIRST).getValue(), is(Sample.FIRST.name()));
        assertThat(describe(Sample.SECOND).getValue(), is(Sample.SECOND.name()));
    }

    @Test
    public void assertToString() throws Exception {
        assertThat(describe(Sample.FIRST).toString(), is(Sample.FIRST.toString()));
        assertThat(describe(Sample.SECOND).toString(), is(Sample.SECOND.toString()));
    }

    @Test
    public void assertType() throws Exception {
        assertThat(describe(Sample.FIRST).getEnumerationType(), equalTo((TypeDescription) new TypeDescription.ForLoadedType(Sample.class)));
        assertThat(describe(Sample.SECOND).getEnumerationType(), equalTo((TypeDescription) new TypeDescription.ForLoadedType(Sample.class)));
    }

    @Test
    public void assertHashCode() throws Exception {
        assertThat(describe(Sample.FIRST).hashCode(), is(Sample.FIRST.name().hashCode() + 31 * new TypeDescription.ForLoadedType(Sample.class).hashCode()));
        assertThat(describe(Sample.SECOND).hashCode(), is(Sample.SECOND.name().hashCode() + 31 * new TypeDescription.ForLoadedType(Sample.class).hashCode()));
        assertThat(describe(Sample.FIRST).hashCode(), not(is(describe(Sample.SECOND).hashCode())));
    }

    @Test
    public void assertEquals() throws Exception {
        AnnotationDescription.EnumerationValue identical = describe(Sample.FIRST);
        assertThat(identical, equalTo(identical));
        AnnotationDescription.EnumerationValue equalFirst = mock(AnnotationDescription.EnumerationValue.class);
        when(equalFirst.getValue()).thenReturn(Sample.FIRST.name());
        when(equalFirst.getEnumerationType()).thenReturn(new TypeDescription.ForLoadedType(Sample.class));
        assertThat(describe(Sample.FIRST), equalTo(equalFirst));
        AnnotationDescription.EnumerationValue equalSecond = mock(AnnotationDescription.EnumerationValue.class);
        when(equalSecond.getValue()).thenReturn(Sample.SECOND.name());
        when(equalSecond.getEnumerationType()).thenReturn(new TypeDescription.ForLoadedType(Sample.class));
        assertThat(describe(Sample.SECOND), equalTo(equalSecond));
        AnnotationDescription.EnumerationValue equalFirstTypeOnly = mock(AnnotationDescription.EnumerationValue.class);
        when(equalFirstTypeOnly.getValue()).thenReturn(Sample.SECOND.name());
        when(equalFirstTypeOnly.getEnumerationType()).thenReturn(new TypeDescription.ForLoadedType(Sample.class));
        assertThat(describe(Sample.FIRST), not(equalTo(equalFirstTypeOnly)));
        AnnotationDescription.EnumerationValue equalFirstNameOnly = mock(AnnotationDescription.EnumerationValue.class);
        when(equalFirstNameOnly.getValue()).thenReturn(Sample.FIRST.name());
        when(equalFirstNameOnly.getEnumerationType()).thenReturn(new TypeDescription.ForLoadedType(Other.class));
        assertThat(describe(Sample.FIRST), not(equalTo(equalFirstNameOnly)));
        assertThat(describe(Sample.FIRST), not(equalTo(equalSecond)));
        assertThat(describe(Sample.FIRST), not(equalTo(new Object())));
        assertThat(describe(Sample.FIRST), not(equalTo(null)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompatible() throws Exception {
        describe(Sample.FIRST).load(Other.class);
    }

    public static enum Sample {
        FIRST,
        SECOND
    }

    private static enum Other {
        INSTANCE
    }
}
