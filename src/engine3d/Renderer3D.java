package engine3d;

import engine3d.mesh.Triangle;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.gfx.images.Image;

import java.util.ArrayList;

/**
 * The graphics 3d renderer. Contains all rendering methods for drawing triangles needed for
 * the Pipeline class.
 * It extends from the Renderer class.
 *
 * @class Renderer3D
 * @author Sergio Martí Torregrosa
 * @date 18/08/2020
 */
public class Renderer3D extends Renderer {

    /**
     * The depth buffer
     */
    private final float[] depthBuffer;

    /**
     * The render flag
     */
    private RenderFlags renderFlag = RenderFlags.RENDER_TEXTURED_SHADOW;

    /**
     * The constructor
     */
    public Renderer3D(int[] p, int pW, int pH) {
        super(p, pW, pH);
        depthBuffer = new float[pW * pH];
        clearDepthBuffer();
    }

    /**
     * This method clears all the depth buffer, and sets all the values to 0
     */
    public void clearDepthBuffer() {
        for ( int i = 0; i < getW() * getH(); i++ ) {
            depthBuffer[i] = 0.0f;
        }
    }

    /**
     * This method is really important. This setPixel method has in account
     * the depthBuffer, so all methods what use this setPixel have to have
     * an associate depth value for the pixel which is going to be drawn
     *
     * All methods of this class (the Renderer3D) have to rely on this
     *
     * @param x the x coordinate on screen to draw the pixel
     * @param y the y coordinate on screen to drawn the pixel
     * @param depth the associate depth value of this pixel
     * @param color the color what is wanted to draw
     */
    private void setPixel(int x, int y, float depth, int color) {
        if ( depth > depthBuffer[y * getW() + x] ) {
            super.setPixel(x, y, color);
            depthBuffer[y * getW() + x] = depth;
        }
    }

    /**
     * This method draws a simple straight vertical line, is needed for
     * the draw line primitive. Is a way to split the method
     * The straight lines idea by gurkanctn
     * @param x1 the start x coordinate of the line
     * @param y1 the start y coordinate of the line
     * @param depth1 the depth value associate with the first pixel of the line
     * @param y2 the end y coordinate of the line
     * @param depth2 the depth value associate with the second pixel of the line
     * @param color the color of the line
     */
    private void drawStraightVerticalLine(int x1, int y1, float depth1, int y2, float depth2, int color) {
        int y, temp;
        float distDepth, increaseDepth, temporalFloat;

        distDepth = depth2 - depth1;

        if ( y2 < y1 ) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        if ( distDepth == 0 ) { // The line has the same depth in all

            for ( y = y1; y <= y2; y++ ) {
                setPixel(x1, y, depth1, color);
            }

        } else { // The line has a different depth

            if ( depth2 < depth1 ) {
                temporalFloat = depth1;
                depth1 = depth2;
                depth2 = temporalFloat;
            }

            increaseDepth = depth2 / depth1;

            for ( y = y1; y <= y2; y++ ) {
                setPixel(x1, y, y * increaseDepth, color);
            }
        }
    }

    /**
     * This method draws a simple straight horizontal line, is needed for
     * the draw line primitive. Is a way to split the method
     * The straight lines idea by gurkanctn
     * @param x1 the start x coordinate of the line
     * @param y1 the start y coordinate of the line
     * @param depth1 the depth value associate with the first pixel of the line
     * @param x2 the end x coordinate of the line
     * @param depth2 the depth value associate with the second pixel of the line
     * @param color the color of the line
     */
    private void drawStraightHorizontalLine(int x1, int y1, float depth1, int x2, float depth2, int color) {
        int x, temp;
        float distDepth, increaseDepth, temporalFloat;

        distDepth = depth2 - depth1;

        if ( x2 < x1 ) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }

