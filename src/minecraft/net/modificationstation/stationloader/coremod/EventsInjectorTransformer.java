package net.modificationstation.stationloader.coremod;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.modificationstation.classloader.IClassTransformer;

public class EventsInjectorTransformer implements IClassTransformer{
    
    public static final List<String> transformedClasses = Arrays.asList(new String[] {
            "net.minecraft.src.GuiScreen"
    });
    
    private final EventsInjector injector = new EventsInjector();

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (transformedClasses.contains(name)) {
			try {
				ClassNode classNode = new ClassNode();
				ClassReader classReader = new ClassReader(bytes);
				classReader.accept(classNode, 0);
				Consumer<ClassNode> injectFunc = injector.patch[transformedClasses.indexOf(name)];
				injectFunc.accept(classNode);
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				classNode.accept(classWriter);
				return  classWriter.toByteArray();
			} catch (Exception e) {e.printStackTrace();}
		}
		return bytes;
	}
}