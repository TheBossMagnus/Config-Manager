package io.github.thebossmagnus.mods.config_manager.common.screen;

import io.github.thebossmagnus.mods.config_manager.common.AddFlagsUtil;
import io.github.thebossmagnus.mods.config_manager.common.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class Gui extends Screen {
    private static final int MARGIN = 30;
    private static final int MAX_CONTENT_WIDTH = 620;
    private static final int CARD_GAP = 12;
    private static final int CARD_PADDING = 12;
    private static final int TEXT_GAP = 8;
    private static final int BUTTON_GAP = 12;
    private static final int BUTTON_HEIGHT = 20;
    private static final int CLOSE_BUTTON_WIDTH = 160;
    private static final int CARD_COLOR = 0xB0161B22;
    private static final int UPDATE_COLOR = 0x55D6A0;
    private static final int RESET_COLOR = 0xFF7777;
    private static final int CONFIRM_COLOR = 0xFF5555;

    private final Screen parent;

    @SuppressWarnings("unused")
    public Gui(Screen parent) {
        super(Component.translatable("config_manager.title"));
        this.parent = parent;
    }

    private static Component warning(String specificWarningKey) {
        return Component.translatable(
                "%s\n" +
                        "%s",
                Component.translatable(specificWarningKey),
                Component.translatable("config_manager.warning.game_restart")
        );
    }

    @Override
    protected void init() {
        Component updateLabel = Component.translatable("config_manager.update_config");
        Component resetLabel = Component.translatable("config_manager.reset_config");
        Component updateWarningText = warning("config_manager.warning.lose_some_config");
        Component resetWarningText = warning("config_manager.warning.lose_all_config");
        int contentWidth = Math.min(MAX_CONTENT_WIDTH, width - MARGIN); // Compute how much space we actually have with a decent margin
        int cardWidth = (contentWidth - CARD_GAP) / 2;
        int firstBoxStart = (width - contentWidth) / 2;
        int secondBoxStart = firstBoxStart + cardWidth + CARD_GAP;
        int textWidth = cardWidth - CARD_PADDING * 2; // how much space we have for the text inside
        int cardHeight = Math.max(
                Card.height(font, textWidth, updateLabel, updateWarningText),
                Card.height(font, textWidth, resetLabel, resetWarningText)
        ); // take the taller of the two cards to align them
        int buttonY = MARGIN + cardHeight - CARD_PADDING - BUTTON_HEIGHT; //At what height to place the two buttons

        addWidget(new Card(font, firstBoxStart, MARGIN, cardWidth, cardHeight, UPDATE_COLOR, updateLabel, updateWarningText));
        addWidget(new Card(font, secondBoxStart, MARGIN, cardWidth, cardHeight, RESET_COLOR, resetLabel, resetWarningText));
        addRenderableWidget(actionButton(updateLabel, () -> AddFlagsUtil.setUpdateFlag(true),
                firstBoxStart + CARD_PADDING, buttonY, textWidth));
        addRenderableWidget(actionButton(resetLabel, () -> AddFlagsUtil.setOverwriteFlag(true),
                secondBoxStart + CARD_PADDING, buttonY, textWidth));
        addRenderableWidget(Button.builder(Component.translatable("config_manager.close"), _ -> onClose())
                .bounds((width - CLOSE_BUTTON_WIDTH) / 2, height - MARGIN,
                        Math.min(CLOSE_BUTTON_WIDTH, width - MARGIN), BUTTON_HEIGHT)
                .build());
    }

    private Button actionButton(Component label, Runnable action, int x, int y, int buttonWidth) {
        boolean[] confirmed = {false};
        return Button.builder(label, button -> {
                    if (!confirmed[0]) {
                        button.setMessage(Component.translatable("config_manager.confirmation")
                                .withStyle(style -> style.withColor(CONFIRM_COLOR)));
                        confirmed[0] = true;
                        return;
                    }
                    try {
                        action.run();
                        Constants.LOGGER.info("Flag Added, it will be processed on the next game boot");
                        button.setMessage(Component.translatable("config_manager.success"));
                    } catch (Throwable e) {
                        Constants.LOGGER.error("Failed to add flag", e);
                        button.setMessage(Component.translatable("config_manager.error")
                                .withStyle(style -> style.withColor(CONFIRM_COLOR)));
                    }
                    button.active = false;
                })
                .bounds(x, y, buttonWidth, BUTTON_HEIGHT)
                .build();
    }

    @Override
    public void onClose() {
        this.minecraft.gui.setScreen(this.parent);
    }

    private static final class Card extends AbstractWidget {
        private final Font font;
        private final int accent;
        private final List<FormattedCharSequence> title;
        private final List<FormattedCharSequence> warning;

        private Card(Font font, int x, int y, int width, int height, int accent, Component title, Component warning) {
            super(x, y, width, height, Component.empty());
            this.font = font;
            this.accent = accent;
            int textWidth = width - CARD_PADDING * 2;
            this.title = font.split(title.copy().withStyle(style -> style.withColor(accent)), textWidth);
            this.warning = font.split(warning, textWidth);
        }

        private static int height(Font font, int textWidth, Component title, Component warning) {
            int lineCount = font.split(title, textWidth).size() + font.split(warning, textWidth).size();
            return CARD_PADDING * 2 + lineCount * font.lineHeight + TEXT_GAP + BUTTON_GAP + BUTTON_HEIGHT;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics) {
            graphics.fill(getX(), getY(), getX() + width, getY() + height, CARD_COLOR);
            graphics.fill(getX(), getY(), getX() + width, getY() + 2, accent);
            int x = getX() + CARD_PADDING;
            int y = getY() + CARD_PADDING;
            for (FormattedCharSequence line : title) {
                graphics.text(font, line, x, y, 0xFFFFFFFF);
                y += font.lineHeight;
            }
            y += TEXT_GAP;
            for (FormattedCharSequence line : warning) {
                graphics.text(font, line, x, y, 0xFFFFFFFF);
                y += font.lineHeight;
            }
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
            this.defaultButtonNarrationText(output);
        }

    }
}
