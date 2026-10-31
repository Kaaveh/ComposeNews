package ir.composenews.designsystem.component

import io.kotest.core.spec.style.StringSpec
import kotlin.test.assertEquals

class PriceChartTest :
    StringSpec({

        "Given a touch on the plot, When finding the scrub index, Then it picks the nearest point and stays in range" {
            assertEquals(0, scrubIndexAt(x = 0f, plotWidth = 300f, count = 4))
            assertEquals(1, scrubIndexAt(x = 140f, plotWidth = 300f, count = 4))
            assertEquals(3, scrubIndexAt(x = 300f, plotWidth = 300f, count = 4))
            assertEquals(0, scrubIndexAt(x = -20f, plotWidth = 300f, count = 4))
            assertEquals(3, scrubIndexAt(x = 340f, plotWidth = 300f, count = 4))
            assertEquals(0, scrubIndexAt(x = 50f, plotWidth = 300f, count = 1))
        }

        "Given a flat series, When building the axis, Then the price sits mid-band instead of at the bottom" {
            assertEquals(5.0 to 10.0, axisRange(min = 5.0, max = 15.0))
            val (low, span) = axisRange(min = 1.0, max = 1.0)
            assertEquals(1.0, low + span / 2, absoluteTolerance = 1e-9)
            assertEquals(0.02, span, absoluteTolerance = 1e-9)
            assertEquals(-0.5 to 1.0, axisRange(min = 0.0, max = 0.0))
        }

        "Given a gridline step, When picking label decimals, Then neighbouring labels stay distinct" {
            assertEquals(0, axisDecimals(250.0))
            assertEquals(2, axisDecimals(0.775))
            assertEquals(5, axisDecimals(0.00025))
        }

        "Given a marker near an edge, When placing the tooltip, Then it stays inside the plot" {
            assertEquals(40f, tooltipLeft(x = 100f, width = 120f, plotWidth = 300f))
            assertEquals(0f, tooltipLeft(x = 10f, width = 120f, plotWidth = 300f))
            assertEquals(180f, tooltipLeft(x = 295f, width = 120f, plotWidth = 300f))
        }

        "Given a marker under the tooltip, When placing it, Then it drops below the marker" {
            assertEquals(0f, tooltipTop(markerY = 120f, clearance = 10f, height = 50f, chartHeight = 188f))
            assertEquals(30f, tooltipTop(markerY = 20f, clearance = 10f, height = 50f, chartHeight = 188f))
            assertEquals(138f, tooltipTop(markerY = 50f, clearance = 100f, height = 50f, chartHeight = 188f))
        }
    })
