package com.grpc.lab1.server;
import com.grpc.lab1.AddRequest;
import com.grpc.lab1.AddReply;
import com.grpc.lab1.MultRequest;
import com.grpc.lab1.MultReply;
import com.grpc.lab1.CalculatorServiceGrpc.CalculatorServiceImplBase;
import io.grpc.stub.StreamObserver;
import java.util.*;

public class CalculatorServiceImpl extends CalculatorServiceImplBase{
    @Override
    public void add(AddRequest request, StreamObserver<AddReply> reply){
        System.out.println("Request recieved from client:\n" + request);
        int[][] a = convToMat(request.getAList());
        int[][] b = convToMat(request.getBList());
        int blockSize = a.length;
        int[][] c = new int[blockSize][blockSize];
        for (int i=0; i<c.length; i++){
            for (int j=0;j<c.length;j++){
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        AddReply response=AddReply.newBuilder().addAllC(convToDim(c)).build();
        reply.onNext(response);
        reply.onCompleted();
    }

    @Override
    public void mult(MultRequest request, StreamObserver<MultReply> reply){
        System.out.println("Request recieved from client:\n" + request);
        int[][] a = convToMat(request.getAList());
        int[][] b = convToMat(request.getBList());
        int blockSize = a.length;
        System.out.println(blockSize);
        int[][] c = new int[blockSize][blockSize];

        for(int i=0; i<blockSize; i++){ 
            for(int j=0; j<blockSize; j++){
                for(int k=0; k<blockSize; k++){
                    c[i][j]+=(a[i][k]*b[k][j]);
                }
            }
        }
        MultReply response=MultReply.newBuilder().addAllC(convToDim(c)).build();
        reply.onNext(response);
        reply.onCompleted();
    }

    public static List<Integer> convToDim(int[][] mat){
        List<Integer> B = new ArrayList<Integer>();
        for(int i=0; i<mat.length; i++) {
            for(int j=0; j<mat[i].length; j++) {
                B.add(mat[i][j]);
            }
        }
        return B;
    }

    public static int[][] convToMat(List<Integer> a){
        int dim = (int) Math.round(Math.sqrt(a.size()));
        int B[][] = new int[dim][dim];

        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                B[i][j] = a.get((i*dim) + j);
            }
        }

        return B;
    }
}