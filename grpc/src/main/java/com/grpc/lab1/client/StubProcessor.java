package com.grpc.lab1.client;

import com.grpc.lab1.AddRequest;
import com.grpc.lab1.AddReply;
import com.grpc.lab1.MultRequest;
import com.grpc.lab1.MultReply;
import com.grpc.lab1.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.*;

// Callable Interface Adaption for Thread Process
class StubProcessor implements Callable<MultReply> {
    private CalculatorServiceGrpc.CalculatorServiceBlockingStub stub;
    private MultRequest request;
    private MultReply reply;

    public StubProcessor(CalculatorServiceGrpc.CalculatorServiceBlockingStub stub, MultRequest request){
        this.stub = stub;
        this.request = request;
    }

    public MultReply call(){
        return stub.mult(this.request);
    }
}
