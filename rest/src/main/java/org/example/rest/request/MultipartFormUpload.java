package org.example.rest.request;

import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.Subscriptions;
import io.smallrye.mutiny.operators.AbstractMulti;
import io.vertx.core.http.impl.headers.HeadersAdaptor;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.multipart.FormDataPart;
import io.vertx.mutiny.ext.web.multipart.MultipartForm;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import static io.smallrye.mutiny.helpers.ParameterValidation.nonNull;
import static io.smallrye.mutiny.unchecked.Unchecked.supplier;

class MultipartFormUpload extends AbstractMulti<Buffer> {
    private static final Logger LOG = LoggerFactory.getLogger(MultipartFormUpload.class);
    private static final AtomicReferenceFieldUpdater<MultipartFormUpload, Subscriber> DOWNSTREAM_UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(MultipartFormUpload.class, Subscriber.class, "downstream");
    private static final UnpooledByteBufAllocator ALLOC = new UnpooledByteBufAllocator(false);

    private final DefaultFullHttpRequest request;
    private final HttpPostRequestEncoder encoder;

    private volatile Subscriber<? super Buffer> downstream;

    public MultipartFormUpload(MultipartForm form) throws Exception {
        this.request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        this.encoder = new HttpPostRequestEncoder(request, true);

        for (FormDataPart part : form) {
            MemoryFileUpload fileUpload = new MemoryFileUpload(part.name(), part.filename(), part.mediaType(), null,
                    null, part.content().length());

            fileUpload.setContent(part.content().getByteBuf());
            encoder.addBodyHttpData(fileUpload);
        }
        encoder.finalizeRequest();
    }

    private Multi<Buffer> encodeBody() {
        return Uni.createFrom().item(supplier(() -> Buffer.buffer(encoder.readChunk(ALLOC).content())))
                .repeat().until(y -> supplier(encoder::isEndOfInput).get())
                .onItem().invoke(() -> LOG.debug("Writing multipart-form body chunk"));
    }

    public MultiMap headers() {
        return MultiMap.newInstance(new HeadersAdaptor(request.headers()));
    }

    @Override
    public void subscribe(Subscriber<? super Buffer> downstream) {
        if (DOWNSTREAM_UPDATER.compareAndSet(this, null, nonNull(downstream, "downstream"))) {
            encodeBody().subscribe(downstream);
        } else {
            Subscriptions.fail(downstream, new IllegalStateException("Already subscribed"));
        }
    }
}