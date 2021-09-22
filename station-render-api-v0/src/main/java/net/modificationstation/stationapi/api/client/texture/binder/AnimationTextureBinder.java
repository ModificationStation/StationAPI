package net.modificationstation.stationapi.api.client.texture.binder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.TextureAnimationData;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import java.awt.image.*;

public class AnimationTextureBinder extends StationTextureBinder {

    private final TextureAnimationData animationData;
    private final byte[][] frames;
    private final byte[][][] interpolatedFrames;
    @SuppressWarnings("deprecation")
    private final TexturePack currentTexturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack;
    private final boolean customFrameData;
    private TextureAnimationData.Frame currentFrame;
    private int currentFrameIndex;
    private int timer;

    public AnimationTextureBinder(BufferedImage image, Atlas.Sprite staticReference, TextureAnimationData animationData) {
        super(staticReference);
        this.animationData = animationData;
        int
                targetWidth = staticReference.getWidth(),
                targetHeight = staticReference.getHeight(),
                images = image.getHeight() / targetHeight;
        frames = new byte[images][];
        for (int i = 0; i < images; i++) {
            int[] temp = new int[targetWidth * targetHeight];
            image.getRGB(0, targetHeight * i, targetWidth, targetHeight, temp, 0, targetWidth);
            frames[i] = new byte[targetWidth * targetHeight * 4];
            for (int j = 0; j < temp.length; j++) {
                int
                        a = temp[j] >> 24 & 0xff,
                        r = temp[j] >> 16 & 0xff,
                        g = temp[j] >> 8 & 0xff,
                        b = temp[j] & 0xff;
                frames[i][j * 4] = (byte) r;
                frames[i][j * 4 + 1] = (byte) g;
                frames[i][j * 4 + 2] = (byte) b;
                frames[i][j * 4 + 3] = (byte) a;
            }
        }
        customFrameData = animationData.frames.size() > 0;
        if (customFrameData) {
            currentFrame = animationData.frames.get(currentFrameIndex);
            grid = frames[currentFrame.index];
            if (animationData.interpolate) {
                interpolatedFrames = new byte[animationData.frames.size()][][];
                for (int frameIndex = 0, framesSize = animationData.frames.size(); frameIndex < framesSize; frameIndex++) {
                    TextureAnimationData.Frame
                            frame = animationData.frames.get(frameIndex),
                            nextFrame = animationData.frames.get(frameIndex == framesSize - 1 ? 0 : frameIndex + 1);
                    byte[]
                            frameGrid = frames[frame.index],
                            nextFrameGrid = frames[nextFrame.index];
                    byte[][] interpolations = new byte[frame.time - 1][];
                    for (int interpolatedFrame = 0; interpolatedFrame < frame.time - 1; interpolatedFrame++) {
                        byte[] interpolatedFrameGrid = new byte[frameGrid.length];
                        for (int i = 0; i < interpolatedFrameGrid.length; i++)
                            interpolatedFrameGrid[i] = (byte) MathHelper.lerp((double) (interpolatedFrame + 1) / frame.time, Byte.toUnsignedInt(frameGrid[i]), Byte.toUnsignedInt(nextFrameGrid[i]));
                        interpolations[interpolatedFrame] = interpolatedFrameGrid;
                    }
                    interpolatedFrames[frameIndex] = interpolations;
                }
            } else
                interpolatedFrames = null;
        } else {
            grid = frames[currentFrameIndex];
            if (animationData.interpolate) {
                interpolatedFrames = new byte[frames.length][][];
                for (int frameIndex = 0; frameIndex < frames.length; frameIndex++) {
                    byte[]
                            frameGrid = frames[frameIndex],
                            nextFrameGrid = frames[frameIndex == frames.length - 1 ? 0 : frameIndex + 1];
                    byte[][] interpolations = new byte[animationData.frametime - 1][];
                    for (int interpolatedFrame = 0; interpolatedFrame < animationData.frametime - 1; interpolatedFrame++) {
                        byte[] interpolatedFrameGrid = new byte[frameGrid.length];
                        for (int i = 0; i < interpolatedFrameGrid.length; i++)
                            interpolatedFrameGrid[i] = (byte) MathHelper.lerp((double) (interpolatedFrame + 1) / animationData.frametime, Byte.toUnsignedInt(frameGrid[i]), Byte.toUnsignedInt(nextFrameGrid[i]));
                        interpolations[interpolatedFrame] = interpolatedFrameGrid;
                    }
                    interpolatedFrames[frameIndex] = interpolations;
                }
            } else
                interpolatedFrames = null;
        }
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        if (!currentTexturePack.equals(newTexturePack))
            //noinspection deprecation
            ((TextureManagerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getTextureBinders().remove(this);
    }

    @Override
    public void update() {
        if (customFrameData) {
            if (++timer >= currentFrame.time) {
                timer = 0;
                if (++currentFrameIndex >= animationData.frames.size())
                    currentFrameIndex = 0;
                currentFrame = animationData.frames.get(currentFrameIndex);
                grid = frames[currentFrame.index];
            } else if (animationData.interpolate)
                grid = interpolatedFrames[currentFrameIndex][timer - 1];
        } else {
            if (++timer >= animationData.frametime) {
                timer = 0;
                if (++currentFrameIndex >= frames.length)
                    currentFrameIndex = 0;
                grid = frames[currentFrameIndex];
            } else if (animationData.interpolate)
                grid = interpolatedFrames[currentFrameIndex][timer - 1];
        }
    }
}
