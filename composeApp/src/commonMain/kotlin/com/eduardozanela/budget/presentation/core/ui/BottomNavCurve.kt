package com.eduardozanela.budget.presentation.core.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

// A factor to control the "spread" or width of the curve.
// Larger values make the curve wider.
private const val CURVE_WIDTH_FACTOR = 4.5f

// A factor to control the "depth" of the curve.
// Larger values make the curve dip lower.
private const val CURVE_DEPTH_FACTOR = 1.0f

// A factor determining how "pinched" or "rounded" the curve's connection points are.
// Values closer to 0.5f make it more like a circular arc segment.
// Values closer to 1.0f can make it more "pointy" if not careful.
private const val CONTROL_POINT_FACTOR = 0.5f // Adjusted for smoother connection

class BottomNavCurve(
    private val fabRadius: Float // Expected radius of the FAB or central item
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            // Calculate the total width and depth of the central curve feature
            // based on the FAB's radius and our adjustment factors.
            val curveTotalWidth = fabRadius * CURVE_WIDTH_FACTOR
            val curveTotalDepth = fabRadius * CURVE_DEPTH_FACTOR

            // The Y coordinate where the straight top edge of the BottomBar will be.
            // This is also where the curve starts and ends its dip.
            val topEdgeY = curveTotalDepth * 0.6f // Let the curve dip slightly below the FAB's visual top

            // The Y coordinate for the deepest point of the curve.
            val curveDipY = 0f // Curve will dip towards the top of the composable bounds

            // The horizontal center of the shape
            val centerX = size.width / 2f

            // --- Define Curve Points ---
            // Start of the first (left) curve segment
            val firstCurveStart =
                Offset(centerX - curveTotalWidth / 2f, topEdgeY)
            // End of the first (left) curve segment / Start of the second (right) curve segment
            // This is the deepest point of the curve at the horizontal center.
            val firstCurveEnd_secondCurveStart =
                Offset(centerX, curveDipY)
            // End of the second (right) curve segment
            val secondCurveEnd =
                Offset(centerX + curveTotalWidth / 2f, topEdgeY)

            // --- Define Control Points for Bézier Curves ---
            // Control points determine the shape of the cubic Bézier curves.
            // We'll place them to create a smooth, symmetrical arc.

            // For the first (left) curve:
            // Control Point 1: Influences the curve leaving firstCurveStart
            val firstCurveControl1 = Offset(
                x = firstCurveStart.x + (firstCurveEnd_secondCurveStart.x - firstCurveStart.x) * CONTROL_POINT_FACTOR,
                y = firstCurveStart.y // Keep it horizontal initially for a smoother start
            )
            // Control Point 2: Influences the curve arriving at firstCurveEnd_secondCurveStart
            val firstCurveControl2 = Offset(
                x = firstCurveEnd_secondCurveStart.x - (firstCurveEnd_secondCurveStart.x - firstCurveStart.x) * (1 - CONTROL_POINT_FACTOR),
                y = firstCurveEnd_secondCurveStart.y // Keep it horizontal towards the dip
            )

            // For the second (right) curve:
            // Control Point 1: Influences the curve leaving firstCurveEnd_secondCurveStart
            val secondCurveControl1 = Offset(
                x = firstCurveEnd_secondCurveStart.x + (secondCurveEnd.x - firstCurveEnd_secondCurveStart.x) * (1 - CONTROL_POINT_FACTOR),
                y = firstCurveEnd_secondCurveStart.y // Keep it horizontal from the dip
            )
            // Control Point 2: Influences the curve arriving at secondCurveEnd
            val secondCurveControl2 = Offset(
                x = secondCurveEnd.x - (secondCurveEnd.x - firstCurveEnd_secondCurveStart.x) * CONTROL_POINT_FACTOR,
                y = secondCurveEnd.y // Keep it horizontal towards the end for a smoother connection
            )

            // --- Draw the Path ---
            // Start at the top-left, on the straight edge part
            moveTo(0f, firstCurveStart.y)
            // Line to the start of the first curve
            lineTo(firstCurveStart.x, firstCurveStart.y)

            // First cubic Bézier curve (left side of the dip)
            cubicTo(
                firstCurveControl1.x, firstCurveControl1.y,
                firstCurveControl2.x, firstCurveControl2.y,
                firstCurveEnd_secondCurveStart.x, firstCurveEnd_secondCurveStart.y
            )

            // Second cubic Bézier curve (right side of the dip)
            cubicTo(
                secondCurveControl1.x, secondCurveControl1.y,
                secondCurveControl2.x, secondCurveControl2.y,
                secondCurveEnd.x, secondCurveEnd.y
            )

            // Line from the end of the second curve to the top-right edge
            lineTo(size.width, secondCurveEnd.y)
            // Line down to the bottom-right corner
            lineTo(size.width, size.height)
            // Line across to the bottom-left corner
            lineTo(0f, size.height)
            // Close the path (implicitly connects back to the start)
            close()
        })
    }
}