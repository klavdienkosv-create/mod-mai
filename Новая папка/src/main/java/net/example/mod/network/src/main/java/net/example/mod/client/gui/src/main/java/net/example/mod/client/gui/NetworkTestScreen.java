package net.example.mod.client.gui;

import net.example.mod.network.ExploitTestPackets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NetworkTestScreen extends Screen {
    private EditBoxWidget nbtScriptBox;
    private TextFieldWidget itemIdField;
    private TextFieldWidget amountField;
    private int activeMode = 0;

    public NetworkTestScreen() {
        super(Text.literal("Network Vulnerability Audit Tool"));
    }

    @Override
    protected void init() {
        this.nbtScriptBox = new EditBoxWidget(this.textRenderer, 20, 40, 180, 140, Text.literal("NBT / Components"), Text.empty());
        this.nbtScriptBox.setText("[minecraft:enchantments={levels:{\"minecraft:sharpness\":5}}]");
        this.addSelectableChild(this.nbtScriptBox);

        this.itemIdField = new TextFieldWidget(this.textRenderer, 220, 40, 120, 20, Text.literal("Item ID"));
        this.itemIdField.setText("minecraft:diamond_sword");
        this.addSelectableChild(this.itemIdField);

        this.amountField = new TextFieldWidget(this.textRenderer, 220, 80, 50, 20, Text.literal("Count"));
        this.amountField.setText("1");
        this.addSelectableChild(this.amountField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Mode: Custom Payload"), button -> {
            activeMode = (activeMode + 1) % 3;
            String modeName = switch (activeMode) {
                case 0 -> "Mode: Custom Payload";
                case 1 -> "Mode: Creative Action";
                case 2 -> "Mode: Command Block";
                default -> "Unknown";
            };
            button.setMessage(Text.literal(modeName));
        }).dimensions(220, 110, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("SEND PACKET"), button -> {
            executePacketSend();
        }).dimensions(220, 150, 150, 20).build());
    }

    private void executePacketSend() {
        String nbtData = this.nbtScriptBox.getText();
        String itemId = this.itemIdField.getText();
        int count = 1;
        try { count = Integer.parseInt(this.amountField.getText()); } catch (NumberFormatException ignored) {}

        switch (this.activeMode) {
            case 0 -> ExploitTestPackets.sendCustomPayloadTest(itemId, count, nbtData);
            case 1 -> ExploitTestPackets.sendCreativeInventoryPacket(itemId, count, nbtData);
            case 2 -> ExploitTestPackets.sendCommandBlockUpdatePacket(nbtData);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.nbtScriptBox.render(context, mouseX, mouseY, delta);
        this.itemIdField.render(context, mouseX, mouseY, delta);
        this.amountField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, "NBT Data Structure:", 20, 25, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Item ID:", 220, 25, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Count:", 220, 70, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
