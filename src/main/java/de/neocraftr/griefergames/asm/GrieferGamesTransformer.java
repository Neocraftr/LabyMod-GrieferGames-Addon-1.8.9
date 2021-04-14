package de.neocraftr.griefergames.asm;

import net.labymod.core.asm.LabyModCoreMod;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GrieferGamesTransformer implements IClassTransformer {

    private final String scaledResolutionName = LabyModCoreMod.isObfuscated() ? "avr" : "net.minecraft.client.gui.ScaledResolution";

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

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }
}
