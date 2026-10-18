package com.jason.spaceinvaders;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUi();
        gameView = new GameView(this);
        setContentView(gameView);
    }

    private void hideSystemUi() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if (gameView != null) gameView.setPaused(false);
    }

    @Override
    protected void onPause() {
        if (gameView != null) gameView.setPaused(true);
        super.onPause();
    }

    public static class GameView extends View {
        private static final int COLS = 6;
        private static final int ROWS = 4;

        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Random random = new Random();
        private final List<Bullet> playerBullets = new ArrayList<>();
        private final List<Bullet> enemyBullets = new ArrayList<>();
        private final List<Enemy> enemies = new ArrayList<>();
        private final List<Star> stars = new ArrayList<>();

        private float width;
        private float height;
        private float playerX;
        private float playerY;
        private float playerW;
        private float playerH;
        private int score;
        private int lives;
        private int level;
        private boolean gameOver;
        private boolean paused;
        private float enemyDirection = 1f;
        private float enemySpeed;
        private long lastFrameNanos;
        private float playerShotTimer;
        private float enemyShotTimer;
        private float flashTimer;

        public GameView(Context context) {
            super(context);
            setFocusable(true);
            setBackgroundColor(Color.rgb(3, 5, 14));
            textPaint.setTypeface(android.graphics.Typeface.create(
                    android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD));
        }

        public void setPaused(boolean value) {
            paused = value;
            lastFrameNanos = System.nanoTime();
            if (!paused) postInvalidateOnAnimation();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            width = w;
            height = h;
            playerW = width * 0.13f;
            playerH = Math.max(26f, height * 0.026f);
            playerY = height - Math.max(90f, height * 0.10f);
            playerX = width / 2f;
            createStars();
            startNewGame();
        }

        private void createStars() {
            stars.clear();
            int count = Math.max(50, (int) (width * height / 18000f));
            for (int i = 0; i < count; i++) {
                stars.add(new Star(random.nextFloat() * width,
                        random.nextFloat() * height,
                        0.5f + random.nextFloat() * 2.0f,
                        14f + random.nextFloat() * 34f));
            }
        }

        private void startNewGame() {
            score = 0;
            lives = 3;
            level = 1;
            gameOver = false;
            playerBullets.clear();
            enemyBullets.clear();
            setupLevel();
            lastFrameNanos = System.nanoTime();
            postInvalidateOnAnimation();
        }

        private void setupLevel() {
            enemies.clear();
            playerBullets.clear();
            enemyBullets.clear();
            enemyDirection = 1f;
            enemySpeed = width * (0.055f + (level - 1) * 0.008f);
            float gapX = width * 0.025f;
            float enemyW = (width - gapX * (COLS + 1)) / COLS;
            float enemyH = enemyW * 0.62f;
            float top = Math.max(110f, height * 0.14f);
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    float left = gapX + c * (enemyW + gapX);
                    float y = top + r * (enemyH + height * 0.025f);
                    enemies.add(new Enemy(left, y, enemyW, enemyH, r));
                }
            }
            playerShotTimer = 0f;
            enemyShotTimer = 0.6f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            long now = System.nanoTime();
            float dt = lastFrameNanos == 0 ? 0f : (now - lastFrameNanos) / 1_000_000_000f;
            lastFrameNanos = now;
            dt = Math.min(dt, 0.035f);

            if (!paused && !gameOver && width > 0) update(dt);
            drawScene(canvas);

            if (!paused) postInvalidateOnAnimation();
        }

        private void update(float dt) {
            updateStars(dt);
            flashTimer = Math.max(0f, flashTimer - dt);

            playerShotTimer -= dt;
            if (playerShotTimer <= 0f) {
                playerBullets.add(new Bullet(playerX, playerY - playerH * 0.9f,
                        -height * 0.72f, false));
                playerShotTimer = Math.max(0.19f, 0.34f - level * 0.012f);
            }

            float minX = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            for (Enemy enemy : enemies) {
                minX = Math.min(minX, enemy.x);
                maxX = Math.max(maxX, enemy.x + enemy.w);
            }
            boolean hitEdge = (enemyDirection > 0 && maxX >= width - 8f)
                    || (enemyDirection < 0 && minX <= 8f);
            if (hitEdge) {
                enemyDirection *= -1f;
                for (Enemy enemy : enemies) enemy.y += height * 0.024f;
            } else {
                for (Enemy enemy : enemies) enemy.x += enemyDirection * enemySpeed * dt;
            }

            enemyShotTimer -= dt;
            if (enemyShotTimer <= 0f && !enemies.isEmpty()) {
                Enemy shooter = chooseBottomEnemy();
                if (shooter != null) {
                    enemyBullets.add(new Bullet(shooter.x + shooter.w / 2f,
                            shooter.y + shooter.h, height * (0.34f + level * 0.012f), true));
                }
                enemyShotTimer = Math.max(0.30f, 0.95f - level * 0.045f)
                        + random.nextFloat() * 0.42f;
            }

            moveBullets(playerBullets, dt);
            moveBullets(enemyBullets, dt);
            handleCollisions();

            boolean invaded = false;
            for (Enemy enemy : enemies) {
                if (enemy.y + enemy.h >= playerY - playerH) {
                    invaded = true;
                    break;
                }
            }
            if (invaded) {
                loseLife();
                if (!gameOver) setupLevel();
            }

            if (enemies.isEmpty() && !gameOver) {
                score += 250 * level;
                level++;
                setupLevel();
            }
        }

        private void updateStars(float dt) {
            for (Star star : stars) {
                star.y += star.speed * dt;
                if (star.y > height) {
                    star.y = 0;
                    star.x = random.nextFloat() * width;
                }
            }
        }

        private Enemy chooseBottomEnemy() {
            List<Enemy> candidates = new ArrayList<>();
            for (Enemy enemy : enemies) {
                boolean blocked = false;
                float center = enemy.x + enemy.w / 2f;
                for (Enemy other : enemies) {
                    float otherCenter = other.x + other.w / 2f;
                    if (other.y > enemy.y && Math.abs(otherCenter - center) < enemy.w * 0.55f) {
                        blocked = true;
                        break;
                    }
                }
                if (!blocked) candidates.add(enemy);
            }
            return candidates.isEmpty() ? enemies.get(random.nextInt(enemies.size()))
                    : candidates.get(random.nextInt(candidates.size()));
        }

        private void moveBullets(List<Bullet> bullets, float dt) {
            Iterator<Bullet> it = bullets.iterator();
            while (it.hasNext()) {
                Bullet bullet = it.next();
                bullet.y += bullet.speedY * dt;
                if (bullet.y < -40f || bullet.y > height + 40f) it.remove();
            }
        }

        private void handleCollisions() {
            Iterator<Bullet> playerIt = playerBullets.iterator();
            while (playerIt.hasNext()) {
                Bullet bullet = playerIt.next();
                Enemy hit = null;
                for (Enemy enemy : enemies) {
                    if (enemy.bounds().contains(bullet.x, bullet.y)) {
                        hit = enemy;
                        break;
                    }
                }
                if (hit != null) {
                    enemies.remove(hit);
                    playerIt.remove();
                    score += 10 * (ROWS - hit.row) * level;
                }
            }

            RectF playerBounds = new RectF(playerX - playerW / 2f, playerY - playerH,
                    playerX + playerW / 2f, playerY + playerH);
            Iterator<Bullet> enemyIt = enemyBullets.iterator();
            while (enemyIt.hasNext()) {
                Bullet bullet = enemyIt.next();
                if (playerBounds.contains(bullet.x, bullet.y)) {
                    enemyIt.remove();
                    loseLife();
                    break;
                }
            }
        }

        private void loseLife() {
            if (flashTimer > 0f || gameOver) return;
            lives--;
            flashTimer = 0.65f;
            enemyBullets.clear();
            playerX = width / 2f;
            if (lives <= 0) gameOver = true;
        }

        private void drawScene(Canvas canvas) {
            canvas.drawColor(Color.rgb(3, 5, 14));
            drawStars(canvas);
            drawHud(canvas);
            drawEnemies(canvas);
            drawBullets(canvas);
            drawPlayer(canvas);
            if (gameOver) drawGameOver(canvas);
            if (paused && !gameOver) drawPaused(canvas);
        }

        private void drawStars(Canvas canvas) {
            paint.setColor(Color.rgb(185, 205, 255));
            for (Star star : stars) canvas.drawCircle(star.x, star.y, star.radius, paint);
        }

        private void drawHud(Canvas canvas) {
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(Math.max(24f, width * 0.055f));
            textPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("SCORE " + score, width * 0.04f, height * 0.065f, textPaint);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("VIES " + lives, width * 0.96f, height * 0.065f, textPaint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(Math.max(17f, width * 0.038f));
            textPaint.setColor(Color.rgb(120, 220, 255));
            canvas.drawText("NIVEAU " + level, width / 2f, height * 0.105f, textPaint);
        }

        private void drawPlayer(Canvas canvas) {
            if (flashTimer > 0f && ((int) (flashTimer * 14f)) % 2 == 0) return;
            paint.setColor(Color.rgb(70, 255, 165));
            RectF body = new RectF(playerX - playerW / 2f, playerY,
                    playerX + playerW / 2f, playerY + playerH);
            canvas.drawRoundRect(body, playerH * 0.25f, playerH * 0.25f, paint);
            float[] ship = {
                    playerX, playerY - playerH * 1.25f,
                    playerX - playerW * 0.32f, playerY,
                    playerX + playerW * 0.32f, playerY
            };
            android.graphics.Path path = new android.graphics.Path();
            path.moveTo(ship[0], ship[1]);
            path.lineTo(ship[2], ship[3]);
            path.lineTo(ship[4], ship[5]);
            path.close();
            canvas.drawPath(path, paint);
            paint.setColor(Color.rgb(160, 255, 230));
            canvas.drawRect(playerX - playerW * 0.10f, playerY - playerH * 0.55f,
                    playerX + playerW * 0.10f, playerY + playerH * 0.25f, paint);
        }

        private void drawEnemies(Canvas canvas) {
            for (Enemy enemy : enemies) {
                int color;
                if (enemy.row == 0) color = Color.rgb(255, 95, 175);
                else if (enemy.row == 1) color = Color.rgb(255, 190, 70);
                else color = Color.rgb(120, 215, 255);
                paint.setColor(color);
                float x = enemy.x;
                float y = enemy.y;
                float w = enemy.w;
                float h = enemy.h;
                canvas.drawRect(x + w * .18f, y, x + w * .82f, y + h * .18f, paint);
                canvas.drawRect(x + w * .08f, y + h * .18f, x + w * .92f, y + h * .72f, paint);
                canvas.drawRect(x, y + h * .35f, x + w, y + h * .56f, paint);
                canvas.drawRect(x + w * .14f, y + h * .72f, x + w * .32f, y + h, paint);
                canvas.drawRect(x + w * .68f, y + h * .72f, x + w * .86f, y + h, paint);
                paint.setColor(Color.rgb(3, 5, 14));
                canvas.drawRect(x + w * .27f, y + h * .34f, x + w * .39f, y + h * .48f, paint);
                canvas.drawRect(x + w * .61f, y + h * .34f, x + w * .73f, y + h * .48f, paint);
            }
        }

        private void drawBullets(Canvas canvas) {
            paint.setStrokeWidth(Math.max(5f, width * 0.011f));
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(Color.rgb(125, 255, 210));
            for (Bullet bullet : playerBullets) {
                canvas.drawLine(bullet.x, bullet.y + height * .014f, bullet.x, bullet.y, paint);
            }
            paint.setColor(Color.rgb(255, 90, 110));
            for (Bullet bullet : enemyBullets) {
                canvas.drawLine(bullet.x, bullet.y - height * .012f, bullet.x, bullet.y, paint);
            }
        }

        private void drawGameOver(Canvas canvas) {
            paint.setColor(Color.argb(185, 0, 0, 0));
            canvas.drawRect(0, 0, width, height, paint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.rgb(255, 95, 125));
            textPaint.setTextSize(Math.max(46f, width * 0.115f));
            canvas.drawText("GAME OVER", width / 2f, height * 0.43f, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(Math.max(24f, width * 0.055f));
            canvas.drawText("Score : " + score, width / 2f, height * 0.50f, textPaint);
            textPaint.setColor(Color.rgb(135, 225, 255));
            textPaint.setTextSize(Math.max(18f, width * 0.042f));
            canvas.drawText("Touchez l'écran pour rejouer", width / 2f,
                    height * 0.58f, textPaint);
        }

        private void drawPaused(Canvas canvas) {
            paint.setColor(Color.argb(120, 0, 0, 0));
            canvas.drawRect(0, 0, width, height, paint);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(Math.max(36f, width * 0.09f));
            canvas.drawText("PAUSE", width / 2f, height / 2f, textPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (gameOver) {
                    startNewGame();
                    return true;
                }
                movePlayer(event.getX());
                return true;
            }
            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                movePlayer(event.getX());
                return true;
            }
            return true;
        }

        private void movePlayer(float x) {
            playerX = Math.max(playerW / 2f + 5f, Math.min(width - playerW / 2f - 5f, x));
        }

        private static class Bullet {
            float x;
            float y;
            float speedY;
            boolean enemy;

            Bullet(float x, float y, float speedY, boolean enemy) {
                this.x = x;
                this.y = y;
                this.speedY = speedY;
                this.enemy = enemy;
            }
        }

        private static class Enemy {
            float x;
            float y;
            float w;
            float h;
            int row;

            Enemy(float x, float y, float w, float h, int row) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
                this.row = row;
            }

            RectF bounds() {
                return new RectF(x, y, x + w, y + h);
            }
        }

        private static class Star {
            float x;
            float y;
            float radius;
            float speed;

            Star(float x, float y, float radius, float speed) {
                this.x = x;
                this.y = y;
                this.radius = radius;
                this.speed = speed;
            }
        }
    }
}
