package net.modificationstation.stationloader.coremod;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.modificationstation.classloader.IClassTransformer;

/**
 * The class transformer that checks for class's name and then patches it (if needed)
 * 
 * @author mine_diver
 *
 */
public class EventsInjectorTransformer implements IClassTransformer{
    
    /**
     * List of patched classes
     */
    public static final List<String> transformedClasses = Arrays.asList(new String[] {
            "net.minecraft.src.GuiScreen"
    });
    
    /**
     * EventsInjector's instance
     */
    private final EventsInjector injector = new EventsInjector();

	@Override
	public byte[] transform(String name, byte[] bytes) {
	    //Checking for class's name
		if (transformedClasses.contains(name)) {
		    //If it's a class that we need to patch, call the EventsInjector to patch it
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