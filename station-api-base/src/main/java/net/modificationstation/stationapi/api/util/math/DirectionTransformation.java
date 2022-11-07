package net.modificationstation.stationapi.api.util.math;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.Nullable;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DirectionTransformation implements StringIdentifiable {
   IDENTITY("identity", AxisTransformation.P123, false, false, false),
   ROT_180_FACE_XY("rot_180_face_xy", AxisTransformation.P123, true, true, false),
   ROT_180_FACE_XZ("rot_180_face_xz", AxisTransformation.P123, true, false, true),
   ROT_180_FACE_YZ("rot_180_face_yz", AxisTransformation.P123, false, true, true),
   ROT_120_NNN("rot_120_nnn", AxisTransformation.P231, false, false, false),
   ROT_120_NNP("rot_120_nnp", AxisTransformation.P312, true, false, true),
   ROT_120_NPN("rot_120_npn", AxisTransformation.P312, false, true, true),
   ROT_120_NPP("rot_120_npp", AxisTransformation.P231, true, false, true),
   ROT_120_PNN("rot_120_pnn", AxisTransformation.P312, true, true, false),
   ROT_120_PNP("rot_120_pnp", AxisTransformation.P231, true, true, false),
   ROT_120_PPN("rot_120_ppn", AxisTransformation.P231, false, true, true),
   ROT_120_PPP("rot_120_ppp", AxisTransformation.P312, false, false, false),
   ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", AxisTransformation.P213, true, true, true),
   ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", AxisTransformation.P213, false, false, true),
   ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", AxisTransformation.P321, true, true, true),
   ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", AxisTransformation.P321, false, true, false),
   ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", AxisTransformation.P132, true, true, true),
   ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", AxisTransformation.P132, true, false, false),
   ROT_90_X_NEG("rot_90_x_neg", AxisTransformation.P132, false, false, true),
   ROT_90_X_POS("rot_90_x_pos", AxisTransformation.P132, false, true, false),
   ROT_90_Y_NEG("rot_90_y_neg", AxisTransformation.P321, true, false, false),
   ROT_90_Y_POS("rot_90_y_pos", AxisTransformation.P321, false, false, true),
   ROT_90_Z_NEG("rot_90_z_neg", AxisTransformation.P213, false, true, false),
   ROT_90_Z_POS("rot_90_z_pos", AxisTransformation.P213, true, false, false),
   INVERSION("inversion", AxisTransformation.P123, true, true, true),
   INVERT_X("invert_x", AxisTransformation.P123, true, false, false),
   INVERT_Y("invert_y", AxisTransformation.P123, false, true, false),
   INVERT_Z("invert_z", AxisTransformation.P123, false, false, true),
   ROT_60_REF_NNN("rot_60_ref_nnn", AxisTransformation.P312, true, true, true),
   ROT_60_REF_NNP("rot_60_ref_nnp", AxisTransformation.P231, true, false, false),
   ROT_60_REF_NPN("rot_60_ref_npn", AxisTransformation.P231, false, false, true),
   ROT_60_REF_NPP("rot_60_ref_npp", AxisTransformation.P312, false, false, true),
   ROT_60_REF_PNN("rot_60_ref_pnn", AxisTransformation.P231, false, true, false),
   ROT_60_REF_PNP("rot_60_ref_pnp", AxisTransformation.P312, true, false, false),
   ROT_60_REF_PPN("rot_60_ref_ppn", AxisTransformation.P312, false, true, false),
   ROT_60_REF_PPP("rot_60_ref_ppp", AxisTransformation.P231, true, true, true),
   SWAP_XY("swap_xy", AxisTransformation.P213, false, false, false),
   SWAP_YZ("swap_yz", AxisTransformation.P132, false, false, false),
   SWAP_XZ("swap_xz", AxisTransformation.P321, false, false, false),
   SWAP_NEG_XY("swap_neg_xy", AxisTransformation.P213, true, true, false),
   SWAP_NEG_YZ("swap_neg_yz", AxisTransformation.P132, false, true, true),
   SWAP_NEG_XZ("swap_neg_xz", AxisTransformation.P321, true, false, true),
   ROT_90_REF_X_NEG("rot_90_ref_x_neg", AxisTransformation.P132, true, false, true),
   ROT_90_REF_X_POS("rot_90_ref_x_pos", AxisTransformation.P132, true, true, false),
   ROT_90_REF_Y_NEG("rot_90_ref_y_neg", AxisTransformation.P321, true, true, false),
   ROT_90_REF_Y_POS("rot_90_ref_y_pos", AxisTransformation.P321, false, true, true),
   ROT_90_REF_Z_NEG("rot_90_ref_z_neg", AxisTransformation.P213, false, true, true),
   ROT_90_REF_Z_POS("rot_90_ref_z_pos", AxisTransformation.P213, true, false, true);

   private final Matrix3f matrix;
   private final String name;
   @Nullable
   private Map<Direction, Direction> mappings;
   private final boolean flipX;
   private final boolean flipY;
   private final boolean flipZ;
   private final AxisTransformation axisTransformation;
   private static final DirectionTransformation[][] COMBINATIONS = Util.make(new DirectionTransformation[values().length][values().length], (directionTransformations) -> {
      Map<BiTuple<AxisTransformation, BooleanList>, DirectionTransformation> map = Arrays.stream(values()).collect(Collectors.toMap((directionTransformationx) -> Tuple.tuple(directionTransformationx.axisTransformation, directionTransformationx.getAxisFlips()), (directionTransformationx) -> directionTransformationx));
      DirectionTransformation[] var2 = values();

      for (DirectionTransformation directionTransformation : var2) {
         DirectionTransformation[] var6 = values();

         for (DirectionTransformation directionTransformation2 : var6) {
            BooleanList booleanList = directionTransformation.getAxisFlips();
            BooleanList booleanList2 = directionTransformation2.getAxisFlips();
            AxisTransformation axisTransformation = directionTransformation2.axisTransformation.prepend(directionTransformation.axisTransformation);
            BooleanArrayList booleanArrayList = new BooleanArrayList(3);

            for (int i = 0; i < 3; ++i) {
               booleanArrayList.add(booleanList.getBoolean(i) ^ booleanList2.getBoolean(directionTransformation.axisTransformation.map(i)));
            }

            directionTransformations[directionTransformation.ordinal()][directionTransformation2.ordinal()] = map.get(Tuple.tuple(axisTransformation, booleanArrayList));
         }
      }

   });
   private static final DirectionTransformation[] INVERSES = Arrays.stream(values()).map((directionTransformation) -> Arrays.stream(values()).filter((directionTransformation2) -> directionTransformation.prepend(directionTransformation2) == IDENTITY).findAny().orElseThrow(NullPointerException::new)).toArray(DirectionTransformation[]::new);

   DirectionTransformation(String name, AxisTransformation axisTransformation, boolean flipX, boolean flipY, boolean flipZ) {
      this.name = name;
      this.flipX = flipX;
      this.flipY = flipY;
      this.flipZ = flipZ;
      this.axisTransformation = axisTransformation;
      this.matrix = new Matrix3f();
      this.matrix.a00 = flipX ? -1.0F : 1.0F;
      this.matrix.a11 = flipY ? -1.0F : 1.0F;
      this.matrix.a22 = flipZ ? -1.0F : 1.0F;
      this.matrix.multiply(axisTransformation.getMatrix());
   }

   private BooleanList getAxisFlips() {
      return new BooleanArrayList(new boolean[]{this.flipX, this.flipY, this.flipZ});
   }

   public DirectionTransformation prepend(DirectionTransformation transformation) {
      return COMBINATIONS[this.ordinal()][transformation.ordinal()];
   }

   public String toString() {
      return this.name;
   }

   @Override
   public String asString() {
      return this.name;
   }

   public Direction map(Direction direction) {
      if (this.mappings == null) {
         this.mappings = Maps.newEnumMap(Direction.class);
         Direction[] var2 = Direction.values();

         for (Direction direction2 : var2) {
            Direction.Axis axis = direction2.getAxis();
            Direction.AxisDirection axisDirection = direction2.getDirection();
            Direction.Axis axis2 = Direction.Axis.values()[this.axisTransformation.map(axis.ordinal())];
            Direction.AxisDirection axisDirection2 = this.shouldFlipDirection(axis2) ? axisDirection.getOpposite() : axisDirection;
            Direction direction3 = Direction.from(axis2, axisDirection2);
            this.mappings.put(direction2, direction3);
         }
      }

      return this.mappings.get(direction);
   }

   public boolean shouldFlipDirection(Direction.Axis axis) {
      return switch (axis) {
         case X -> this.flipX;
         case Y -> this.flipY;
         case Z -> this.flipZ;
      };
   }

   public JigsawOrientation mapJigsawOrientation(JigsawOrientation orientation) {
      return JigsawOrientation.byDirections(this.map(orientation.getFacing()), this.map(orientation.getRotation()));
   }
}
