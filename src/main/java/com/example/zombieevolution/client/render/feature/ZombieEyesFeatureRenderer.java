package com.example.zombieevolution.client.render.feature;

import com.example.zombieevolution.ZombieEvolution;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieEyesFeatureRenderer extends RenderLayer<Zombie, ZombieModel<Zombie>> {

    private static final RenderType EYES = RenderType.eyes(
            new ResourceLocation(ZombieEvolution.MOD_ID, "textures/entity/zombie/eyes.png"));

    public ZombieEyesFeatureRenderer(LivingEntityRenderer<Zombie, ZombieModel<Zombie>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Zombie entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer consumer = vertexConsumers.getBuffer(EYES);
        this.getParentModel().renderToBuffer(matrices, consumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}
