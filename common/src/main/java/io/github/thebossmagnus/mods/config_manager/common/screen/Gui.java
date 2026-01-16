package io.github.thebossmagnus.mods.config_manager.common.screen;


import io.github.thebossmagnus.mods.config_manager.common.AddFlagsUtil;
import io.github.thebossmagnus.mods.config_manager.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class Gui extends Screen {
    private static final int buttonWidth = 150;
    private static final int buttonHeight = 20;
    private static final int COLOR_RED = 0xFF0000;
    private static final int COLOR_WHITE = 0xFFFFFF;
    private final Screen parent;
    private final Component updateWarnings = Component.translatable("* %s\n* %s",
            Component.translatable("config_manager.warning.lose_some_config"),
            Component.translatable("config_manager.warning.game_restart")
    );
    private final Component resetWarnings = Component.translatable("* %s\n* %s",
            Component.translatable("config_manager.warning.lose_all_config"),
            Component.translatable("config_manager.warning.game_restart")
    );

    private MultilineLabelWidget updateWarningsLabel;
    private MultilineLabelWidget resetWarningsLabel;

    @SuppressWarnings("unused")
    public Gui(Screen screen) {
        super(Component.translatable("config_manager.title"));
        parent = screen;
    }

    private Button createConfigButton(Component label, Runnable flagSetter) {
        // Use a local mutable state for each button
        final boolean[] isFirstClick = {true};
        return Button.builder(label, (btn) -> {
                    if (isFirstClick[0]) {
                        btn.setMessage(Component.translatable("config_manager.confirmation").withStyle(style -> style.withColor(COLOR_RED)));
                        isFirstClick[0] = false;
                    } else {
                        try {
                            btn.active = false;
                            flagSetter.run();
                            btn.setMessage(Component.translatable("config_manager.success").withStyle(style -> style.withColor(COLOR_WHITE)));
                            Constants.LOGGER.info("Flag Added");
                        } catch (Exception e) {
                            Constants.LOGGER.error("Failed to add flag", e);
                            btn.setMessage(Component.translatable("config_manager.error").withStyle(style -> style.withColor(COLOR_RED)));
                            btn.active = false;
                        }
                    }
                })
                .size(buttonWidth, buttonHeight)
                .build();
    }

    @Override
    protected void init() {
        Button updateButton = createConfigButton(
                Component.translatable("config_manager.update_config"),
                () -> AddFlagsUtil.setUpdateFlag(true)
        );
        updateButton.setPosition((int) ((this.width - buttonWidth) * 0.15), (int) ((this.height - buttonHeight) * 0.6));

        Button resetButton = createConfigButton(
                Component.translatable("config_manager.reset_config"),
                () -> AddFlagsUtil.setOverwriteFlag(true)
        );
        resetButton.setPosition((int) ((this.width - buttonWidth) * 0.85), (int) ((this.height - buttonHeight) * 0.6));

        Button closeButton = Button.builder(Component.translatable("config_manager.close"), (btn) -> {
            this.onClose();
        }).pos((int) ((this.width - buttonWidth) * 0.5), (int) ((this.height - buttonHeight) * 0.95)).size(buttonWidth, buttonHeight).build();

        this.addRenderableWidget(resetButton);
        this.addRenderableWidget(updateButton);
        this.addRenderableWidget(closeButton);

        updateWarningsLabel = new MultilineLabelWidget(
                this.font,
                updateWarnings,
                (int) ((this.width - buttonWidth) * 0.15),
                (int) ((this.height - buttonHeight) * 0.6) - 90,
                buttonWidth,
                true
        );

        resetWarningsLabel = new MultilineLabelWidget(
                this.font,
                resetWarnings,
                (int) ((this.width - buttonWidth) * 0.85),
                (int) ((this.height - buttonHeight) * 0.6) - 90,
                buttonWidth,
                true
        );
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        updateWarningsLabel.render(guiGraphics, mouseX, mouseY, partialTick);
        resetWarningsLabel.render(guiGraphics, mouseX, mouseY, partialTick);
    }


    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

}
