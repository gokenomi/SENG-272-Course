package iso15939.ui;

import iso15939.model.DimensionResult;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class RadarChartPanel extends JPanel {
    private List<DimensionResult> results = new ArrayList<>();

    public RadarChartPanel() {
        setPreferredSize(new Dimension(410, 360));
        setBackground(Color.WHITE);
    }

    public void setResults(List<DimensionResult> results) {
        this.results = new ArrayList<>(results);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int count = results.size();
        if (count < 3) {
            g2.setColor(Color.GRAY);
            g2.drawString("Radar chart needs at least three dimensions.", 24, 40);
            g2.dispose();
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 2 - 58;

        g2.setStroke(new BasicStroke(1f));
        for (int level = 1; level <= 5; level++) {
            Path2D grid = new Path2D.Double();
            double levelRadius = radius * (level / 5.0);
            for (int i = 0; i < count; i++) {
                Point p = pointFor(centerX, centerY, levelRadius, i, count);
                if (i == 0) {
                    grid.moveTo(p.x, p.y);
                } else {
                    grid.lineTo(p.x, p.y);
                }
            }
            grid.closePath();
            g2.setColor(new Color(218, 224, 232));
            g2.draw(grid);
        }

        for (int i = 0; i < count; i++) {
            Point edge = pointFor(centerX, centerY, radius, i, count);
            g2.setColor(new Color(203, 211, 221));
            g2.drawLine(centerX, centerY, edge.x, edge.y);
            drawCenteredLabel(g2, results.get(i).getDimension().getName(), edge.x, edge.y, centerX, centerY);
        }

        Path2D scoreShape = new Path2D.Double();
        for (int i = 0; i < count; i++) {
            double scoreRadius = radius * (results.get(i).getScore() / 5.0);
            Point p = pointFor(centerX, centerY, scoreRadius, i, count);
            if (i == 0) {
                scoreShape.moveTo(p.x, p.y);
            } else {
                scoreShape.lineTo(p.x, p.y);
            }
        }
        scoreShape.closePath();

        g2.setColor(new Color(35, 118, 184, 70));
        g2.fill(scoreShape);
        g2.setColor(new Color(35, 118, 184));
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(scoreShape);
        g2.dispose();
    }

    private Point pointFor(int centerX, int centerY, double radius, int index, int count) {
        double angle = -Math.PI / 2 + (2 * Math.PI * index / count);
        int x = (int) Math.round(centerX + Math.cos(angle) * radius);
        int y = (int) Math.round(centerY + Math.sin(angle) * radius);
        return new Point(x, y);
    }

    private void drawCenteredLabel(Graphics2D g2, String text, int x, int y, int centerX, int centerY) {
        String label = text.length() > 18 ? text.substring(0, 17) + "." : text;
        FontMetrics metrics = g2.getFontMetrics();
        int labelX = x - metrics.stringWidth(label) / 2;
        int labelY = y;
        if (x < centerX) {
            labelX -= 14;
        } else if (x > centerX) {
            labelX += 14;
        }
        if (y < centerY) {
            labelY -= 10;
        } else {
            labelY += 20;
        }
        g2.setColor(new Color(54, 65, 82));
        g2.drawString(label, labelX, labelY);
    }
}
