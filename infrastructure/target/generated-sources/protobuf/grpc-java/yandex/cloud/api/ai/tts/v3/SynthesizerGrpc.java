package yandex.cloud.api.ai.tts.v3;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service for adaptive synthesis from template with variable parts
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.56.1)",
    comments = "Source: speechkit/tts/v3/tts_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SynthesizerGrpc {

  private SynthesizerGrpc() {}

  public static final String SERVICE_NAME = "speechkit.tts.v3.Synthesizer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest,
      syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> getUtteranceSynthesisMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UtteranceSynthesis",
      requestType = syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest.class,
      responseType = syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest,
      syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> getUtteranceSynthesisMethod() {
    io.grpc.MethodDescriptor<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest, syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> getUtteranceSynthesisMethod;
    if ((getUtteranceSynthesisMethod = SynthesizerGrpc.getUtteranceSynthesisMethod) == null) {
      synchronized (SynthesizerGrpc.class) {
        if ((getUtteranceSynthesisMethod = SynthesizerGrpc.getUtteranceSynthesisMethod) == null) {
          SynthesizerGrpc.getUtteranceSynthesisMethod = getUtteranceSynthesisMethod =
              io.grpc.MethodDescriptor.<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest, syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UtteranceSynthesis"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SynthesizerMethodDescriptorSupplier("UtteranceSynthesis"))
              .build();
        }
      }
    }
    return getUtteranceSynthesisMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SynthesizerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynthesizerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynthesizerStub>() {
        @java.lang.Override
        public SynthesizerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynthesizerStub(channel, callOptions);
        }
      };
    return SynthesizerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SynthesizerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynthesizerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynthesizerBlockingStub>() {
        @java.lang.Override
        public SynthesizerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynthesizerBlockingStub(channel, callOptions);
        }
      };
    return SynthesizerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SynthesizerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SynthesizerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SynthesizerFutureStub>() {
        @java.lang.Override
        public SynthesizerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SynthesizerFutureStub(channel, callOptions);
        }
      };
    return SynthesizerFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service for adaptive synthesis from template with variable parts
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * tts here will change, just for MVP
     * </pre>
     */
    default void utteranceSynthesis(syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest request,
        io.grpc.stub.StreamObserver<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUtteranceSynthesisMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Synthesizer.
   * <pre>
   * Service for adaptive synthesis from template with variable parts
   * </pre>
   */
  public static abstract class SynthesizerImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SynthesizerGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Synthesizer.
   * <pre>
   * Service for adaptive synthesis from template with variable parts
   * </pre>
   */
  public static final class SynthesizerStub
      extends io.grpc.stub.AbstractAsyncStub<SynthesizerStub> {
    private SynthesizerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SynthesizerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynthesizerStub(channel, callOptions);
    }

    /**
     * <pre>
     * tts here will change, just for MVP
     * </pre>
     */
    public void utteranceSynthesis(syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest request,
        io.grpc.stub.StreamObserver<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getUtteranceSynthesisMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Synthesizer.
   * <pre>
   * Service for adaptive synthesis from template with variable parts
   * </pre>
   */
  public static final class SynthesizerBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SynthesizerBlockingStub> {
    private SynthesizerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SynthesizerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynthesizerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * tts here will change, just for MVP
     * </pre>
     */
    public java.util.Iterator<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse> utteranceSynthesis(
        syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getUtteranceSynthesisMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Synthesizer.
   * <pre>
   * Service for adaptive synthesis from template with variable parts
   * </pre>
   */
  public static final class SynthesizerFutureStub
      extends io.grpc.stub.AbstractFutureStub<SynthesizerFutureStub> {
    private SynthesizerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SynthesizerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SynthesizerFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_UTTERANCE_SYNTHESIS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UTTERANCE_SYNTHESIS:
          serviceImpl.utteranceSynthesis((syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest) request,
              (io.grpc.stub.StreamObserver<syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getUtteranceSynthesisMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisRequest,
              syandex.cloud.api.ai.tts.v3.Tts.UtteranceSynthesisResponse>(
                service, METHODID_UTTERANCE_SYNTHESIS)))
        .build();
  }

  private static abstract class SynthesizerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SynthesizerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return yandex.cloud.api.ai.tts.v3.TtsService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Synthesizer");
    }
  }

  private static final class SynthesizerFileDescriptorSupplier
      extends SynthesizerBaseDescriptorSupplier {
    SynthesizerFileDescriptorSupplier() {}
  }

  private static final class SynthesizerMethodDescriptorSupplier
      extends SynthesizerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SynthesizerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SynthesizerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SynthesizerFileDescriptorSupplier())
              .addMethod(getUtteranceSynthesisMethod())
              .build();
        }
      }
    }
    return result;
  }
}