        if ( distDepth == 0 ) { // The line has the same depth in all

            for ( x = x1; x <= x2; x++ ) {
                setPixel(x, y1, depth1, color);
            }

        } else { // The line has different depth

            if ( depth2 < depth1 ) {
                temporalFloat = depth1;
                depth1 = depth2;
                depth2 = temporalFloat;
            }

            increaseDepth = depth2 / depth1;

            for ( x = x1; x <= x2; x++ ) {
                setPixel(x, y1, increaseDepth * x, color);
            }

        }
    }

    /**
     * This method draws a line on screen, but is has in account the depthBuffer
     * of the renderer3D
     * @param x1 the start x coordinate of the line
     * @param y1 the start y coordinate of the line
     * @param depth1 the depth value associate with the first pixel of the line
     * @param x2 the end x coordinate of the line
     * @param y2 the end y coordinate of the line
     * @param depth2 the depth value associate with the second pixel of the line
     * @param color the color of the line
     */
    public void drawLine(int x1, int y1, float depth1, int x2, int y2, float depth2, int color) {
        int x, y, distX, distY;

        distX = x2 - x1;
        distY = y2 - y1;

        // Line is vertical
        if ( distX == 0 ) {
            drawStraightVerticalLine(x1, y1, depth1, y2, depth2, color);
            return;
        }

        // Line is horizontal
        if ( distY == 0 ) {
            drawStraightHorizontalLine(x1, y1, depth1, x2, depth2, color);
            return;
        }

        // Line is diagonal
        int absDistX1 = Math.abs(distX);
        int absDistY1 = Math.abs(distY);

        if ( absDistY1 <= absDistX1 ) {

            int px = 2 * absDistY1 - absDistX1;
            int endX;

            if ( distX >= 0 ) {
                x = x1;
                y = y1;
                endX = x2;
            } else {
                x = x2;
                y = y2;
                endX = x1;
            }

            setPixel(x, y, color);

            for (; x < endX; x++ ) {

                if ( px < 0 ) {
                    px = px + 2 * absDistY1;
                } else {
                    if ( ( distX < 0 && distY < 0 ) || ( distX > 0 && distY > 0 ) ) {
                        y = y + 1;
                    } else {
                        y = y - 1;
                    }
                    px = px + 2 * (absDistY1 - absDistX1);
                }

                setPixel(x, y, color);
            }

        } else {

            int py = 2 * absDistX1 - absDistY1;
            int endY;

            if ( distY >= 0 ) {
                x = x1;
                y = y1;
                endY = y2;
            } else {
                x = x2;
                y = y2;
                endY = y1;
            }

            setPixel(x, y, color);

            for (; y < endY; y++) {

                if ( py <= 0 ) {
                    py = py + 2 * absDistX1;
                } else {
                    if ( ( distX < 0 && distY < 0 ) || ( distX > 0 && distY > 0 ) ) {
                        x = x + 1;
                    } else {
                        x = x - 1;
                    }
                    py = py + 2 * (absDistX1 - absDistY1);
                }

                setPixel(x, y, color);
            }
        }
    }

    /**
     * This method pretends to change the input color by the light source
     * @param inputColor the input color to change
     * @param light the light
     * @return returns the color but more brigtness o darkness
     */
    private int calculateColor(int inputColor, float light) {
        int a = ((inputColor >> 24) & 0xff);
        int r = ((inputColor >> 16) & 0xff);
        int g = ((inputColor >>  8) & 0xff);
        int b = ((inputColor      ) & 0xff);
        light = Math.max(0.1f, light);
        int newR = (int)(light * r);
        int newG = (int)(light * g);
        int newB = (int)(light * b);
        return (a << 24 | newR << 16 | newG << 8 | newB);
    }

    private void setPixelForTriangle(int y, int x, float w, float brightness, int color) {
        // tex_w = (tex_w == 0.0f)? 1.0f : tex_w;

        color = calculateColor(color, brightness);

        try {
            if ( w > depthBuffer[y * getW() + x] ) {
                setPixel(x, y, color);
                depthBuffer[y * getW() + x] = w;
            }
        } catch ( ArrayIndexOutOfBoundsException e ) {
            String errorMessage = "X: " + x + " Y: " + y + " outside of " + getW() + "x" + getH();
            System.out.println("Set pixel Error: " + errorMessage + e.getMessage());
        }
    }

    /**
     * This method sets the correspondent pixel of the triangle to
     * the pixel from the texture bitmap.
     * @param y the y value of the pixel
     * @param x the x value of the pixel
     * @param tex_u value x fot the texture
     * @param tex_v value y for the texture
     * @param tex_w value z for the texture, needed for perspective projection
     * @param texture the texture, which is a bitmap image
     */
    private void setPixelForTexturedTriangle(int y, int x,
                                             float tex_u, float tex_v, float tex_w,
                                             float brightness,
                                             Image texture) {
        int color;
        //tex_w = (tex_w == 0.0f)? 1.0f : tex_w;

        color = texture.getSample((tex_u / tex_w), (tex_v / tex_w));
        color = calculateColor(color, brightness);

        try {
            if ( tex_w > depthBuffer[y * getW() + x] ) {
                setPixel(x, y, color);
                depthBuffer[y * getW() + x] = tex_w;
            }
        } catch ( ArrayIndexOutOfBoundsException e ) {
            String errorMessage = "X: " + x + " Y: " + y + " outside of " + getW() + "x" + getH();
            System.out.println("Set pixel Error: " + errorMessage + e.getMessage());
        }

    }

    /**
     * This method draws a textured triangle. Code copied from Javidx9 video
     * Code-It-Yourself! 3D Graphics Engine Part #4 - Texturing & depth buffers.
     * https://www.youtube.com/watch?v=nBzCS-Y0FcY&t=1722s
     * @param x1 x value for point one of triangle
     * @param y1 y value for point one of triangle
     * @param u1 x value for texture point one of triangle
     * @param v1 y value for texture point one of triangle
     * @param w1 z value for texture point one of triangle
     * @param x2 x value for point two of triangle
     * @param y2 y value for point two of triangle
     * @param u2 x value for texture point two of triangle
     * @param v2 y value for texture point two of triangle
     * @param w2 z value for texture point two of triangle
     * @param x3 x value for point three of triangle
     * @param y3 y value for point three of triangle
     * @param u3 x value for texture point three triangle
     * @param v3 y value for texture point three triangle
     * @param w3 z value for texture point three triangle
     * @param brightness the brightness of the triangle
     * @param texture the image texture
     */
    private void drawTexturedShadowTriangle(
            int x1, int y1, float u1, float v1, float w1,
            int x2, int y2, float u2, float v2, float w2,
            int x3, int y3, float u3, float v3, float w3,
            float brightness,
            Image texture) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;

            float tempFloat = u1;
            u1 = u2;
            u2 = tempFloat;

            tempFloat = v1;
            v1 = v2;
            v2 = tempFloat;

            tempFloat = w1;
            w1 = w2;
            w2 = tempFloat;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;

            float tempFloat = u1;
            u1 = u3;
            u3 = tempFloat;

            tempFloat = v1;
            v1 = v3;
            v3 = tempFloat;

            tempFloat = w1;
            w1 = w3;
            w3 = tempFloat;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;

            float tempFloat = u2;
            u2 = u3;
            u3 = tempFloat;

            tempFloat = v2;
            v2 = v3;
            v3 = tempFloat;

            tempFloat = w2;
            w2 = w3;
            w3 = tempFloat;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        float dv1 = v2 - v1;
        float du1 = u2 - u1;
        float dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        float dv2 = v3 - v1;
        float du2 = u3 - u1;
        float dw2 = w3 - w1;

        float tex_u, tex_v, tex_w;

        float dax_step = 0, dbx_step = 0, du1_step = 0, dv1_step = 0, du2_step = 0, dv2_step = 0, dw1_step = 0, dw2_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy2 != 0 ) {
            du2_step = du2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dv2_step = dv2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dw2_step = dw2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ ) {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u1 + (float)(i - y1) * du1_step;
                float tex_sv = v1 + (float)(i - y1) * dv1_step;
                float tex_sw = w1 + (float)(i - y1) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTexturedTriangle(i, j, tex_u, tex_v, tex_w, brightness, texture);

                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        du1_step = 0;
        dv1_step = 0;
        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy1 != 0 ) {
            for (int i = y2; i <= y3; i++) {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u2 + (float)(i - y2) * du1_step;
                float tex_sv = v2 + (float)(i - y2) * dv1_step;
                float tex_sw = w2 + (float)(i - y2) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTexturedTriangle(i, j, tex_u, tex_v, tex_w, brightness, texture);

                    t += tstep;
                }
            }
        }
    }

    /**
     * This method draws a filled triangle. Code copied from Javidx9 video
     * Code-It-Yourself! 3D Graphics Engine Part #4 - Texturing & depth buffers.
     * https://www.youtube.com/watch?v=nBzCS-Y0FcY&t=1722s
     * @param x1 x value for point one of triangle
     * @param y1 y value for point one of triangle
     * @param u1 x value for texture point one of triangle
     * @param v1 y value for texture point one of triangle
     * @param w1 z value for texture point one of triangle
     * @param x2 x value for point two of triangle
     * @param y2 y value for point two of triangle
     * @param u2 x value for texture point two of triangle
     * @param v2 y value for texture point two of triangle
     * @param w2 z value for texture point two of triangle
     * @param x3 x value for point three of triangle
     * @param y3 y value for point three of triangle
     * @param u3 x value for texture point three triangle
     * @param v3 y value for texture point three triangle
     * @param w3 z value for texture point three triangle
     * @param brightness the brightness of the triangle
     * @param color the color of the triangle
     */
    private void drawFillTriangle(
            int x1, int y1, float u1, float v1, float w1,
            int x2, int y2, float u2, float v2, float w2,
            int x3, int y3, float u3, float v3, float w3,
            float brightness,
            int color) {
        if (y2 < y1) {
            int tempInteger = y1;
            y1 = y2;
            y2 = tempInteger;

            tempInteger = x1;
            x1 = x2;
            x2 = tempInteger;

            float tempFloat = u1;
            u1 = u2;
            u2 = tempFloat;

            tempFloat = v1;
            v1 = v2;
            v2 = tempFloat;

            tempFloat = w1;
            w1 = w2;
            w2 = tempFloat;
        }

        if (y3 < y1) {
            int tempInteger = y1;
            y1 = y3;
            y3 = tempInteger;

            tempInteger = x1;
            x1 = x3;
            x3 = tempInteger;

            float tempFloat = u1;
            u1 = u3;
            u3 = tempFloat;

            tempFloat = v1;
            v1 = v3;
            v3 = tempFloat;

            tempFloat = w1;
            w1 = w3;
            w3 = tempFloat;
        }

        if (y3 < y2) {
            int tempInteger = y2;
            y2 = y3;
            y3 = tempInteger;

            tempInteger = x2;
            x2 = x3;
            x3 = tempInteger;

            float tempFloat = u2;
            u2 = u3;
            u3 = tempFloat;

            tempFloat = v2;
            v2 = v3;
            v3 = tempFloat;

            tempFloat = w2;
            w2 = w3;
            w3 = tempFloat;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        float dv1 = v2 - v1;
        float du1 = u2 - u1;
        float dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        float dv2 = v3 - v1;
        float du2 = u3 - u1;
        float dw2 = w3 - w1;

        float tex_u, tex_v, tex_w;

        float dax_step = 0, dbx_step = 0, du1_step = 0, dv1_step = 0, du2_step = 0, dv2_step = 0, dw1_step = 0, dw2_step = 0;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy2 != 0 ) {
            du2_step = du2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dv2_step = dv2 / (float)Math.abs(dy2);
        }
        if ( dy2 != 0 ) {
            dw2_step = dw2 / (float)Math.abs(dy2);
        }

        if ( dy1 != 0 ) {
            for ( int i = y1; i <= y2; i++ ) {
                int ax = (int)(x1 + (float)(i - y1) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u1 + (float)(i - y1) * du1_step;
                float tex_sv = v1 + (float)(i - y1) * dv1_step;
                float tex_sw = w1 + (float)(i - y1) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if ( ax > bx ) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTriangle(i, j, tex_w, brightness, color);

                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if ( dy1 != 0 ) {
            dax_step = dx1 / (float)Math.abs(dy1);
        }
        if ( dy2 != 0 ) {
            dbx_step = dx2 / (float)Math.abs(dy2);
        }

        du1_step = 0;
        dv1_step = 0;
        if ( dy1 != 0 ) {
            du1_step = du1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dv1_step = dv1 / (float)Math.abs(dy1);
        }
        if ( dy1 != 0 ) {
            dw1_step = dw1 / (float)Math.abs(dy1);
        }

        if ( dy1 != 0 ) {
            for (int i = y2; i <= y3; i++) {
                int ax = (int)(x2 + (float)(i - y2) * dax_step);
                int bx = (int)(x1 + (float)(i - y1) * dbx_step);

                float tex_su = u2 + (float)(i - y2) * du1_step;
                float tex_sv = v2 + (float)(i - y2) * dv1_step;
                float tex_sw = w2 + (float)(i - y2) * dw1_step;

                float tex_eu = u1 + (float)(i - y1) * du2_step;
                float tex_ev = v1 + (float)(i - y1) * dv2_step;
                float tex_ew = w1 + (float)(i - y1) * dw2_step;

                if (ax > bx) {
                    int tempInteger = ax;
                    ax = bx;
                    bx = tempInteger;

                    float tempFloat = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempFloat;

                    tempFloat = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempFloat;

                    tempFloat = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempFloat;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float)(bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    setPixelForTriangle(i, j, tex_w, brightness, color);

                    t += tstep;
                }
            }
        }
    }

    private void drawFlatTriangle(Triangle triangle, int color) {
        drawFillTriangle(
                (int)triangle.getP()[0].getX(), (int)triangle.getP()[0].getY(),
                (int)triangle.getT()[0].getX(), (int)triangle.getT()[0].getY(), (int)triangle.getT()[0].getZ(),
                (int)triangle.getP()[1].getX(), (int)triangle.getP()[1].getY(),
                (int)triangle.getT()[1].getX(), (int)triangle.getT()[1].getY(), (int)triangle.getT()[1].getZ(),
                (int)triangle.getP()[2].getX(), (int)triangle.getP()[2].getY(),
                (int)triangle.getT()[2].getX(), (int)triangle.getT()[2].getY(), (int)triangle.getT()[2].getZ(),
                triangle.getBrightness(),
                color
        );
        drawTriangle(
                (int)triangle.getP()[0].getX(), (int)triangle.getP()[0].getY(),
                (int)triangle.getP()[1].getX(), (int)triangle.getP()[1].getY(),
                (int)triangle.getP()[2].getX(), (int)triangle.getP()[2].getY(),
                //0xff000000
                0xffffffff
        );
    }

    /**
     * Este método dibuja en pantalla un triangulo relleno.
     * @param triangle el objeto triangulo a dibujar.
     */
    private void drawFlatTriangle(Triangle triangle) {
        drawFlatTriangle(triangle, triangle.getColor());
    }

    private void drawFlatSmoothTriangle(Triangle triangle, int color) {
        drawFillTriangle(
                (int)triangle.getP()[0].getX(), (int)triangle.getP()[0].getY(),
                (int)triangle.getP()[1].getX(), (int)triangle.getP()[1].getY(),
                (int)triangle.getP()[2].getX(), (int)triangle.getP()[2].getY(),
                color
        );
    }

    /**
     * Este método dibuja un triangulo plano relleno, pero sin un borde negro.
     * @param triangle el objeto triangulo a dibujar.
     */
    private void drawFlatSmoothTriangle(Triangle triangle) {
        drawFlatSmoothTriangle(triangle, triangle.getColor());
    }

    private void drawWireTriangle(Triangle triangle, int color) {
        drawTriangle(
                (int)triangle.getP()[0].getX(), (int)triangle.getP()[0].getY(),
                (int)triangle.getP()[1].getX(), (int)triangle.getP()[1].getY(),
                (int)triangle.getP()[2].getX(), (int)triangle.getP()[2].getY(),
                color
        );
    }

    /**
     * Este método dibuja en pantalla un triangulo, pero simplemente
     * las lineas que lo delimitan.
     * @param triangle el objeto triangulo a dibujar.
     */
    private void drawWireTriangle(Triangle triangle) {
        drawWireTriangle(triangle, triangle.getColor());
    }

     /**
     * This method is an overload of the drawTexturedTriangle, for this reason, it passes as a
     * parameter a triangle, to clarify the code.
     * @param triangle the triangle to draw
     * @param texture the image texture
     */
    private void drawTexturedShadowTriangle(Triangle triangle, Image texture) {
        drawTexturedShadowTriangle(
                (int) triangle.getP()[0].getX(),
                (int) triangle.getP()[0].getY(),

                triangle.getT()[0].getX(),
                triangle.getT()[0].getY(),
                triangle.getT()[0].getZ(),

                (int) triangle.getP()[1].getX(),
                (int) triangle.getP()[1].getY(),

                triangle.getT()[1].getX(),
                triangle.getT()[1].getY(),
                triangle.getT()[1].getZ(),

                (int) triangle.getP()[2].getX(),
                (int) triangle.getP()[2].getY(),

                triangle.getT()[2].getX(),
                triangle.getT()[2].getY(),
                triangle.getT()[2].getZ(),

                triangle.getBrightness(),
                texture
        );
    }

    /**
     * This method is an overload of the drawTexturedTriangle. It doesn't
     * have in account the brightness of the triangle, show the
     * triangle isn't shadowed
     * @param triangle the triangle to draw
     * @param texture the image texture
     */
    private void drawTexturedTriangle(Triangle triangle, Image texture) {
        drawTexturedShadowTriangle(
                (int) triangle.getP()[0].getX(),
                (int) triangle.getP()[0].getY(),

                triangle.getT()[0].getX(),
                triangle.getT()[0].getY(),
                triangle.getT()[0].getZ(),

                (int) triangle.getP()[1].getX(),
                (int) triangle.getP()[1].getY(),

                triangle.getT()[1].getX(),
                triangle.getT()[1].getY(),
                triangle.getT()[1].getZ(),

                (int) triangle.getP()[2].getX(),
                (int) triangle.getP()[2].getY(),

                triangle.getT()[2].getX(),
                triangle.getT()[2].getY(),
                triangle.getT()[2].getZ(),

                1.0f,
                texture
        );
    }

    /**
     * This method draws a triangle in any form what is would to draw
     * @param triangle triangle to render.
     */
    private void renderTexturedTriangle(Triangle triangle, Image texture) {
        switch ( renderFlag ) {
            case RENDER_FLAT:
                drawFlatTriangle(triangle);
                break;
            case RENDER_SMOOTH_FLAT:
                drawFlatSmoothTriangle(triangle);
                break;
            case RENDER_WIRE:
                drawWireTriangle(triangle);
                break;
            case RENDER_TEXTURED:
                drawTexturedTriangle(triangle, texture);
                drawWireTriangle(triangle);
                break;
            case RENDER_FULL_TEXTURED:
                drawTexturedTriangle(triangle, texture);
                break;
            case RENDER_TEXTURED_SHADOW:
                drawTexturedShadowTriangle(triangle, texture);
                drawWireTriangle(triangle);
                break;
            case RENDER_FULL_TEXTURED_SHADOW:
                drawTexturedShadowTriangle(triangle, texture);
                break;
        }
    }

    private void renderNonTexturedTriangle(Triangle triangle, int color) {
        switch ( renderFlag ) {
            case RENDER_FLAT: case RENDER_TEXTURED: case RENDER_TEXTURED_SHADOW:
                drawFlatTriangle(triangle, color);
                break;
            case RENDER_SMOOTH_FLAT: case RENDER_FULL_TEXTURED: case RENDER_FULL_TEXTURED_SHADOW:
                drawFlatSmoothTriangle(triangle, color);
                break;
            case RENDER_WIRE:
                drawWireTriangle(triangle, color);
                break;
        }
    }

    private void renderNonTexturedTriangle(Triangle triangle) {
        switch ( renderFlag ) {
            case RENDER_FLAT: case RENDER_TEXTURED: case RENDER_TEXTURED_SHADOW:
                drawFlatTriangle(triangle);
                break;
            case RENDER_SMOOTH_FLAT: case RENDER_FULL_TEXTURED: case RENDER_FULL_TEXTURED_SHADOW:
                drawFlatSmoothTriangle(triangle);
                break;
            case RENDER_WIRE:
                drawWireTriangle(triangle);
                break;
        }
    }

    /**
     * This method goes over an ArrayList filled with triangles and draws them depending on the render
     * flag which is active.
     * @param triangles triangles to drawn. In this case, the projected and rasterized triangles.
     */
    void renderTriangles(ArrayList<Triangle> triangles, Image texture) {
        for ( Triangle triangle : triangles ) {
            renderTexturedTriangle(triangle, texture);
        }
    }

    void renderTriangles(ArrayList<Triangle> triangles, int color) {
        for ( Triangle triangle : triangles ) {
            renderNonTexturedTriangle(triangle, color);
        }
    }

    void renderTriangles(ArrayList<Triangle> triangles) {
        for ( Triangle triangle : triangles ) {
            renderNonTexturedTriangle(triangle);
        }
    }

    public RenderFlags getRenderFlag() {
        return renderFlag;
    }

    public void setRenderFlag(RenderFlags renderFlag) {
        this.renderFlag = renderFlag;
    }

}
