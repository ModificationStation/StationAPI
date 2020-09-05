package net.modificationstation.stationloader.impl.client.model;

import com.google.gson.Gson;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.impl.common.StationLoader;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CustomModelRenderer extends EntityRenderer implements net.modificationstation.stationloader.api.client.model.CustomModelRenderer {
    CustomModel entityModelBase;

    /*public CustomModelRenderer(String testModel) {
        if (testModel == null) {
            testModel = "{\"__comment\":\"Model generated using MrCrayfish's Model Creator (https://mrcrayfish.com/tools?id=mc)\",\"textures\":{\"Untitle44d\":\"minecraft:blocks/Untitle44d\"},\"display\":{\"gui\":{\"rotation\":[30,45,0],\"translation\":[0,0,0],\"scale\":[0.625,0.625,0.625]},\"ground\":{\"rotation\":[0,0,0],\"translation\":[0,3,0],\"scale\":[0.25,0.25,0.25]},\"fixed\":{\"rotation\":[0,180,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"head\":{\"rotation\":[0,180,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,315,0],\"translation\":[0,2.5,0],\"scale\":[0.4,0.4,0.4]},\"thirdperson_righthand\":{\"rotation\":[75,315,0],\"translation\":[0,2.5,0],\"scale\":[0.375,0.375,0.375]}},\"elements\":[{\"name\":\"Element\",\"from\":[0,0,0],\"to\":[2,16,16],\"faces\":{\"north\":{\"texture\":\"#Untitle44d\",\"uv\":[7,8,8,16]},\"east\":{\"texture\":\"#Untitle44d\",\"uv\":[8,8,16,16]},\"south\":{\"texture\":\"#Untitle44d\",\"uv\":[0,8,1,16]},\"west\":{\"texture\":\"#Untitle44d\",\"uv\":[8,8,16,16]},\"up\":{\"texture\":\"#Untitle44d\",\"uv\":[7,8,8,16]},\"down\":{\"texture\":\"#Untitle44d\",\"uv\":[0,8,1,16]}}},{\"name\":\"Element\",\"from\":[2,6,0],\"to\":[16,8,16],\"faces\":{\"north\":{\"texture\":\"#Untitle44d\",\"uv\":[16,5,9,4]},\"east\":{\"texture\":\"#Untitle44d\",\"uv\":[8,4,16,5]},\"south\":{\"texture\":\"#Untitle44d\",\"uv\":[9,4,16,5]},\"up\":{\"texture\":\"#Untitle44d\",\"uv\":[9,0,16,8]},\"down\":{\"texture\":\"#Untitle44d\",\"uv\":[1,0,8,8]}}},{\"name\":\"Element\",\"from\":[14,0,0],\"to\":[16,6,16],\"faces\":{\"north\":{\"texture\":\"#Untitle44d\",\"uv\":[3,5,4,8]},\"east\":{\"texture\":\"#Untitle44d\",\"uv\":[0,5,8,8]},\"south\":{\"texture\":\"#Untitle44d\",\"uv\":[3,5,4,8]},\"west\":{\"texture\":\"#Untitle44d\",\"uv\":[0,5,8,8]},\"down\":{\"texture\":\"#Untitle44d\",\"uv\":[7,0,8,8]}}}]}";
        }
        //String testModel = "{\"__comment\":\"Model generated using MrCrayfish's Model Creator (https://mrcrayfish.com/tools?id=mc)\",\"textures\":{\"Untitle44d\":\"minecraft:blocks/Untitle44d\"},\"display\":{\"gui\":{\"rotation\":[30,45,0],\"translation\":[0,0,0],\"scale\":[0.625,0.625,0.625]},\"ground\":{\"rotation\":[0,0,0],\"translation\":[0,3,0],\"scale\":[0.25,0.25,0.25]},\"fixed\":{\"rotation\":[0,180,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"head\":{\"rotation\":[0,180,0],\"translation\":[0,0,0],\"scale\":[1,1,1]},\"firstperson_righthand\":{\"rotation\":[0,315,0],\"translation\":[0,2.5,0],\"scale\":[0.4,0.4,0.4]},\"thirdperson_righthand\":{\"rotation\":[75,315,0],\"translation\":[0,2.5,0],\"scale\":[0.375,0.375,0.375]}},\"elements\":[{\"name\":\"Cube\",\"from\":[0,0,0],\"to\":[16,8,8],\"faces\":{\"north\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,16,8]},\"east\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,8,8]},\"west\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,8,8]},\"up\":{\"texture\":\"#Untitle44d\",\"uv\":[0,8,16,16]},\"down\":{\"texture\":\"#Untitle44d\",\"uv\":[0,8,16,16]}}},{\"name\":\"Cube\",\"from\":[0,0,8],\"to\":[16,16,16],\"faces\":{\"north\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,8,8]},\"east\":{\"texture\":\"#Untitle44d\",\"uv\":[8,0,16,16]},\"south\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,8,8]},\"west\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,8,16]},\"up\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,16,8]},\"down\":{\"texture\":\"#Untitle44d\",\"uv\":[0,0,16,8]}}}]}";
        JsonModel json = (new Gson()).fromJson(testModel, (Type) JsonModel.class);
        entityModelBase = new CustomModel(ModelTranslator.translate(json));
    }*/

    public CustomModelRenderer(String path, String modid) {
        String testModel = new BufferedReader(new InputStreamReader(StationLoader.class.getResourceAsStream(path), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        JsonModel json = (new Gson()).fromJson(testModel, (Type) JsonModel.class);
        entityModelBase = new CustomModel(ModelTranslator.translate(json, modid));
    }

    @Override
    public void render(EntityBase entity, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();

        GL11.glScalef(1f, 1f, 1f);
        GL11.glTranslatef((float)x-0.5F, (float)y, (float)z-0.5F);
        //GL11.glOrtho(0, 0, 0, 1, 1, -1);
        entityModelBase.render(0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public CustomModel getEntityModelBase() {
        return entityModelBase;
    }
}
