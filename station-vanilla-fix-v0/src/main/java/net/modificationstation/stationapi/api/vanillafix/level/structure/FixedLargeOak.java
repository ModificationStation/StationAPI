package net.modificationstation.stationapi.api.vanillafix.level.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.LargeOak;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;

import java.util.Random;

public class FixedLargeOak extends LargeOak {

    static final byte[] field_645 = new byte[]{2, 0, 0, 1, 2, 1};
    Random field_646 = new Random();
    Level field_647;
    int[] field_648 = new int[]{0, 0, 0};
    int field_649 = 0;
    int field_650;
    double field_651 = 0.618;
    double field_653 = 0.381;
    double field_654 = 1.0;
    double field_655 = 1.0;
    int field_656 = 1;
    int field_657 = 12;
    int field_658 = 4;
    int[][] field_659;

    void method_612() {
        int n;
        this.field_650 = (int)((double)this.field_649 * this.field_651);
        if (this.field_650 >= this.field_649) this.field_650 = this.field_649 - 1;
        if ((n = (int)(1.382 + Math.pow(this.field_655 * (double)this.field_649 / 13.0, 2.0))) < 1) n = 1;
        int[][] nArray = new int[n * this.field_649][4];
        int n2 = this.field_648[1] + this.field_649 - this.field_658;
        int n3 = 1;
        int n4 = this.field_648[1] + this.field_650;
        int n5 = n2 - this.field_648[1];
        nArray[0][0] = this.field_648[0];
        nArray[0][1] = n2--;
        nArray[0][2] = this.field_648[2];
        nArray[0][3] = n4;
        while (n5 >= 0) {
            float f = this.method_613(n5);
            if (f < 0.0f) {
                --n2;
                --n5;
                continue;
            }
            double d = 0.5;
            for (int i = 0; i < n; ++i) {
                int n6;
                double d2;
                double d3 = this.field_654 * ((double)f * ((double)this.field_646.nextFloat() + 0.328));
                int n7 = MathHelper.floor(d3 * Math.sin(d2 = (double)this.field_646.nextFloat() * 2.0 * 3.14159) + (double)this.field_648[0] + d);
                int[] nArray3 = new int[]{n7, n2, n6 = MathHelper.floor(d3 * Math.cos(d2) + (double)this.field_648[2] + d)};
                if (this.method_616(nArray3, new int[]{n7, n2 + this.field_658, n6}) != -1) continue;
                int[] nArray4 = new int[]{this.field_648[0], this.field_648[1], this.field_648[2]};
                double d4 = Math.sqrt(Math.pow(Math.abs(this.field_648[0] - nArray3[0]), 2.0) + Math.pow(Math.abs(this.field_648[2] - nArray3[2]), 2.0));
                double d5 = d4 * this.field_653;
                nArray4[1] = (double)nArray3[1] - d5 > (double)n4 ? n4 : (int)((double)nArray3[1] - d5);
                if (this.method_616(nArray4, nArray3) != -1) continue;
                nArray[n3][0] = n7;
                nArray[n3][1] = n2;
                nArray[n3][2] = n6;
                nArray[n3][3] = nArray4[1];
                ++n3;
            }
            --n2;
            --n5;
        }
        this.field_659 = new int[n3][4];
        System.arraycopy(nArray, 0, this.field_659, 0, n3);
    }

    void method_615(int i, int j, int k, float f, byte b, int l) {
        int n = (int)((double)f + 0.618);
        byte by = field_645[b];
        byte by2 = field_645[b + 3];
        int[] nArray = new int[]{i, j, k};
        int[] nArray2 = new int[]{0, 0, 0};
        nArray2[b] = nArray[b];
        for (int i2 = -n; i2 <= n; ++i2) {
            nArray2[by] = nArray[by] + i2;
            int n2 = -n;
            while (n2 <= n) {
                double d = Math.sqrt(Math.pow((double)Math.abs(i2) + 0.5, 2.0) + Math.pow((double)Math.abs(n2) + 0.5, 2.0));
                if (d > (double)f) {
                    ++n2;
                    continue;
                }
                nArray2[by2] = nArray[by2] + n2;
                int n3 = this.field_647.getTileId(nArray2[0], nArray2[1], nArray2[2]);
                if (n3 != 0 && !(BlockBase.BY_ID[n3] instanceof FixedLeaves)) {
                    ++n2;
                    continue;
                }
                this.field_647.setTileInChunk(nArray2[0], nArray2[1], nArray2[2], l);
                ++n2;
            }
        }
    }

    float method_613(int i) {
        if ((double)i < (double)this.field_649 * 0.3) return -1.618f;
        float f = (float)this.field_649 / 2.0f;
        float f2 = (float)this.field_649 / 2.0f - (float)i;
        float f3 = f2 == 0.0f ? f : (Math.abs(f2) >= f ? 0.0f : (float)Math.sqrt(Math.pow(Math.abs(f), 2.0) - Math.pow(Math.abs(f2), 2.0)));
        return f3 * 0.5f;
    }

    float method_619(int i) {
        if (i < 0 || i >= this.field_658) return -1.0f;
        if (i == 0 || i == this.field_658 - 1) return 2.0f;
        return 3.0f;
    }

    void method_614(int i, int j, int k) {
        int n = j + this.field_658;
        for (int i2 = j; i2 < n; ++i2) {
            float f = this.method_619(i2 - j);
            this.method_615(i, i2, k, f, (byte)1, Blocks.OAK_LEAVES.id);
        }
    }

