package lecho.lib.hellocharts.renderer;

import lecho.lib.hellocharts.ChartComputator;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.Chart;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.graphics.Typeface;

public abstract class AbstractChartRenderer implements ChartRenderer {
	public int DEFAULT_LABEL_MARGIN_DP = 4;
	protected Chart chart;
	protected Paint labelPaint = new Paint();
	protected Paint labelBackgroundPaint = new Paint();
	protected RectF labelBackgroundRect = new RectF();
	protected FontMetricsInt fontMetrics = new FontMetricsInt();
	protected Viewport tempMaxViewport = new Viewport();
	protected boolean isViewportCalculationEnabled = true;

	protected float density;
	protected float scaledDensity;

	protected SelectedValue selectedValue = new SelectedValue();

	protected char[] labelBuffer = new char[32];
	protected int labelOffset;
	protected int labelMargin;

	protected boolean isValueLabelBackgroundEnabled;
	protected boolean isValueLabelBackgrountAuto;
	protected int valueLabelBackgroundColor;

	public AbstractChartRenderer(Context context, Chart chart) {
		this.density = context.getResources().getDisplayMetrics().density;
		this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		this.chart = chart;

		labelMargin = Utils.dp2px(density, DEFAULT_LABEL_MARGIN_DP);
		labelOffset = labelMargin;

		labelPaint.setAntiAlias(true);
		labelPaint.setStyle(Paint.Style.FILL);
		labelPaint.setTextAlign(Align.LEFT);
		labelPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		labelPaint.setColor(Color.WHITE);

		labelBackgroundPaint.setAntiAlias(true);
		labelBackgroundPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void initCurrentViewport() {
		if (isViewportCalculationEnabled) {
			ChartComputator computator = chart.getChartComputator();
			computator.setCurrentViewport(computator.getMaximumViewport());
		}
	}

	@Override
	public void initDataAttributes() {
		final ChartData data = chart.getChartData();

		Typeface typeface = chart.getChartData().getValueLabelTypeface();
		if (null != typeface) {
			labelPaint.setTypeface(typeface);
		}

		labelPaint.setTextSize(Utils.sp2px(scaledDensity, data.getValueLabelTextSize()));
		labelPaint.getFontMetricsInt(fontMetrics);

		this.isValueLabelBackgroundEnabled = data.isValueLabelBackgroundEnabled();
		this.isValueLabelBackgrountAuto = data.isValueLabelBackgroundAuto();
		this.valueLabelBackgroundColor = data.getValueLabelBackgroundColor();
		this.labelBackgroundPaint.setColor(valueLabelBackgroundColor);

		// Important - clear selection when data changed.
		selectedValue.clear();

	}

	/**
	 * Draws label text and label background if isValueLabelBackgroundEnabled is true.
	 */
	protected void drawLabelTextAndBackground(Canvas canvas, char[] labelBuffer, int startIndex, int numChars,
			int autoBackgroundColor) {
		final float textX;
		final float textY;

		if (isValueLabelBackgroundEnabled) {

			if (isValueLabelBackgrountAuto) {
				labelBackgroundPaint.setColor(autoBackgroundColor);
			}

			canvas.drawRect(labelBackgroundRect, labelBackgroundPaint);

			textX = labelBackgroundRect.left + labelMargin;
			textY = labelBackgroundRect.bottom - labelMargin;
		} else {
			textX = labelBackgroundRect.left;
			textY = labelBackgroundRect.bottom;
		}

		canvas.drawText(labelBuffer, startIndex, numChars, textX, textY, labelPaint);
	}

	@Override
	public boolean isTouched() {
		return selectedValue.isSet();
	}

	@Override
	public void clearTouch() {
		selectedValue.clear();
	}

	@Override
	public void setMaxViewport(Viewport maxViewport) {
		if (null == maxViewport) {
			initMaxViewport();
		} else {
			this.tempMaxViewport.set(maxViewport);
			chart.getChartComputator().setMaxViewport(maxViewport);
		}
	}

	@Override
	public Viewport getMaxViewport() {
		return tempMaxViewport;
	}

	@Override
	public void setCurrentViewport(Viewport viewport) {
		if (null == viewport) {
			initCurrentViewport();
		} else {
			chart.getChartComputator().setCurrentViewport(viewport);
		}
	}

	@Override
	public Viewport getCurrentViewport() {
		return chart.getChartComputator().getCurrentViewport();
	}

	@Override
	public boolean isViewportCalculationEnabled() {
		return isViewportCalculationEnabled;
	}

	@Override
	public void setViewportCalculationEnabled(boolean isEnabled) {
		this.isViewportCalculationEnabled = isEnabled;
	}

	@Override
	public void selectValue(SelectedValue selectedValue) {
		this.selectedValue.set(selectedValue);
	}

	@Override
	public SelectedValue getSelectedValue() {
		return selectedValue;
	}
}
