package io.disquark.rest.request;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Codecs {
    private static final Map<String, Codec> CODECS = new ConcurrentHashMap<>();
    private static final Codec JSON_CODEC = new JsonCodec();
    private static final Codec MULTIPART_CODEC = new MultipartCodec();

    static {
        ClassLoader tccl = getContextClassLoader();
        ServiceLoader<Codec> sl = ServiceLoader.load(Codec.class, tccl != null ? tccl : getSystemClassLoader());
        sl.forEach(codec -> CODECS.put(codec.getContentType(), codec));
    }

    private static ClassLoader getSystemClassLoader() {
        Supplier<ClassLoader> systemCl = () -> {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            return cl != null ? cl : new ClassLoader(null) {
            };
        };

        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) systemCl::get);
        }
        return systemCl.get();
    }

    private static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() != null) {
            return AccessController
                    .doPrivileged((PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader());
        }
        return Thread.currentThread().getContextClassLoader();
    }

    public static Codec getCodec(String contentType) {
        Codec codec = CODECS.get(contentType);
        if (codec == null) {
            if (contentType.equals(JsonCodec.CONTENT_TYPE)) {
                codec = JSON_CODEC;
            } else if (contentType.equals(MultipartCodec.CONTENT_TYPE)) {
                codec = MULTIPART_CODEC;
            } else {
                throw new NoSuchElementException("No codec implementation registered for content type " + contentType);
            }
        }
        return codec;
    }

    private Codecs() {
    }
}
