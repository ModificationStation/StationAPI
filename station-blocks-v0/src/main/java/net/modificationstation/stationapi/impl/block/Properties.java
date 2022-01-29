package net.modificationstation.stationapi.impl.block;

import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.JigsawOrientation;

/**
 * Contains all block and fluid state properties that Minecraft uses.
 */
public class Properties {
   /**
    * A property that specifies if a tripwire is attached to a tripwire hook.
    */
   public static final BooleanProperty ATTACHED = BooleanProperty.of("attached");
   /**
    * A property that specifies if a scaffolding block is bottom of a floating segment.
    */
   public static final BooleanProperty BOTTOM = BooleanProperty.of("bottom");
   /**
    * A property that specifies if a command block is conditional.
    */
   public static final BooleanProperty CONDITIONAL = BooleanProperty.of("conditional");
   /**
    * A property that specifies if a tripwire has been disarmed.
    */
   public static final BooleanProperty DISARMED = BooleanProperty.of("disarmed");
   /**
    * A property that specifies if a bubble column should drag entities downwards.
    */
   public static final BooleanProperty DRAG = BooleanProperty.of("drag");
   /**
    * A property that specifies whether a hopper is enabled.
    */
   public static final BooleanProperty ENABLED = BooleanProperty.of("enabled");
   /**
    * A property that specifies if a piston is extended.
    */
   public static final BooleanProperty EXTENDED = BooleanProperty.of("extended");
   /**
    * A property that specifies if an end portal frame contains an eye of ender.
    */
   public static final BooleanProperty EYE = BooleanProperty.of("eye");
   /**
    * A property that specifies if a fluid is falling.
    */
   public static final BooleanProperty FALLING = BooleanProperty.of("falling");
   /**
    * A property that specifies if a lantern is hanging.
    */
   public static final BooleanProperty HANGING = BooleanProperty.of("hanging");
   /**
    * A property that specifies if a brewing stand has a bottle in slot 0.
    */
   public static final BooleanProperty HAS_BOTTLE_0 = BooleanProperty.of("has_bottle_0");
   /**
    * A property that specifies if a brewing stand has a bottle in slot 1.
    */
   public static final BooleanProperty HAS_BOTTLE_1 = BooleanProperty.of("has_bottle_1");
   /**
    * A property that specifies if a brewing stand has a bottle in slot 2.
    */
   public static final BooleanProperty HAS_BOTTLE_2 = BooleanProperty.of("has_bottle_2");
   /**
    * A property that specifies if a jukebox has a record.
    */
   public static final BooleanProperty HAS_RECORD = BooleanProperty.of("has_record");
   /**
    * A property that specifies if a lectern has a book.
    */
   public static final BooleanProperty HAS_BOOK = BooleanProperty.of("has_book");
   /**
    * A property that specifies if a daylight sensor's output is inverted.
    */
   public static final BooleanProperty INVERTED = BooleanProperty.of("inverted");
   /**
    * A property that specifies if a fence gate is attached to a wall.
    * 
    * <p>This lowers the fence gate by 3 pixels to attach more cleanly to a wall.
    */
   public static final BooleanProperty IN_WALL = BooleanProperty.of("in_wall");
   /**
    * A property that specifies if a block is lit.
    */
   public static final BooleanProperty LIT = BooleanProperty.of("lit");
   /**
    * A property that specifies if a repeater is locked.
    */
   public static final BooleanProperty LOCKED = BooleanProperty.of("locked");
   /**
    * A property that specifies if a bed is occupied.
    */
   public static final BooleanProperty OCCUPIED = BooleanProperty.of("occupied");
   /**
    * A property that specifies if a block is open.
    * 
    * <p>This property is normally used for doors, trapdoors and fence gates but is also used by barrels.
    */
   public static final BooleanProperty OPEN = BooleanProperty.of("open");
   /**
    * A property that specifies if a block is persistent.
    * 
    * <p>In vanilla, this is used to specify whether leaves should disappear when the logs are removed.
    */
   public static final BooleanProperty PERSISTENT = BooleanProperty.of("persistent");
   /**
    * A property that specifies if a block is being powered to produce or emit redstone signal.
    */
   public static final BooleanProperty POWERED = BooleanProperty.of("powered");
   /**
    * A property that specifies if a piston head is shorter than normal.
    */
   public static final BooleanProperty SHORT = BooleanProperty.of("short");
   /**
    * A property that specifies if a campfire's smoke should be taller.
    * 
    * <p>This occurs when a hay bale is placed under the campfire.
    */
   public static final BooleanProperty SIGNAL_FIRE = BooleanProperty.of("signal_fire");
   /**
    * A property that specifies if a block is covered in snow.
    */
   public static final BooleanProperty SNOWY = BooleanProperty.of("snowy");
   /**
    * A property that specifies if a dispenser is activated.
    */
   public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");
   /**
    *  A property that specifies if TNT is unstable.
    * 
    *  <p>In vanilla, if TNT is unstable, it will ignite when broken.
    */
   public static final BooleanProperty UNSTABLE = BooleanProperty.of("unstable");
   /**
    * A property that specifies if a block is waterlogged.
    */
   public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");
   public static final BooleanProperty VINE_END = BooleanProperty.of("vine_end");
   /**
    *  A property that specifies the axis a block is oriented to.
    * 
    * <p>This property only allows a block to be oriented to the X and Z axes.
    */
   public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS;
   /**
    * A property that specifies the axis a block is oriented to.
    */
   public static final EnumProperty<Direction.Axis> AXIS;
   /**
    * A property that specifies if this block is connected to another block from the top.
    */
   public static final BooleanProperty UP;
   /**
    * A property that specifies if this block is connected to another block from the below.
    */
   public static final BooleanProperty DOWN;
   /**
    * A property that specifies if this block is connected to another block from the north.
    */
   public static final BooleanProperty NORTH;
   /**
    * A property that specifies if this block is connected to another block from the east.
    */
   public static final BooleanProperty EAST;
   /**
    * A property that specifies if this block is connected to another block from the south.
    */
   public static final BooleanProperty SOUTH;
   /**
    * A property that specifies if this block is connected to another block from the west.
    */
   public static final BooleanProperty WEST;
   /**
    * A property that specifies the direction a block is facing.
    */
   public static final DirectionProperty FACING;
   /**
    * A property that specifies the direction a hopper's output faces.
    * 
    * <p>This property does not allow the hopper's output to face upwards.
    */
   public static final DirectionProperty HOPPER_FACING;
   /**
    * A property that specifies the direction a block is facing.
    * 
    * <p>This property only allows a block to face in one of the cardinal directions (north, south, east and west).
    */
   public static final DirectionProperty HORIZONTAL_FACING;
   /**
    * A property that specifies the orientation of a jigsaw.
    */
   public static final EnumProperty<JigsawOrientation> ORIENTATION;
//   /**
//    * A property that specifies the type of wall a block is attached to.
//    */
//   public static final EnumProperty<WallMountLocation> WALL_MOUNT_LOCATION;
//   /**
//    * A property that specifies how a bell is attached to a block.
//    */
//   public static final EnumProperty<Attachment> ATTACHMENT;
//   /**
//    * A property that specifies how a wall extends from the center post to the east.‌
//    */
//   public static final EnumProperty<WallShape> EAST_WALL_SHAPE;
//   /**
//    * A property that specifies how a wall extends from the center post to the north.‌
//    */
//   public static final EnumProperty<WallShape> NORTH_WALL_SHAPE;
//   /**
//    * A property that specifies how a wall extends from the center post to the south.‌
//    */
//   public static final EnumProperty<WallShape> SOUTH_WALL_SHAPE;
//   /**
//    * A property that specifies how a wall extends from the center post to the west.‌
//    */
//   public static final EnumProperty<WallShape> WEST_WALL_SHAPE;
//   /**
//    * A property that specifies how redstone wire attaches to the east.‌
//    */
//   public static final EnumProperty<WireConnection> EAST_WIRE_CONNECTION;
//   /**
//    * A property that specifies how redstone wire attaches to the north.‌
//    */
//   public static final EnumProperty<WireConnection> NORTH_WIRE_CONNECTION;
//   /**
//    * A property that specifies how redstone wire attaches to the south.‌
//    */
//   public static final EnumProperty<WireConnection> SOUTH_WIRE_CONNECTION;
//   /**
//    * A property that specifies how redstone wire attaches to the west.‌
//    */
//   public static final EnumProperty<WireConnection> WEST_WIRE_CONNECTION;
//   /**
//    * A property that specifies whether a double height block is the upper or lower half.
//    */
//   public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF;
//   /**
//    * A property that specifies if a block is the upper or lower half.
//    */
//   public static final EnumProperty<BlockHalf> BLOCK_HALF;
//   /**
//    * A property that specifies the two directions a rail connects to.
//    */
//   public static final EnumProperty<RailShape> RAIL_SHAPE;
//   /**
//    * A property that specifies the two directions a rail connects to.
//    *
//    * <p>This property does not allow for a rail to turn.
//    */
//   public static final EnumProperty<RailShape> STRAIGHT_RAIL_SHAPE;
   /**
    * A property that specifies the metadata of the block.
    */
   public static final IntProperty META = IntProperty.of("meta", 0, 15);
   /**
    * A property that specifies the age of a block on a scale of 0 to 1.
    */
   public static final IntProperty AGE_1;
   /**
    * A property that specifies the age of a block on a scale of 0 to 2.
    */
   public static final IntProperty AGE_2;
   /**
    * A property that specifies the age of a block on a scale of 0 to 3.
    */
   public static final IntProperty AGE_3;
   /**
    * A property that specifies the age of a block on a scale of 0 to 5.
    */
   public static final IntProperty AGE_5;
   /**
    * A property that specifies the age of a block on a scale of 0 to 7.
    */
   public static final IntProperty AGE_7;
   /**
    * A property that specifies the age of a block on a scale of 0 to 15.
    */
   public static final IntProperty AGE_15;
   /**
    * A property that specifies the age of a block on a scale of 0 to 25.
    */
   public static final IntProperty AGE_25;
   /**
    * A property that specifies the bites taken out of a cake.
    */
   public static final IntProperty BITES;
   /**
    * A property that specifies the delay a repeater will apply.
    */
   public static final IntProperty DELAY;
   /**
    * A property that specifies the overhang distance of a block on a scale of 1-7.
    */
   public static final IntProperty DISTANCE_1_7;
   /**
    * A property that specifies the amount of eggs in a turtle egg block.
    */
   public static final IntProperty EGGS;
   /**
    * A property that specifies how close an egg is hatching.
    */
   public static final IntProperty HATCH;
   /**
    * A property that specifies how many layers of snow are in a snow block.
    */
   public static final IntProperty LAYERS;
   /**
    * A property that specifies how many levels of water there are in a cauldron.
    */
   public static final IntProperty LEVEL_3;
   /**
    * A property that specifies the level of a composter.
    */
   public static final IntProperty LEVEL_8;
   /**
    * A property that specifies the height of a fluid on a scale of 1 to 8.
    */
   public static final IntProperty LEVEL_1_8;
   /**
    * A property that specifies the honey level of a beehive.
    */
   public static final IntProperty HONEY_LEVEL;
   public static final IntProperty LEVEL_15;
   /**
    * A property that specifies the moisture of farmland.
    */
   public static final IntProperty MOISTURE;
   /**
    * A property that specifies the pitch of a note block.
    */
   public static final IntProperty NOTE;
   /**
    * A property that specifies how many pickles are in a sea pickle.
    */
   public static final IntProperty PICKLES;
   /**
    * A property that specifies the redstone power of a block.
    */
   public static final IntProperty POWER;
   /**
    * A property that specifies a growth stage on a scale of 0 to 1.
    */
   public static final IntProperty STAGE;
   /**
    * A property that specifies the overhang distance of a scaffolding.
    */
   public static final IntProperty DISTANCE_0_7;
   /**
    * A property that specifies the amount of charges a respawn anchor has.
    */
   public static final IntProperty CHARGES;
   /**
    * A property that specifies the rotation of a block on a 0 to 15 scale.
    * 
    * <p>Each rotation is 22.5 degrees.
    */
   public static final IntProperty ROTATION;
//   /**
//    * A property that specifies what part of a bed a block is.
//    */
//   public static final EnumProperty<BedPart> BED_PART;
//   /**
//    * A property that specifies what type of chest a block is.
//    */
//   public static final EnumProperty<ChestType> CHEST_TYPE;
//   /**
//    * A property that specifies the mode a comparator is set to.
//    */
//   public static final EnumProperty<ComparatorMode> COMPARATOR_MODE;
//   /**
//    * A property that specifies whether a door's hinge is to the right or left.
//    */
//   public static final EnumProperty<DoorHinge> DOOR_HINGE;
//   /**
//    * A property that specifies what instrument a note block will play.
//    */
//   public static final EnumProperty<Instrument> INSTRUMENT;
//   /**
//    * A property that specifies the type of a piston.
//    */
//   public static final EnumProperty<PistonType> PISTON_TYPE;
//   /**
//    * A property that specifies the type of slab.
//    */
//   public static final EnumProperty<SlabType> SLAB_TYPE;
//   /**
//    * A property that specifies the shape of a stair block.
//    */
//   public static final EnumProperty<StairShape> STAIR_SHAPE;
//   /**
//    * A property that specifies the mode of a structure block.
//    */
//   public static final EnumProperty<StructureBlockMode> STRUCTURE_BLOCK_MODE;
//   /**
//    * A property that specifies the size of bamboo leaves.
//    */
//   public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES;

