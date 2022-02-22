package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class VertexFormats {
   public static final VertexFormatElement POSITION_ELEMENT;
   public static final VertexFormatElement COLOR_ELEMENT;
   public static final VertexFormatElement TEXTURE_ELEMENT;
   public static final VertexFormatElement OVERLAY_ELEMENT;
   public static final VertexFormatElement LIGHT_ELEMENT;
   public static final VertexFormatElement NORMAL_ELEMENT;
   public static final VertexFormatElement PADDING_ELEMENT;
   public static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT_NORMAL;
   public static final VertexFormat POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
   @Deprecated
   public static final VertexFormat POSITION_TEXTURE_COLOR_LIGHT;
   public static final VertexFormat POSITION;
   public static final VertexFormat POSITION_COLOR;
   public static final VertexFormat POSITION_COLOR_LIGHT;
   public static final VertexFormat POSITION_TEXTURE;
   public static final VertexFormat POSITION_COLOR_TEXTURE;
   @Deprecated
   public static final VertexFormat POSITION_TEXTURE_COLOR;
   public static final VertexFormat POSITION_COLOR_TEXTURE_LIGHT;
   @Deprecated
   public static final VertexFormat POSITION_TEXTURE_LIGHT_COLOR;
   public static final VertexFormat POSITION_TEXTURE_COLOR_NORMAL;

   static {
      POSITION_ELEMENT = new VertexFormatElement(0, VertexFormatElement.Format.FLOAT, VertexFormatElement.Type.POSITION, 3);
      COLOR_ELEMENT = new VertexFormatElement(0, VertexFormatElement.Format.UBYTE, VertexFormatElement.Type.COLOR, 4);
      TEXTURE_ELEMENT = new VertexFormatElement(0, VertexFormatElement.Format.FLOAT, VertexFormatElement.Type.UV, 2);
      OVERLAY_ELEMENT = new VertexFormatElement(1, VertexFormatElement.Format.SHORT, VertexFormatElement.Type.UV, 2);
      LIGHT_ELEMENT = new VertexFormatElement(2, VertexFormatElement.Format.SHORT, VertexFormatElement.Type.UV, 2);
      NORMAL_ELEMENT = new VertexFormatElement(0, VertexFormatElement.Format.BYTE, VertexFormatElement.Type.NORMAL, 3);
      PADDING_ELEMENT = new VertexFormatElement(0, VertexFormatElement.Format.BYTE, VertexFormatElement.Type.PADDING, 1);
      POSITION_COLOR_TEXTURE_LIGHT_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).add(TEXTURE_ELEMENT).add(LIGHT_ELEMENT).add(NORMAL_ELEMENT).add(PADDING_ELEMENT).build());
      POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).add(TEXTURE_ELEMENT).add(OVERLAY_ELEMENT).add(LIGHT_ELEMENT).add(NORMAL_ELEMENT).add(PADDING_ELEMENT).build());
      POSITION_TEXTURE_COLOR_LIGHT = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(TEXTURE_ELEMENT).add(COLOR_ELEMENT).add(LIGHT_ELEMENT).build());
      POSITION = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).build());
      POSITION_COLOR = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).build());
      POSITION_COLOR_LIGHT = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).add(LIGHT_ELEMENT).build());
      POSITION_TEXTURE = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(TEXTURE_ELEMENT).build());
      POSITION_COLOR_TEXTURE = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).add(TEXTURE_ELEMENT).build());
      POSITION_TEXTURE_COLOR = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(TEXTURE_ELEMENT).add(COLOR_ELEMENT).build());
      POSITION_COLOR_TEXTURE_LIGHT = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(COLOR_ELEMENT).add(TEXTURE_ELEMENT).add(LIGHT_ELEMENT).build());
      POSITION_TEXTURE_LIGHT_COLOR = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(TEXTURE_ELEMENT).add(LIGHT_ELEMENT).add(COLOR_ELEMENT).build());
      POSITION_TEXTURE_COLOR_NORMAL = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_ELEMENT).add(TEXTURE_ELEMENT).add(COLOR_ELEMENT).add(NORMAL_ELEMENT).add(PADDING_ELEMENT).build());
   }
}
