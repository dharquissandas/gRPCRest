package com.grpc.lab1.client;

import com.grpc.lab1.AddRequest;
import com.grpc.lab1.AddReply;
import com.grpc.lab1.MultRequest;
import com.grpc.lab1.MultReply;
import com.grpc.lab1.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.*;

public class GrpcClient {
    public static void main(String[] args) {
        int MAX = 4;
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        int A[][] = { {1, 2, 3, 4}, 
                      {5, 6, 7, 8}, 
                      {9, 10, 11, 12},
                      {13, 14, 15, 16}}; 
   
        int B[][] = { {1, 2, 3, 4}, 
                	  {5, 6, 7, 8}, 
                	  {9, 10, 11, 12},
                	  {13, 14, 15, 16}};

        int[][] ans = multiplyMatrixBlock(A,B,stub);
        System.out.println("Final Answer");
        for (int i=0; i<MAX; i++){
            for (int j=0; j<MAX;j++){
                System.out.print(ans[i][j]+" ");
            }
            System.out.println("");
        }
        channel.shutdown();
    }
    
    public static int[][] multiplyMatrixBlock(int A[][], int B[][], CalculatorServiceGrpc.CalculatorServiceBlockingStub stub){
        int MAX = 4;
        int bSize= 2;
        int[][] A1 = new int[MAX][MAX];
        int[][] A2 = new int[MAX][MAX];
        int[][] A3 = new int[MAX][MAX];
        int[][] B1 = new int[MAX][MAX];
        int[][] B2 = new int[MAX][MAX];
        int[][] B3 = new int[MAX][MAX];
        int[][] C1 = new int[MAX][MAX];
        int[][] C2 = new int[MAX][MAX];
        int[][] C3 = new int[MAX][MAX];
        int[][] D1 = new int[MAX][MAX];
        int[][] D2 = new int[MAX][MAX];
        int[][] D3 = new int[MAX][MAX];
        int[][] res= new int[MAX][MAX];

        for (int i = 0; i < bSize; i++){
            for (int j = 0; j < bSize; j++){
                A1[i][j]=A[i][j];
                A2[i][j]=B[i][j];
            }
        }

        for (int i = 0; i < bSize; i++){
            for (int j = bSize; j < MAX; j++){
                B1[i][j-bSize]=A[i][j];
                B2[i][j-bSize]=B[i][j];
            }
        }
        
        for (int i = bSize; i < MAX; i++){
            for (int j = 0; j < bSize; j++){
                C1[i-bSize][j]=A[i][j];
                C2[i-bSize][j]=B[i][j];
            }
        }

        for (int i = bSize; i < MAX; i++){
            for (int j = bSize; j < MAX; j++){
                D1[i-bSize][j-bSize]=A[i][j];
                D2[i-bSize][j-bSize]=B[i][j];
            }
        }

        List<Integer> A3p1 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(A1)).addAllB(convToDim(A2)).build()).getCList();
        List<Integer> A3p2 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(B1)).addAllB(convToDim(C2)).build()).getCList();
        A3 = convToMat(stub.add(AddRequest.newBuilder().addAllA(A3p1).addAllB(A3p2).build()).getCList());

        List<Integer> B3p1 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(A1)).addAllB(convToDim(B2)).build()).getCList();
        List<Integer> B3p2 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(B1)).addAllB(convToDim(D2)).build()).getCList();
        B3 = convToMat(stub.add(AddRequest.newBuilder().addAllA(B3p1).addAllB(B3p2).build()).getCList());

        List<Integer> C3p1 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(C1)).addAllB(convToDim(A2)).build()).getCList();
        List<Integer> C3p2 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(D1)).addAllB(convToDim(C2)).build()).getCList();
        C3 = convToMat(stub.add(AddRequest.newBuilder().addAllA(C3p1).addAllB(C3p2).build()).getCList());

        List<Integer> D3p1 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(C1)).addAllB(convToDim(B2)).build()).getCList();
        List<Integer> D3p2 = stub.mult(MultRequest.newBuilder().addAllA(convToDim(D1)).addAllB(convToDim(D2)).build()).getCList();
        D3 = convToMat(stub.add(AddRequest.newBuilder().addAllA(D3p1).addAllB(D3p2).build()).getCList());

        for (int i = 0; i < bSize; i++){
            for (int j = 0; j < bSize; j++){
                res[i][j]=A3[i][j];
            }
        }

        for (int i = 0; i < bSize; i++){
            for (int j = bSize; j < MAX; j++){
                res[i][j]=B3[i][j-bSize];
            }
        }

        for (int i = bSize; i < MAX; i++){
            for (int j = 0; j < bSize; j++){
                res[i][j]=C3[i-bSize][j];
            }
        }

        for (int i = bSize; i < MAX; i++){
            for (int j = bSize; j < MAX; j++){
                res[i][j]=D3[i-bSize][j-bSize];
            }
        }
        return res;
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
        int B[][] = new int[4][4];

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                B[i][j] = a.get((i*4) + j);
            }
        }

        return B;
    }
}