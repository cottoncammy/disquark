package org.example.rest.request.multipart;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class HttpPostRequestEncoderClassVisitor extends ClassVisitor {

    public HttpPostRequestEncoderClassVisitor(int api) {
        super(api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
