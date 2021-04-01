package com.grpc.lab1.client;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grpc.lab1.client.storage.StorageFileNotFoundException;
import com.grpc.lab1.client.storage.StorageService;

import com.grpc.lab1.AddRequest;
import com.grpc.lab1.AddReply;
import com.grpc.lab1.MultRequest;
import com.grpc.lab1.MultReply;
import com.grpc.lab1.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.*;
import java.util.concurrent.*;

@Controller
public class FileUploadController {

	private final StorageService storageService;
    int[][] matone;
    int[][] mattwo;
    int dim;
    int dim1;

    String ip1 = "172.31.59.16";
    String ip2 = "172.31.54.127";
    String ip3 = "172.31.71.110";
    String ip4 = "172.31.72.237";
    String ip5 = "172.31.68.186";
    String ip6 = "172.31.68.185";
    String ip7 = "172.31.70.198";
    String ip8 = "172.31.65.230";

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("file1") MultipartFile file1, RedirectAttributes redirectAttributes) throws IOException {
        String content = new String(file.getBytes());
        String[] matrixarray = content.split("\\s+");
        dim = (int) Math.round(Math.sqrt(matrixarray.length));

        String content1 = new String(file1.getBytes());
        String[] matrixarray1 = content1.split("\\s+");
        dim1 = (int) Math.round(Math.sqrt(matrixarray1.length));

        if(checkValidMat(matrixarray.length) && checkValidMat(matrixarray1.length) && dim1 == dim){
            matone = filetoMat(file);
            mattwo = filetoMat(file1);
            redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + " and " + file1.getOriginalFilename() + "!");
        }
        else{
             redirectAttributes.addFlashAttribute("message",
				file.getOriginalFilename() + " or " + file1.getOriginalFilename() + " has invalid dimensions!");
        }

		return "redirect:/";
	}

    @GetMapping("/calculate")
	public String handlecalculate(Model model){
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress(ip1, 8081).usePlaintext().build();
        ManagedChannel channel2 = ManagedChannelBuilder.forAddress(ip2, 8081).usePlaintext().build();
        ManagedChannel channel3 = ManagedChannelBuilder.forAddress(ip3, 8081).usePlaintext().build();
        ManagedChannel channel4 = ManagedChannelBuilder.forAddress(ip4, 8081).usePlaintext().build();
        ManagedChannel channel5 = ManagedChannelBuilder.forAddress(ip5, 8081).usePlaintext().build();
        ManagedChannel channel6 = ManagedChannelBuilder.forAddress(ip6, 8081).usePlaintext().build();
        ManagedChannel channel7 = ManagedChannelBuilder.forAddress(ip7, 8081).usePlaintext().build();
        ManagedChannel channel8 = ManagedChannelBuilder.forAddress(ip8, 8081).usePlaintext().build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub1 = CalculatorServiceGrpc.newBlockingStub(channel1);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub2 = CalculatorServiceGrpc.newBlockingStub(channel2);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub3 = CalculatorServiceGrpc.newBlockingStub(channel3);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub4 = CalculatorServiceGrpc.newBlockingStub(channel4);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub5 = CalculatorServiceGrpc.newBlockingStub(channel5);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub6 = CalculatorServiceGrpc.newBlockingStub(channel6);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub7 = CalculatorServiceGrpc.newBlockingStub(channel7);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub8 = CalculatorServiceGrpc.newBlockingStub(channel8);


        int[][] ans = multiplyMatrixBlock(matone,mattwo,stub1, stub2, stub3, stub4, stub5, stub6, stub7, stub8);

        channel1.shutdown();
        channel2.shutdown();
        channel3.shutdown();
        channel4.shutdown();
        channel5.shutdown();
        channel6.shutdown();
        channel7.shutdown();
        channel8.shutdown();

        model.addAttribute("ans", convToString(ans));
        return "calculate";
	}

    @GetMapping("/deadlineCalculate")
	public String handleDeadlineCalculate(Model model, @RequestParam(value="deadline", defaultValue="") String deadline){
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress(ip1, 8081).usePlaintext().build();
        ManagedChannel channel2 = ManagedChannelBuilder.forAddress(ip2, 8081).usePlaintext().build();
        ManagedChannel channel3 = ManagedChannelBuilder.forAddress(ip3, 8081).usePlaintext().build();
        ManagedChannel channel4 = ManagedChannelBuilder.forAddress(ip4, 8081).usePlaintext().build();
        ManagedChannel channel5 = ManagedChannelBuilder.forAddress(ip5, 8081).usePlaintext().build();
        ManagedChannel channel6 = ManagedChannelBuilder.forAddress(ip6, 8081).usePlaintext().build();
        ManagedChannel channel7 = ManagedChannelBuilder.forAddress(ip7, 8081).usePlaintext().build();
        ManagedChannel channel8 = ManagedChannelBuilder.forAddress(ip8, 8081).usePlaintext().build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub1 = CalculatorServiceGrpc.newBlockingStub(channel1);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub2 = CalculatorServiceGrpc.newBlockingStub(channel2);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub3 = CalculatorServiceGrpc.newBlockingStub(channel3);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub4 = CalculatorServiceGrpc.newBlockingStub(channel4);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub5 = CalculatorServiceGrpc.newBlockingStub(channel5);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub6 = CalculatorServiceGrpc.newBlockingStub(channel6);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub7 = CalculatorServiceGrpc.newBlockingStub(channel7);
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub8 = CalculatorServiceGrpc.newBlockingStub(channel8);

        long timeDeadline = Long.parseLong(deadline);

        int[][] ans = multiplyMatrixBlockDeadline(matone,mattwo,timeDeadline,stub1,stub2, stub3, stub4, stub5, stub6, stub7, stub8);

        channel1.shutdown();
        channel2.shutdown();
        channel3.shutdown();
        channel4.shutdown();
        channel5.shutdown();
        channel6.shutdown();
        channel7.shutdown();
        channel8.shutdown();

        if(ans.length==0){
            model.addAttribute("error", "Not enough servers to achieve deadline");
        }
        else{
            model.addAttribute("ans", convToString(ans));
        }
        return "calculate";
	}

    //Calculations

    public static int[][] multiplyMatrixBlock(int A[][], int B[][],
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub1,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub2,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub3,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub4,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub5,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub6,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub7,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub8)
    {
        int MAX = A.length;
        if(MAX == 2){
            List<Integer> Ap = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(A)).addAllB(convToDim(B)).build()).getCList();
            return convToMat(Ap);
        }
        else{
            int bSize= MAX/2;
            ArrayList<int[][]> ablocks = getBlocks(A);
            ArrayList<int[][]> bblocks = getBlocks(B);
            List<Integer> A3p1 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(0))).build()).getCList();
            List<Integer> A3p2 = stub2.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()).getCList();
            List<Integer> B3p1 = stub3.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()).getCList();
            List<Integer> B3p2 = stub4.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()).getCList();
            List<Integer> C3p1 = stub5.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()).getCList();
            List<Integer> C3p2 = stub6.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()).getCList();
            List<Integer> D3p1 = stub7.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()).getCList();
            List<Integer> D3p2 = stub8.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()).getCList();
            int[][] A3  = convToMat(stub1.add(AddRequest.newBuilder().addAllA(A3p1).addAllB(A3p2).build()).getCList());
            int[][] B3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(B3p1).addAllB(B3p2).build()).getCList());
            int[][] C3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(C3p1).addAllB(C3p2).build()).getCList());
            int[][] D3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(D3p1).addAllB(D3p2).build()).getCList());
            return resCalc(A3, B3, C3, D3, MAX, bSize);
        }
    }

    public static int[][] multiplyMatrixBlockDeadline(int A[][], int B[][], long deadline,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub1,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub2,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub3,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub4,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub5,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub6,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub7,
                                            CalculatorServiceGrpc.CalculatorServiceBlockingStub stub8
                                            )
    {
        int MAX = A.length;
        if(MAX == 2){
            List<Integer> Ap = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(A)).addAllB(convToDim(B)).build()).getCList();
            return convToMat(Ap);
        }
        else{
            int bSize= MAX/2;
            int numBlockCalls = 8;
            ArrayList<int[][]> ablocks = getBlocks(A);
            ArrayList<int[][]> bblocks = getBlocks(B);
            List<Integer> A3p1 = new ArrayList<Integer>();
            List<Integer> A3p2 = new ArrayList<Integer>();
            List<Integer> B3p1 = new ArrayList<Integer>();
            List<Integer> B3p2 = new ArrayList<Integer>();
            List<Integer> C3p1 = new ArrayList<Integer>();
            List<Integer> C3p2 = new ArrayList<Integer>();
            List<Integer> D3p1 = new ArrayList<Integer>();
            List<Integer> D3p2 = new ArrayList<Integer>();

            Future<MultReply> A3p2r = null;
            Future<MultReply> B3p1r = null;
            Future<MultReply> B3p2r = null;
            Future<MultReply> C3p1r = null;
            Future<MultReply> C3p2r = null;
            Future<MultReply> D3p1r = null;
            Future<MultReply> D3p2r = null;

            long startTime = System.nanoTime();
            A3p1 = stub1.mult((MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(0))).build())).getCList();
            long endTime = System.nanoTime();
            long footprint = endTime-startTime;
            int numberServer = (int) Math.round((footprint*numBlockCalls)/deadline);

            System.out.println(footprint);
            System.out.println(numberServer);
            switch(numberServer){
                case 1:
                    A3p2 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()).getCList();
                    B3p1 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()).getCList();
                    B3p2 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()).getCList();
                    C3p1 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()).getCList();
                    C3p2 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()).getCList();
                    D3p1 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()).getCList();
                    D3p2 = stub1.mult(MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()).getCList();
                    break;
                case 2:
                    ExecutorService executor1 = Executors.newFixedThreadPool(2);
                    A3p2r = executor1.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor1.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor1.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor1.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor1.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor1.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor1.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor1.shutdown();
                    break;
                case 3:
                    ExecutorService executor2 = Executors.newFixedThreadPool(3);
                    A3p2r = executor2.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor2.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor2.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor2.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor2.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor2.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor2.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor2.shutdown();
                    break;
                case 4:
                    ExecutorService executor3 = Executors.newFixedThreadPool(4);
                    A3p2r = executor3.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor3.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor3.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor3.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor3.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor3.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor3.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor3.shutdown();
                    break;
                case 5:
                    ExecutorService executor4 = Executors.newFixedThreadPool(5);
                    A3p2r = executor4.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor4.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor4.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor4.submit(new StubProcessor(stub5, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor4.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor4.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor4.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor4.shutdown();
                case 6:
                    ExecutorService executor5 = Executors.newFixedThreadPool(6);
                    A3p2r = executor5.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor5.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor5.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor5.submit(new StubProcessor(stub5, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor5.submit(new StubProcessor(stub6, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor5.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor5.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor5.shutdown();
                case 7:
                    ExecutorService executor6 = Executors.newFixedThreadPool(7);
                    A3p2r = executor6.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor6.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor6.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor6.submit(new StubProcessor(stub5, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor6.submit(new StubProcessor(stub6, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor6.submit(new StubProcessor(stub7, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor6.submit(new StubProcessor(stub1, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor6.shutdown();
                case 8:
                    ExecutorService executor7 = Executors.newFixedThreadPool(8);
                    A3p2r = executor7.submit(new StubProcessor(stub2, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(2))).build()));
                    B3p1r = executor7.submit(new StubProcessor(stub3, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(0))).addAllB(convToDim(bblocks.get(1))).build()));
                    B3p2r = executor7.submit(new StubProcessor(stub4, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(1))).addAllB(convToDim(bblocks.get(3))).build()));
                    C3p1r = executor7.submit(new StubProcessor(stub5, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(0))).build()));
                    C3p2r = executor7.submit(new StubProcessor(stub6, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(2))).build()));
                    D3p1r = executor7.submit(new StubProcessor(stub7, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(2))).addAllB(convToDim(bblocks.get(1))).build()));
                    D3p2r = executor7.submit(new StubProcessor(stub8, MultRequest.newBuilder().addAllA(convToDim(ablocks.get(3))).addAllB(convToDim(bblocks.get(3))).build()));
                    executor7.shutdown();
                default:
                    System.out.println("Not Enough Servers to compute");
                    return new int[0][0];
            }
            if(numberServer != 1){
                try{
                    A3p2 = A3p2r.get().getCList();
                    B3p1 = B3p1r.get().getCList();
                    B3p2 = B3p2r.get().getCList();
                    C3p1 = C3p1r.get().getCList();
                    C3p2 = C3p2r.get().getCList();
                    D3p1 = D3p1r.get().getCList();
                    D3p2 = D3p2r.get().getCList();
                }
                catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
            int[][] A3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(A3p1).addAllB(A3p2).build()).getCList());
            int[][] B3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(B3p1).addAllB(B3p2).build()).getCList());
            int[][] C3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(C3p1).addAllB(C3p2).build()).getCList());
            int[][] D3 = convToMat(stub1.add(AddRequest.newBuilder().addAllA(D3p1).addAllB(D3p2).build()).getCList());
            return resCalc(A3, B3, C3, D3, MAX, bSize);
        }
    }

    public static ArrayList<int[][]> getBlocks(int mat[][]){
        int dim = mat[0].length;
        int blockdim = dim/2;
        ArrayList<int[][]> blocks = new ArrayList<int[][]>();
        for(int i = 0; i < mat[0].length; i=i+blockdim){
            for(int j = 0; j < mat[0].length; j=j+blockdim){
                int[][] a = new int[blockdim][blockdim];
                for(int k=0; k < blockdim; k++){
                    for(int l=0; l < blockdim; l++){
                        a[l][k] = mat[l+i][j+k];
                    }
                }
                blocks.add(a);
            }
        }
        return blocks;
    }

    public static int[][] resCalc(int[][] A3, int[][] B3, int[][] C3, int[][] D3, int MAX, int bSize){
        int[][] res= new int[MAX][MAX];

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

    //Utils

    public int[][] filetoMat(MultipartFile file) throws IOException{
        String content = new String(file.getBytes());
        String[] matrixarray = content.split("\\s+");
        int B[][] = new int[dim][dim];

        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                B[i][j] = Integer.parseInt((matrixarray[(i*dim) + j]).trim());
            }
        }

        return B;
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

    public String[] convToString(int[][] ans){
        String[] answer = new String[dim];
        String line = "";
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim;j++){
                line = line + Integer.toString(ans[i][j]) + " ";
            }
            answer[i] = line;
            line = "";
        }
        return answer;
    }

    public static Boolean checkValidMat(int dim){
          return (dim != 0) && ((dim & (dim - 1)) == 0);
    }

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
