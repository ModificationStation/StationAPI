package net.modificationstation.stationloader.coremod;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import net.modificationstation.classloader.ClassLoaderReplacer;
import net.modificationstation.classloader.IClassTransformer;
import net.modificationstation.classloader.Side;

/**
 * The class that removes anything with SideOnly's value isn't matching the current side
 * 
 * @author mine_diver
 *
 */
public class SideTransformer implements IClassTransformer
{
    private static Side SIDE = ClassLoaderReplacer.side();
    private static final boolean DEBUG = true;
    @SuppressWarnings("unchecked")
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        Iterator<FieldNode> fields = classNode.fields.iterator();
        while(fields.hasNext())
        {
            FieldNode field = fields.next();
            if (remove((List<AnnotationNode>)field.visibleAnnotations, SIDE))
            {
                if (DEBUG)
                {
                    System.out.println(String.format("Removing Field: %s.%s", classNode.name, field.name));
                }
                fields.remove();
            }
        }
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode method = methods.next();
            if (remove((List<AnnotationNode>)method.visibleAnnotations, SIDE))
            {
                if (DEBUG)
                {
                    System.out.println(String.format("Removing Method: %s.%s%s", classNode.name, method.name, method.desc));
                }
                methods.remove();
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private boolean remove(List<AnnotationNode> anns, Side side)
    {
        if (anns == null)
        {
            return false;
        }
        for (AnnotationNode ann : anns)
        {
            if (ann.desc.equals(Type.getDescriptor(SideOnly.class)))
            {
                if (ann.values != null)
                {
                    for (int x = 0; x < ann.values.size() - 1; x += 2)
                    {
                        Object key = ann.values.get(x);
                        Object value = ann.values.get(x+1);
                        if (key instanceof String && key.equals("value"))
                        {
                            if (value instanceof String[] )
                            {
                                if (!((String[])value)[1].equals(side.toString()))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
