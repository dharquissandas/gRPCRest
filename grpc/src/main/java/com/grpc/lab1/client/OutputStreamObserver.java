package com.grpc.lab1.client;

import com.grpc.lab1.MultReply;
import io.grpc.stub.StreamObserver;

public class OutputStreamObserver implements StreamObserver<MultReply> {
    @Override
    public void onNext(MultReply multreply){
        System.out.println("Recieved: " + multreply);
    }
}