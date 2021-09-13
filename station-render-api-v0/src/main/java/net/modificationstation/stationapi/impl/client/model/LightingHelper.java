package net.modificationstation.stationapi.impl.client.model;

import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;
import net.minecraft.sortme.GameRenderer;
import net.modificationstation.stationapi.api.block.Direction;
import net.modificationstation.stationapi.api.client.model.Vertex;

public class LightingHelper {

    // broken
    public static float[] getSmoothForQuadPoint(BlockBase block, BlockView blockView, int x, int y, int z, Vertex faceQuadPoint) {
        Direction face = faceQuadPoint.lightingFace;
        int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
        float colourMultiplierRed = (float)(colourMultiplier >> 16 & 255) / 255.0F;
        float colourMultiplierGreen = (float)(colourMultiplier >> 8 & 255) / 255.0F;
        float colourMultiplierBlue = (float)(colourMultiplier & 255) / 255.0F;
        if (GameRenderer.field_2340) {
            float colourMultiplierRedTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
            float colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F;
            float colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
            colourMultiplierRed = colourMultiplierRedTmp;
            colourMultiplierGreen = colourMultiplierGreenTmp;
            colourMultiplierBlue = colourMultiplierBlueTmp;
        }
        switch (face) {
            case DOWN:
                colourMultiplierRed *= 0.5;
                colourMultiplierGreen *= 0.5;
                colourMultiplierBlue *= 0.5;
                break;
            case UP:
                break;
            case EAST:
            case WEST:
                colourMultiplierRed *= 0.8;
                colourMultiplierGreen *= 0.8;
                colourMultiplierBlue *= 0.8;
                break;
            case NORTH:
            case SOUTH:
                colourMultiplierRed *= 0.6;
                colourMultiplierGreen *= 0.6;
                colourMultiplierBlue *= 0.6;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + face);
        }
        float brightnessSelf = block.getBrightness(blockView, x, y, z);
        y--;
        float
                brightnessBottomEast = block.getBrightness(blockView, x, y, z - 1),
                brightnessBottomWest = block.getBrightness(blockView, x, y, z + 1),
                brightnessBottomNorth = block.getBrightness(blockView, x - 1, y, z),
                brightnessBottomSouth = block.getBrightness(blockView, x + 1, y, z);
        boolean
                allowsGrassUnderBottomEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z - 1)],
                allowsGrassUnderBottomWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z + 1)],
                allowsGrassUnderBottomNorth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId( x - 1, y, z)],
                allowsGrassUnderBottomSouth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId( x + 1, y, z)];
        y++;
        float
                brightnessNorthEast = block.getBrightness(blockView, x - 1, y, z - 1),
                brightnessNorthWest = block.getBrightness(blockView, x - 1, y, z + 1),
                brightnessSouthEast = block.getBrightness(blockView, x + 1, y, z - 1),
                brightnessSouthWest = block.getBrightness(blockView, x + 1, y, z + 1);
        boolean
                allowsGrassUnderNorthEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x - 1, y, z - 1)],
                allowsGrassUnderNorthWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x - 1, y, z + 1)],
                allowsGrassUnderSouthEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x + 1, y, z - 1)],
                allowsGrassUnderSouthWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x + 1, y, z + 1)];
        y++;
        float
                brightnessTopEast = block.getBrightness(blockView, x, y, z - 1),
                brightnessTopWest = block.getBrightness(blockView, x, y, z + 1),
                brightnessTopNorth = block.getBrightness(blockView, x - 1, y, z),
                brightnessTopSouth = block.getBrightness(blockView, x + 1, y, z);
        boolean
                allowsGrassUnderTopEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z - 1)],
                allowsGrassUnderTopWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z + 1)],
                allowsGrassUnderTopNorth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x - 1, y, z)],
                allowsGrassUnderTopSouth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x + 1, y, z)];
        y--;
        float
                brightnessBottomNorthEast,
                brightnessBottomSouthEast,
                brightnessBottomNorthWest,
                brightnessBottomSouthWest,
                brightnessTopNorthEast,
                brightnessTopSouthEast,
                brightnessTopNorthWest,
                brightnessTopSouthWest;
        if (!allowsGrassUnderBottomEast && !allowsGrassUnderBottomNorth)
            brightnessBottomNorthEast = brightnessBottomNorth;
        else
            brightnessBottomNorthEast = block.getBrightness(blockView, x - 1, y - 1, z - 1);
        if (!allowsGrassUnderBottomEast && !allowsGrassUnderBottomSouth)
            brightnessBottomSouthEast = brightnessBottomSouth;
        else
            brightnessBottomSouthEast = block.getBrightness(blockView, x + 1, y - 1, z - 1);
        if (!allowsGrassUnderBottomWest && !allowsGrassUnderBottomNorth)
            brightnessBottomNorthWest = brightnessBottomNorth;
        else
            brightnessBottomNorthWest = block.getBrightness(blockView, x - 1, y - 1, z + 1);
        if (!allowsGrassUnderBottomWest && !allowsGrassUnderBottomSouth)
            brightnessBottomSouthWest = brightnessBottomSouth;
        else
            brightnessBottomSouthWest = block.getBrightness(blockView, x + 1, y - 1, z + 1);
        if (!allowsGrassUnderTopEast && !allowsGrassUnderTopNorth)
            brightnessTopNorthEast = brightnessTopNorth;
        else
            brightnessTopNorthEast = block.getBrightness(blockView, x - 1, y + 1, z - 1);
        if (!allowsGrassUnderTopEast && !allowsGrassUnderTopSouth)
            brightnessTopSouthEast = brightnessTopSouth;
        else
            brightnessTopSouthEast = block.getBrightness(blockView, x + 1, y + 1, z - 1);
        if (!allowsGrassUnderTopWest && !allowsGrassUnderTopNorth)
            brightnessTopNorthWest = brightnessTopNorth;
        else
            brightnessTopNorthWest = block.getBrightness(blockView, x - 1, y + 1, z + 1);
        if (!allowsGrassUnderTopWest && !allowsGrassUnderTopSouth)
            brightnessTopSouthWest = brightnessTopSouth;
        else
            brightnessTopSouthWest = block.getBrightness(blockView, x + 1, y + 1, z + 1);
        float brightnessX =
                (float)(brightnessBottomNorthEast + (brightnessBottomSouthEast - brightnessBottomNorthEast) * faceQuadPoint.x +
                        brightnessBottomNorthWest + (brightnessBottomSouthWest - brightnessBottomNorthWest) * faceQuadPoint.x +
                        brightnessTopNorthEast + (brightnessTopSouthEast - brightnessTopNorthEast) * faceQuadPoint.x +
                        brightnessTopNorthWest + (brightnessTopSouthWest - brightnessTopNorthWest) * faceQuadPoint.x
                ) / 4;
        float brightnessY =
                (float)(brightnessBottomNorthEast + (brightnessTopNorthEast - brightnessBottomNorthEast) * faceQuadPoint.y +
                        brightnessBottomNorthWest + (brightnessTopNorthWest - brightnessBottomNorthWest) * faceQuadPoint.y +
                        brightnessBottomSouthEast + (brightnessTopSouthEast - brightnessBottomSouthEast) * faceQuadPoint.y +
                        brightnessBottomSouthWest + (brightnessTopSouthWest - brightnessBottomSouthWest) * faceQuadPoint.y
                ) / 4;
        float brightnessZ =
                (float)(brightnessBottomNorthEast + (brightnessBottomNorthWest - brightnessBottomNorthEast) * faceQuadPoint.z +
                        brightnessBottomSouthEast + (brightnessBottomSouthWest - brightnessBottomSouthEast) * faceQuadPoint.z +
                        brightnessTopNorthEast + (brightnessTopNorthWest - brightnessTopNorthEast) * faceQuadPoint.z +
                        brightnessTopSouthEast + (brightnessTopSouthWest - brightnessTopSouthEast) * faceQuadPoint.z
                ) / 4;
        float brightnessMultiplier = (brightnessX + brightnessY + brightnessZ + brightnessSelf) / 4;
        return new float[] {
                colourMultiplierRed * brightnessMultiplier,
                colourMultiplierGreen * brightnessMultiplier,
                colourMultiplierBlue * brightnessMultiplier
        };
    }

    public static int getSmoothForQuadPoint(BlockBase block, BlockView blockView, int x, int y, int z, Vertex faceQuadPoint, int quadPointOrder) {
        Direction face = faceQuadPoint.lightingFace;
        int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
        float colourMultiplierRed = (float)(colourMultiplier >> 16 & 255) / 255.0F;
        float colourMultiplierGreen = (float)(colourMultiplier >> 8 & 255) / 255.0F;
        float colourMultiplierBlue = (float)(colourMultiplier & 255) / 255.0F;
        if (GameRenderer.field_2340) {
            float colourMultiplierRedTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
            float colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F;
            float colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
            colourMultiplierRed = colourMultiplierRedTmp;
            colourMultiplierGreen = colourMultiplierGreenTmp;
            colourMultiplierBlue = colourMultiplierBlueTmp;
        }
        switch (face) {
            case DOWN:
                colourMultiplierRed *= 0.5;
                colourMultiplierGreen *= 0.5;
                colourMultiplierBlue *= 0.5;
                --y;
                break;
            case UP:
                ++y;
                break;
            case EAST:
                colourMultiplierRed *= 0.8;
                colourMultiplierGreen *= 0.8;
                colourMultiplierBlue *= 0.8;
                --z;
                break;
            case WEST:
                colourMultiplierRed *= 0.8;
                colourMultiplierGreen *= 0.8;
                colourMultiplierBlue *= 0.8;
                ++z;
                break;
            case NORTH:
                colourMultiplierRed *= 0.6;
                colourMultiplierGreen *= 0.6;
                colourMultiplierBlue *= 0.6;
                --x;
                break;
            case SOUTH:
                colourMultiplierRed *= 0.6;
                colourMultiplierGreen *= 0.6;
                colourMultiplierBlue *= 0.6;
                ++x;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + face);
        }
        boolean
                allowsGrassUnderSouth,
                allowsGrassUnderNorth,
                allowsGrassUnderWest,
                allowsGrassUnderEast;
        float
                brightnessMultiplier,
                brightnessMiddle = block.getBrightness(blockView, x, y, z),
                brightnessNorth,
                brightnessSouth,
                brightnessEast,
                brightnessWest,
                brightnessNorthEast,
                brightnessSouthEast,
                brightnessNorthWest,
                brightnessSouthWest;
        switch (face) {
            case DOWN:
            case UP:
                allowsGrassUnderSouth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x + 1, y, z)];
                allowsGrassUnderNorth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x - 1, y, z)];
                allowsGrassUnderWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z + 1)];
                allowsGrassUnderEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z - 1)];
                brightnessNorth = block.getBrightness(blockView, x - 1, y, z);
                brightnessSouth = block.getBrightness(blockView, x + 1, y, z);
                brightnessEast = block.getBrightness(blockView, x, y, z - 1);
                brightnessWest = block.getBrightness(blockView, x, y, z + 1);
                brightnessNorthEast = !allowsGrassUnderEast && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x - 1, y, z - 1);
                brightnessSouthEast = !allowsGrassUnderEast && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x + 1, y, z - 1);
                brightnessNorthWest = !allowsGrassUnderWest && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x - 1, y, z + 1);
                brightnessSouthWest = !allowsGrassUnderWest && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x + 1, y, z + 1);
                break;
            case EAST:
            case WEST:
                allowsGrassUnderSouth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x + 1, y, z)];
                allowsGrassUnderNorth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x - 1, y, z)];
                allowsGrassUnderWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y - 1, z)];
                allowsGrassUnderEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y + 1, z)];
                brightnessNorth = block.getBrightness(blockView, x - 1, y, z);
                brightnessSouth = block.getBrightness(blockView, x + 1, y, z);
                brightnessEast = block.getBrightness(blockView, x, y + 1, z);
                brightnessWest = block.getBrightness(blockView, x, y - 1, z);
                brightnessNorthEast = !allowsGrassUnderEast && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x - 1, y + 1, z);
                brightnessSouthEast = !allowsGrassUnderEast && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x + 1, y + 1, z);
                brightnessNorthWest = !allowsGrassUnderWest && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x - 1, y - 1, z);
                brightnessSouthWest = !allowsGrassUnderWest && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x + 1, y - 1, z);
                break;
            case NORTH:
            case SOUTH:
                allowsGrassUnderSouth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y + 1, z)];
                allowsGrassUnderNorth = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y - 1, z)];
                allowsGrassUnderWest = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z - 1)];
                allowsGrassUnderEast = BlockBase.ALLOWS_GRASS_UNDER[blockView.getTileId(x, y, z + 1)];
                brightnessNorth = block.getBrightness(blockView, x, y - 1, z);
                brightnessSouth = block.getBrightness(blockView, x, y + 1, z);
                brightnessEast = block.getBrightness(blockView, x, y, z + 1);
                brightnessWest = block.getBrightness(blockView, x, y, z - 1);
                brightnessNorthEast = !allowsGrassUnderEast && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x, y - 1, z + 1);
                brightnessSouthEast = !allowsGrassUnderEast && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x, y + 1, z + 1);
                brightnessNorthWest = !allowsGrassUnderWest && !allowsGrassUnderNorth ? brightnessNorth : block.getBrightness(blockView, x, y - 1, z - 1);
                brightnessSouthWest = !allowsGrassUnderWest && !allowsGrassUnderSouth ? brightnessSouth : block.getBrightness(blockView, x, y + 1, z - 1);
                break;
            default:
                throw new IllegalStateException("Unexpected block face: " + face);
        }
        switch (face) {
            case DOWN:
                switch (quadPointOrder) {
                    case 0:
                        brightnessMultiplier = (brightnessNorthWest + brightnessNorth + brightnessWest + brightnessMiddle) / 4;
                        break;
                    case 1:
                        brightnessMultiplier = (brightnessNorth + brightnessNorthEast + brightnessMiddle + brightnessEast) / 4;
                        break;
                    case 2:
                        brightnessMultiplier = (brightnessMiddle + brightnessEast + brightnessSouth + brightnessSouthEast) / 4;
                        break;
                    case 3:
                        brightnessMultiplier = (brightnessWest + brightnessMiddle + brightnessSouthWest + brightnessSouth) / 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected quad point order: " + quadPointOrder);
                }
                break;
            case UP:
                switch (quadPointOrder) {
                    case 0:
                        brightnessMultiplier = (brightnessWest + brightnessMiddle + brightnessSouthWest + brightnessSouth) / 4;
                        break;
                    case 1:
                        brightnessMultiplier = (brightnessMiddle + brightnessEast + brightnessSouth + brightnessSouthEast) / 4;
                        break;
                    case 2:
                        brightnessMultiplier = (brightnessNorth + brightnessNorthEast + brightnessMiddle + brightnessEast) / 4;
                        break;
                    case 3:
                        brightnessMultiplier = (brightnessNorthWest + brightnessNorth + brightnessWest + brightnessMiddle) / 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected quad point order: " + quadPointOrder);
                }
                break;
            case EAST:
                switch (quadPointOrder) {
                    case 0:
                        brightnessMultiplier = (brightnessNorth + brightnessNorthEast + brightnessMiddle + brightnessEast) / 4;
                        break;
                    case 1:
                        brightnessMultiplier = (brightnessMiddle + brightnessEast + brightnessSouth + brightnessSouthEast) / 4;
                        break;
                    case 2:
                        brightnessMultiplier = (brightnessWest + brightnessMiddle + brightnessSouthWest + brightnessSouth) / 4;
                        break;
                    case 3:
                        brightnessMultiplier = (brightnessNorthWest + brightnessNorth + brightnessWest + brightnessMiddle) / 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected quad point order: " + quadPointOrder);
                }
                break;
            case WEST:
            case SOUTH:
                switch (quadPointOrder) {
                    case 0:
                        brightnessMultiplier = (brightnessNorth + brightnessNorthEast + brightnessMiddle + brightnessEast) / 4;
                        break;
                    case 1:
                        brightnessMultiplier = (brightnessNorthWest + brightnessNorth + brightnessWest + brightnessMiddle) / 4;
                        break;
                    case 2:
                        brightnessMultiplier = (brightnessWest + brightnessMiddle + brightnessSouthWest + brightnessSouth) / 4;
                        break;
                    case 3:
                        brightnessMultiplier = (brightnessMiddle + brightnessEast + brightnessSouth + brightnessSouthEast) / 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected quad point order: " + quadPointOrder);
                }
                break;
            case NORTH:
                switch (quadPointOrder) {
                    case 0:
                        brightnessMultiplier = (brightnessMiddle + brightnessEast + brightnessSouth + brightnessSouthEast) / 4;
                        break;
                    case 1:
                        brightnessMultiplier = (brightnessWest + brightnessMiddle + brightnessSouthWest + brightnessSouth) / 4;
                        break;
                    case 2:
                        brightnessMultiplier = (brightnessNorthWest + brightnessNorth + brightnessWest + brightnessMiddle) / 4;
                        break;
                    case 3:
                        brightnessMultiplier = (brightnessNorth + brightnessNorthEast + brightnessMiddle + brightnessEast) / 4;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected quad point order: " + quadPointOrder);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected block face: " + face);
        }
        return (((int) (colourMultiplierRed * brightnessMultiplier * 255) & 255) << 16) +
                (((int) (colourMultiplierGreen * brightnessMultiplier * 255) & 255) << 8) +
                ((int) (colourMultiplierBlue * brightnessMultiplier * 255) & 255);
    }
}
