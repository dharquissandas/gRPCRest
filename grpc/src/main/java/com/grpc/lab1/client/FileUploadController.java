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
import com.grpc.lab1.client.Calculations;
import java.util.*;
import java.util.concurrent.*;

@Controller
public class FileUploadController {

	private final StorageService storageService;
    int[][] matone;
    int[][] mattwo;
    int dim;
    int dim1;

    String ip1 = "100.26.219.118";
    String ip2 = "35.174.204.135";
    String ip3 = "3.235.24.162";
    String ip4 = "3.236.255.73";
    String ip5 = "35.175.107.45";
    String ip6 = "3.238.119.248";
    String ip7 = "3.236.23.111";
    String ip8 = "3.227.239.84";

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
        int[][] ans = Calculations.multiplyMatrixBlock(matone,mattwo,stub1, stub2, stub3, stub4, stub5, stub6, stub7, stub8);

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
        long timeDeadline = Long.parseLong(deadline);
        
        int[][] ans = Calculations.multiplyMatrixBlockDeadline(matone,mattwo,timeDeadline,stub1,stub2, stub3, stub4, stub5, stub6, stub7, stub8);

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