    void method_617(int[] is, int[] js, int i) {
        int[] nArray = new int[]{0, 0, 0};
        int n = 0;
        for (int n2 = 0; n2 < 3; n2 = (byte)(n2 + 1)) {
            nArray[n2] = js[n2] - is[n2];
            if (Math.abs(nArray[n2]) <= Math.abs(nArray[n])) continue;
            n = n2;
        }
        if (nArray[n] == 0) return;
        byte by = field_645[n];
        byte by2 = field_645[n + 3];
        int n3 = nArray[n] > 0 ? 1 : -1;
        double d = (double)nArray[by] / (double)nArray[n];
        double d2 = (double)nArray[by2] / (double)nArray[n];
        int[] nArray2 = new int[]{0, 0, 0};
        int n4 = nArray[n] + n3;
        for (int j = 0; j != n4; j += n3) {
            nArray2[n] = MathHelper.floor((double)(is[n] + j) + 0.5);
            nArray2[by] = MathHelper.floor((double)is[by] + (double)j * d + 0.5);
            nArray2[by2] = MathHelper.floor((double)is[by2] + (double)j * d2 + 0.5);
            this.field_647.setTileInChunk(nArray2[0], nArray2[1], nArray2[2], i);
        }
    }

    void method_618() {
        int n = this.field_659.length;
        for (int i = 0; i < n; ++i) {
            int n2 = this.field_659[i][0];
            int n3 = this.field_659[i][1];
            int n4 = this.field_659[i][2];
            this.method_614(n2, n3, n4);
        }
    }

    boolean method_621(int i) {
        return !((double)i < (double)this.field_649 * 0.2);
    }

    void method_620() {
        int n = this.field_648[0];
        int n2 = this.field_648[1];
        int n3 = this.field_648[1] + this.field_650;
        int n4 = this.field_648[2];
        int[] nArray = new int[]{n, n2, n4};
        int[] nArray2 = new int[]{n, n3, n4};
        this.method_617(nArray, nArray2, BlockBase.LOG.id);
        if (this.field_656 == 2) {
            nArray[0] = nArray[0] + 1;
            nArray2[0] = nArray2[0] + 1;
            this.method_617(nArray, nArray2, BlockBase.LOG.id);
            nArray[2] = nArray[2] + 1;
            nArray2[2] = nArray2[2] + 1;
            this.method_617(nArray, nArray2, BlockBase.LOG.id);
            nArray[0] = nArray[0] + -1;
            nArray2[0] = nArray2[0] + -1;
            this.method_617(nArray, nArray2, BlockBase.LOG.id);
        }
    }

    void method_622() {
        int n = this.field_659.length;
        int[] nArray = new int[]{this.field_648[0], this.field_648[1], this.field_648[2]};
        for (int i = 0; i < n; ++i) {
            int[] nArray2 = this.field_659[i];
            int[] nArray3 = new int[]{nArray2[0], nArray2[1], nArray2[2]};
            nArray[1] = nArray2[3];
            int n2 = nArray[1] - this.field_648[1];
            if (!this.method_621(n2)) continue;
            this.method_617(nArray, nArray3, BlockBase.LOG.id);
        }
    }

    int method_616(int[] is, int[] js) {
        int n;
        int[] nArray = new int[]{0, 0, 0};
        int n2 = 0;
        for (int n3 = 0; n3 < 3; n3 = (byte)(n3 + 1)) {
            nArray[n3] = js[n3] - is[n3];
            if (Math.abs(nArray[n3]) <= Math.abs(nArray[n2])) continue;
            n2 = n3;
        }
        if (nArray[n2] == 0) return -1;
        byte by = field_645[n2];
        byte by2 = field_645[n2 + 3];
        int n4 = nArray[n2] > 0 ? 1 : -1;
        double d = (double)nArray[by] / (double)nArray[n2];
        double d2 = (double)nArray[by2] / (double)nArray[n2];
        int[] nArray2 = new int[]{0, 0, 0};
        int n5 = nArray[n2] + n4;
        for (n = 0; n != n5; n += n4) {
            nArray2[n2] = is[n2] + n;
            nArray2[by] = MathHelper.floor((double)is[by] + (double)n * d);
            nArray2[by2] = MathHelper.floor((double)is[by2] + (double)n * d2);
            int n6 = this.field_647.getTileId(nArray2[0], nArray2[1], nArray2[2]);
            if (n6 != 0 && !(BlockBase.BY_ID[n6] instanceof FixedLeaves)) break;
        }
        if (n == n5) return -1;
        return Math.abs(n);
    }

    boolean method_611() {
        int[] nArray = new int[]{this.field_648[0], this.field_648[1], this.field_648[2]};
        int[] nArray2 = new int[]{this.field_648[0], this.field_648[1] + this.field_649 - 1, this.field_648[2]};
        int n = this.field_647.getTileId(this.field_648[0], this.field_648[1] - 1, this.field_648[2]);
        if (n != BlockBase.GRASS.id && n != BlockBase.DIRT.id) return false;
        int n2 = this.method_616(nArray, nArray2);
        if (n2 == -1) return true;
        if (n2 < 6) return false;
        this.field_649 = n2;
        return true;
    }

    public void method_1143(double d, double e, double f) {
        this.field_657 = (int)(d * 12.0);
        if (d > 0.5) this.field_658 = 5;
        this.field_654 = e;
        this.field_655 = f;
    }

    public boolean generate(Level arg, Random random, int i, int j, int k) {
        this.field_647 = arg;
        long l = random.nextLong();
        this.field_646.setSeed(l);
        this.field_648[0] = i;
        this.field_648[1] = j;
        this.field_648[2] = k;
        if (this.field_649 == 0) this.field_649 = 5 + this.field_646.nextInt(this.field_657);
        if (!this.method_611()) return false;
        this.method_612();
        this.method_618();
        this.method_620();
        this.method_622();
        return true;
    }
}
