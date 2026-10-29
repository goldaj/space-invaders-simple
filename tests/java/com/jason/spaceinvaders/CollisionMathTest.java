package com.jason.spaceinvaders;

/** Standalone JVM regression tests; no Android SDK, JUnit or account needed. */
public final class CollisionMathTest {
    private static int count;

    private static void check(boolean actual, boolean expected, String label) {
        if (actual != expected) throw new AssertionError(label);
        count++;
    }

    public static void main(String[] args) {
        check(CollisionMath.crossesRect(50, 150, 40, 20, 80, 80, 100), true, "ascending tunneling");
        check(CollisionMath.crossesRect(50, 10, 140, 40, 80, 60, 90), true, "descending tunneling");
        check(CollisionMath.crossesRect(19, 150, 40, 20, 80, 80, 100), false, "horizontal miss");
        check(CollisionMath.crossesRect(50, 70, 40, 20, 80, 80, 100), false, "vertical miss");
        check(CollisionMath.crossesRect(50, 150, 101, 20, 80, 80, 100), false, "stops before enemy");
        check(CollisionMath.crossesRect(20, 100, 100, 20, 80, 80, 100), true, "edge-inclusive");
        check(CollisionMath.crossesRect(50, 150, 40, 80, 80, 20, 100), false, "inverted width");
        check(CollisionMath.crossesRect(50, 150, 40, 20, 100, 80, 80), false, "inverted height");
        System.out.println("CollisionMath: " + count + " passed");
    }
}
