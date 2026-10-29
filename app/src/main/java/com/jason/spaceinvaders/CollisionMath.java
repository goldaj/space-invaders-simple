package com.jason.spaceinvaders;

/** Pure geometry shared by both projectile types. No Android dependencies. */
final class CollisionMath {
    private CollisionMath() { }

    /**
     * A projectile travels vertically from previousY to currentY at x.
     * Detect intersection along the whole traveled segment, not just its tip
     * at the end of a frame (which can tunnel through a thin target).
     */
    static boolean crossesRect(float x, float previousY, float currentY,
                               float left, float top, float right, float bottom) {
        if (right < left || bottom < top) return false;
        return x >= left && x <= right
                && Math.max(previousY, currentY) >= top
                && Math.min(previousY, currentY) <= bottom;
    }
}
