package top.kwseeker.quarkus;

import org.junit.jupiter.api.Test;

public class EnclosingClassTest {

    @Test
    public void testEnclosingClass() {
        Class<?> innerClass = OuterClass.InnerClass.class;
        System.out.println(innerClass.getEnclosingClass());
        System.out.println(innerClass.getDeclaringClass().getName());

        OuterClass.InnerClass inner = new OuterClass.InnerClass();
        Class<?> enclosingClass = inner.getClass().getEnclosingClass();
        System.out.println(enclosingClass);
        System.out.println(enclosingClass.getName());
    }

    public static class OuterClass {
        public static class InnerClass {
        }
    }
}
