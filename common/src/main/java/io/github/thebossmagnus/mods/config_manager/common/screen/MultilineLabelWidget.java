package io.github.thebossmagnus.mods.config_manager.common.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget for rendering multi-line text with automatic word wrapping and formatting in Minecraft GUIs.
 */
public class MultilineLabelWidget implements Renderable {
    private final Font font;
    private final Component text;
    private final int x;
    private final int y;
    private final int width;
    private final int maxHeight;
    private final boolean centered;

    /**
     * Constructs a MultilineLabelWidget
     *
     * @param font      Font renderer used for drawing text
     * @param text      The text component to display (can contain formatting and siblings)
     * @param x         X position of the widget
     * @param y         Y position of the widget
     * @param width     Width of the widget (used for wrapping and centering)
     * @param maxHeight Maximum height available (text will scale to fit)
     * @param centered  Whether to center the text horizontally
     */
    public MultilineLabelWidget(Font font, Component text, int x, int y, int width, int maxHeight, boolean centered) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.maxHeight = maxHeight;
        this.centered = centered;
    }

    /**
     * Renders the multi-line label with automatic word wrapping
     *
     * @param guiGraphics The graphics context
     * @param mouseX      Mouse X position
     * @param mouseY      Mouse Y position
     * @param partialTick Partial tick
     */
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Collect the main text and its siblings (extra components attached to the main one)
        List<Component> allComponents = new ArrayList<>();
        allComponents.add(text);
        allComponents.addAll(text.getSiblings());

        List<FormattedCharSequence> outputLines = new ArrayList<>();

        // For each component, use font.split to wrap text to the specified width
        for (Component comp : allComponents) {
            // font.split returns a list of FormattedCharSequence, each fitting within the width
            List<FormattedCharSequence> wrapped = font.split(comp, width);
            outputLines.addAll(wrapped);
        }

        int lineHeight = computeLineHeight(outputLines);

        // Calculate the actual total height used by the label
        int totalHeight = outputLines.size() * lineHeight;

        // Start text in a height that makes it end right and the start of the button + padding
        int currentY = y + maxHeight - totalHeight;

        for (FormattedCharSequence line : outputLines) {
            int drawX = centered ? x + width / 2 : x;
            if (centered) {
                guiGraphics.drawCenteredString(font, line, drawX, currentY, 0xFFFFFFFF);
            } else {
                guiGraphics.drawString(font, line, drawX, currentY, 0xFFFFFFFF);
            }
            currentY += lineHeight;
        }
    }

    /**
     * computes a good line height based on maxHeight
     *
     * @param lines A list with all lines to be rendered
     */

    private int computeLineHeight(List<FormattedCharSequence> lines) {
        int lineHeight = font.lineHeight + 1; // Default line height with a small padding
        float scale = Math.min(1.0f, (float) maxHeight / (lines.size() * lineHeight)); //if there's enough space, use normal line height else scale down
        return Math.round(lineHeight * scale);
    }
}