   static {
      HORIZONTAL_AXIS = EnumProperty.of("axis", Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z);
      AXIS = EnumProperty.of("axis", Direction.Axis.class);
      UP = BooleanProperty.of("up");
      DOWN = BooleanProperty.of("down");
      NORTH = BooleanProperty.of("north");
      EAST = BooleanProperty.of("east");
      SOUTH = BooleanProperty.of("south");
      WEST = BooleanProperty.of("west");
      FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
      HOPPER_FACING = DirectionProperty.of("facing", (facing) -> facing != Direction.UP);
      HORIZONTAL_FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);
      ORIENTATION = EnumProperty.of("orientation", JigsawOrientation.class);
//      WALL_MOUNT_LOCATION = EnumProperty.of("face", WallMountLocation.class);
//      ATTACHMENT = EnumProperty.of("attachment", Attachment.class);
//      EAST_WALL_SHAPE = EnumProperty.of("east", WallShape.class);
//      NORTH_WALL_SHAPE = EnumProperty.of("north", WallShape.class);
//      SOUTH_WALL_SHAPE = EnumProperty.of("south", WallShape.class);
//      WEST_WALL_SHAPE = EnumProperty.of("west", WallShape.class);
//      EAST_WIRE_CONNECTION = EnumProperty.of("east", WireConnection.class);
//      NORTH_WIRE_CONNECTION = EnumProperty.of("north", WireConnection.class);
//      SOUTH_WIRE_CONNECTION = EnumProperty.of("south", WireConnection.class);
//      WEST_WIRE_CONNECTION = EnumProperty.of("west", WireConnection.class);
//      DOUBLE_BLOCK_HALF = EnumProperty.of("half", DoubleBlockHalf.class);
//      BLOCK_HALF = EnumProperty.of("half", BlockHalf.class);
//      RAIL_SHAPE = EnumProperty.of("shape", RailShape.class);
//      STRAIGHT_RAIL_SHAPE = EnumProperty.of("shape", RailShape.class, (shape) -> {
//         return shape != RailShape.NORTH_EAST && shape != RailShape.NORTH_WEST && shape != RailShape.SOUTH_EAST && shape != RailShape.SOUTH_WEST;
//      });
      AGE_1 = IntProperty.of("age", 0, 1);
      AGE_2 = IntProperty.of("age", 0, 2);
      AGE_3 = IntProperty.of("age", 0, 3);
      AGE_5 = IntProperty.of("age", 0, 5);
      AGE_7 = IntProperty.of("age", 0, 7);
      AGE_15 = IntProperty.of("age", 0, 15);
      AGE_25 = IntProperty.of("age", 0, 25);
      BITES = IntProperty.of("bites", 0, 6);
      DELAY = IntProperty.of("delay", 1, 4);
      DISTANCE_1_7 = IntProperty.of("distance", 1, 7);
      EGGS = IntProperty.of("eggs", 1, 4);
      HATCH = IntProperty.of("hatch", 0, 2);
      LAYERS = IntProperty.of("layers", 1, 8);
      LEVEL_3 = IntProperty.of("level", 0, 3);
      LEVEL_8 = IntProperty.of("level", 0, 8);
      LEVEL_1_8 = IntProperty.of("level", 1, 8);
      HONEY_LEVEL = IntProperty.of("honey_level", 0, 5);
      LEVEL_15 = IntProperty.of("level", 0, 15);
      MOISTURE = IntProperty.of("moisture", 0, 7);
      NOTE = IntProperty.of("note", 0, 24);
      PICKLES = IntProperty.of("pickles", 1, 4);
      POWER = IntProperty.of("power", 0, 15);
      STAGE = IntProperty.of("stage", 0, 1);
      DISTANCE_0_7 = IntProperty.of("distance", 0, 7);
      CHARGES = IntProperty.of("charges", 0, 4);
      ROTATION = IntProperty.of("rotation", 0, 15);
//      BED_PART = EnumProperty.of("part", BedPart.class);
//      CHEST_TYPE = EnumProperty.of("type", ChestType.class);
//      COMPARATOR_MODE = EnumProperty.of("mode", ComparatorMode.class);
//      DOOR_HINGE = EnumProperty.of("hinge", DoorHinge.class);
//      INSTRUMENT = EnumProperty.of("instrument", Instrument.class);
//      PISTON_TYPE = EnumProperty.of("type", PistonType.class);
//      SLAB_TYPE = EnumProperty.of("type", SlabType.class);
//      STAIR_SHAPE = EnumProperty.of("shape", StairShape.class);
//      STRUCTURE_BLOCK_MODE = EnumProperty.of("mode", StructureBlockMode.class);
//      BAMBOO_LEAVES = EnumProperty.of("leaves", BambooLeaves.class);
   }
}
