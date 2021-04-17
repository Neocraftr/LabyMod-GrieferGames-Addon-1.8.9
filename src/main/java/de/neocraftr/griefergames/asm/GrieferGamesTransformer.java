package de.neocraftr.griefergames.asm;

import net.labymod.core.asm.LabyModCoreMod;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GrieferGamesTransformer implements IClassTransformer {

    private final String scaledResolutionName = LabyModCoreMod.isObfuscated() ? "avr" : "net.minecraft.client.gui.ScaledResolution";
    private final String minecraftName = LabyModCoreMod.isObfuscated() ? "ave" : "net.minecraft.client.Minecraft";
    private final String rightClickMouseName = LabyModCoreMod.isObfuscated() ? "ax" : "rightClickMouse";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(node, 0);

        if(name.equals(scaledResolutionName) || transformedName.equals(scaledResolutionName)) {
            node.methods.stream()
                    .filter(methodNode -> methodNode.name.equals("<init>"))
                    .findFirst()
                    .ifPresent(methodNode -> {
                        InsnList list = new InsnList();
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/neocraftr/griefergames/plots/gui/PlotSwitchGui", "scaleResolution", "()V", false));
                        methodNode.instructions.insert(methodNode.instructions.getFirst(), list);

                    });
        }

        if(name.equals(minecraftName) || transformedName.equals(minecraftName)) {
            node.methods.stream()
                    .filter(methodNode -> methodNode.name.equals(rightClickMouseName) && methodNode.desc.equals("()V"))
                    .findFirst()
                    .ifPresent(methodNode -> {
                        InsnList insnList = new InsnList();
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/neocraftr/griefergames/utils/BytecodeMethods", "handleRightClick", "()Z", false));

                        LabelNode labelNode = new LabelNode();
                        insnList.add(new JumpInsnNode(Opcodes.IFNE, labelNode));

                        methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), insnList);
                        methodNode.instructions.insertBefore(methodNode.instructions.getLast().getPrevious(), labelNode);
                        methodNode.instructions.insertBefore(labelNode, new FrameNode(Opcodes.F_SAME, 0, null, 1, null));
                    });
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
