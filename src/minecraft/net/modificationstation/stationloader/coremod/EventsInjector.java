package net.modificationstation.stationloader.coremod;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

@SuppressWarnings("unchecked")
public class EventsInjector {    
    
    final Consumer<ClassNode> patch[];
    
    EventsInjector() {
        final List<Consumer<ClassNode>> list = new ArrayList<Consumer<ClassNode>>();
        list.add(this::patchGuiScreen);
        patch = list.toArray(new Consumer[]{});
    }
    
    public void patchGuiScreen(ClassNode classNode) {
        Iterator<MethodNode> methodIt = classNode.methods.iterator();
        for (MethodNode method = methodIt.next();methodIt.hasNext();method = methodIt.next()) {
            if (method.name.equals("setWorldAndResolution") && method.desc.equals("(Lnet/minecraft/client/Minecraft;II)V")) {
                Iterator<AbstractInsnNode> instructionIt = method.instructions.iterator();
                for (AbstractInsnNode instruction = instructionIt.next();instructionIt.hasNext();instruction = instructionIt.next())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode)instruction).name.equals("initGui") && ((MethodInsnNode)instruction).desc.equals("()V") && ((MethodInsnNode)instruction).owner.equals("net/minecraft/src/GuiScreen")) {
                        method.instructions.insertBefore(instruction.getPrevious(), ClassesInfo.GuiScreen.GuiScreenInit_before);
                        method.instructions.insert(instruction, ClassesInfo.GuiScreen.PostGuiScreenInit);
                        AbstractInsnNode PostGuiScreenInit_end = instruction;
                        for (int i = 0; i < 5;i++)
                            PostGuiScreenInit_end = PostGuiScreenInit_end.getNext();
                        method.instructions.insert(PostGuiScreenInit_end, ClassesInfo.GuiScreen.GuiScreenInit);
                    }
            }
            if (method.name.equals("mouseClicked") && method.desc.equals("(III)V")) {
                Iterator<AbstractInsnNode> instructionIt = method.instructions.iterator();
                for (AbstractInsnNode instruction = instructionIt.next();instructionIt.hasNext();instruction = instructionIt.next())
                    if (instruction.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode)instruction).name.equals("actionPerformed") && ((MethodInsnNode)instruction).desc.equals("(Lnet/minecraft/src/GuiButton;)V") && ((MethodInsnNode)instruction).owner.equals("net/minecraft/src/GuiScreen")) {
                        method.instructions.insertBefore(instruction.getPrevious().getPrevious(), ClassesInfo.GuiScreen.ActionPerformed_before);
                        method.instructions.insert(instruction, ClassesInfo.GuiScreen.ActionPerformed);
                    }
            }
            if (method.name.equals("drawScreen") && method.desc.equals("(IIF)V")) {
                Iterator<AbstractInsnNode> instructionIt = method.instructions.iterator();
                AbstractInsnNode returnNode = null;
                for (AbstractInsnNode instruction = instructionIt.next();instructionIt.hasNext();instruction = instructionIt.next()) {
                    if (instruction.getPrevious() == null)
                        method.instructions.insertBefore(instruction, ClassesInfo.GuiScreen.DrawScreen_before);
                    if (instruction.getOpcode() == RETURN)
                        returnNode = instruction;
                }
                method.instructions.insertBefore(returnNode, ClassesInfo.GuiScreen.DrawScreen);
            }
        }
    }
    
    private static void addEvent(InsnList toAdd, String eventPath) {
        toAdd.add(new FieldInsnNode(GETSTATIC, eventPath, "EVENT", "Lnet/modificationstation/stationloader/events/common/Event;"));
        toAdd.add(new MethodInsnNode(INVOKEVIRTUAL, "net/modificationstation/stationloader/events/common/Event", "getInvoker", "()Ljava/lang/Object;", false));
        toAdd.add(new TypeInsnNode(CHECKCAST, eventPath));
    }
    
    private static class ClassesInfo {
        static class GuiScreen {
            static final InsnList DrawScreen_before = new InsnList();
            static final InsnList DrawScreen = new InsnList();
            static final InsnList ActionPerformed_before = new InsnList();
            static final InsnList ActionPerformed = new InsnList();
            static final InsnList GuiScreenInit_before = new InsnList();
            static final InsnList PostGuiScreenInit = new InsnList();
            static final InsnList GuiScreenInit = new InsnList();
            
            static {
                LabelNode DrawScreen_pass = new LabelNode();
                addEvent(DrawScreen_before, "net/modificationstation/stationloader/events/client/gui/guiscreen/DrawScreen");
                DrawScreen_before.add(new VarInsnNode(ALOAD, 0));
                DrawScreen_before.add(new VarInsnNode(ILOAD, 1));
                DrawScreen_before.add(new VarInsnNode(ILOAD, 2));
                DrawScreen_before.add(new VarInsnNode(FLOAD, 3));
                DrawScreen_before.add(new LdcInsnNode("GuiScreen"));
                DrawScreen_before.add(new MethodInsnNode(INVOKEINTERFACE, "net/modificationstation/stationloader/events/client/gui/guiscreen/DrawScreen", "drawScreen", "(Lnet/minecraft/src/GuiScreen;IIFLjava/lang/String;)Z", true));
                DrawScreen_before.add(new JumpInsnNode(IFEQ, DrawScreen_pass));
                
                DrawScreen.add(DrawScreen_pass);
                
                LabelNode ActionPerformed_pass = new LabelNode();
                addEvent(ActionPerformed_before, "net/modificationstation/stationloader/events/client/gui/guiscreen/ActionPerformed");
                ActionPerformed_before.add(new VarInsnNode(ALOAD, 0));
                ActionPerformed_before.add(new VarInsnNode(ALOAD, 5));
                ActionPerformed_before.add(new MethodInsnNode(INVOKEINTERFACE, "net/modificationstation/stationloader/events/client/gui/guiscreen/ActionPerformed", "actionPerformed", "(Lnet/minecraft/src/GuiScreen;Lnet/minecraft/src/GuiButton;)Z", true));
                ActionPerformed_before.add(new JumpInsnNode(IFEQ, ActionPerformed_pass));
                
                ActionPerformed.add(ActionPerformed_pass);
                
                LabelNode GuiScreenInit_pass = new LabelNode();
                addEvent(GuiScreenInit_before, "net/modificationstation/stationloader/events/client/gui/guiscreen/GuiScreenInit");
                GuiScreenInit_before.add(new VarInsnNode(ALOAD, 0));
                GuiScreenInit_before.add(new MethodInsnNode(INVOKEINTERFACE, "net/modificationstation/stationloader/events/client/gui/guiscreen/GuiScreenInit", "initGuiScreen", "(Lnet/minecraft/src/GuiScreen;)Z", true));
                GuiScreenInit_before.add(new JumpInsnNode(IFEQ, GuiScreenInit_pass));
                
                addEvent(PostGuiScreenInit, "net/modificationstation/stationloader/events/client/gui/guiscreen/PostGuiScreenInit");
                PostGuiScreenInit.add(new VarInsnNode(ALOAD, 0));
                PostGuiScreenInit.add(new MethodInsnNode(INVOKEINTERFACE, "net/modificationstation/stationloader/events/client/gui/guiscreen/PostGuiScreenInit", "postInitGuiScreen", "(Lnet/minecraft/src/GuiScreen;)V", true));
                
                GuiScreenInit.add(GuiScreenInit_pass);
            }
        }
    }
}