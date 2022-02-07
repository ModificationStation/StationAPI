package net.modificationstation.stationapi.api.client.texture.binder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;

import java.awt.image.*;

@Deprecated
public class AnimationTextureBinder extends StationTextureBinder {

    private final AnimationResourceMetadata animationData;
    private final byte[][] frames;
    private final byte[][][] interpolatedFrames;
    private final TexturePack currentTexturePack = ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack;
    private int currentFrameIndex;
    private int timer;
    private final boolean customFrames;

    public AnimationTextureBinder(BufferedImage image, Atlas.Sprite staticReference, AnimationResourceMetadata animationData) {
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
        customFrames = animationData.getFrameCount() > 0;
        if (customFrames) {
            grid = frames[animationData.getFrameIndex(currentFrameIndex)];
            if (animationData.shouldInterpolate()) {
                interpolatedFrames = new byte[animationData.getFrameCount()][][];
                for (int frameIndex = 0, framesSize = animationData.getFrameCount(); frameIndex < framesSize; frameIndex++) {
                    int frame = animationData.getFrameIndex(frameIndex);
                    int nextFrame = animationData.getFrameIndex(frameIndex == framesSize - 1 ? 0 : frameIndex + 1);
                    byte[]
                            frameGrid = frames[frame],
                            nextFrameGrid = frames[nextFrame];
                    byte[][] interpolations = new byte[animationData.getFrameTime(frame) - 1][];
                    for (int interpolatedFrame = 0; interpolatedFrame < animationData.getFrameTime(frame) - 1; interpolatedFrame++) {
                        byte[] interpolatedFrameGrid = new byte[frameGrid.length];
                        for (int i = 0; i < interpolatedFrameGrid.length; i++) {
                            if (i % 4 == 3) {
                                if (nextFrameGrid[i] == 0 && frameGrid[i] != 0) {
                                    interpolatedFrameGrid[i - 3] = frameGrid[i - 3];
                                    interpolatedFrameGrid[i - 2] = frameGrid[i - 2];
                                    interpolatedFrameGrid[i - 1] = frameGrid[i - 1];
                                } else if (nextFrameGrid[i] != 0 && frameGrid[i] == 0) {
                                    interpolatedFrameGrid[i - 3] = nextFrameGrid[i - 3];
                                    interpolatedFrameGrid[i - 2] = nextFrameGrid[i - 2];
                                    interpolatedFrameGrid[i - 1] = nextFrameGrid[i - 1];
                                }
                            }
                            interpolatedFrameGrid[i] = (byte) MathHelper.lerp((double) (interpolatedFrame + 1) / animationData.getFrameTime(frame), Byte.toUnsignedInt(frameGrid[i]), Byte.toUnsignedInt(nextFrameGrid[i]));
                        }
                        interpolations[interpolatedFrame] = interpolatedFrameGrid;
                    }
                    interpolatedFrames[frameIndex] = interpolations;
                }
            } else
                interpolatedFrames = null;
        } else {
            grid = frames[currentFrameIndex];
            if (animationData.shouldInterpolate()) {
                interpolatedFrames = new byte[frames.length][][];
                for (int frameIndex = 0, framesSize = frames.length; frameIndex < framesSize; frameIndex++) {
                    int nextFrame = frameIndex == framesSize - 1 ? 0 : frameIndex + 1;
                    byte[]
                            frameGrid = frames[frameIndex],
                            nextFrameGrid = frames[nextFrame];
                    byte[][] interpolations = new byte[animationData.getDefaultFrameTime() - 1][];
                    for (int interpolatedFrame = 0; interpolatedFrame < animationData.getDefaultFrameTime() - 1; interpolatedFrame++) {
                        byte[] interpolatedFrameGrid = new byte[frameGrid.length];
                        for (int i = 0; i < interpolatedFrameGrid.length; i++)
                            interpolatedFrameGrid[i] = (byte) MathHelper.lerp((double) (interpolatedFrame + 1) / animationData.getDefaultFrameTime(), Byte.toUnsignedInt(frameGrid[i]), Byte.toUnsignedInt(nextFrameGrid[i]));
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
            ((TextureManagerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getTextureBinders().remove(this);
    }

    @Override
    public void update() {
        if (customFrames) {
            if (++timer >= animationData.getFrameTime(currentFrameIndex)) {
                timer = 0;
                if (++currentFrameIndex >= animationData.getFrameCount())
                    currentFrameIndex = 0;
                grid = frames[animationData.getFrameIndex(currentFrameIndex)];
            } else if (animationData.shouldInterpolate())
                grid = interpolatedFrames[currentFrameIndex][timer - 1];
        } else {
            if (++timer >= animationData.getDefaultFrameTime()) {
                timer = 0;
                if (++currentFrameIndex >= frames.length)
                    currentFrameIndex = 0;
                grid = frames[currentFrameIndex];
            } else if (animationData.shouldInterpolate())
                grid = interpolatedFrames[currentFrameIndex][timer - 1];
        }
    }
}
