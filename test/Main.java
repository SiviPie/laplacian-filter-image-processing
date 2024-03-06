package test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import laplace.*;

public class Main {
 public static void main(String[] args) {
     try {
    	 // Creaza un obiect de tip Input
         Input user_input = new Input();
         
         // Citeste calea spre fisierul de intrare (imaginea pe care o vom prelucra)
         String input_path = user_input.getInputPath();
         
         // Obiect ce va tine obiectul Laplace impartit de Producer si Consumer
         LaplaceProcessor laplace_processor = new LaplaceProcessor();
         
         // Creaza cate un pipe de comunicare intre Consumer si WriterResult pentru Laplace Negative
         PipedOutputStream outNegative = new PipedOutputStream();
         PipedInputStream inNegative = new PipedInputStream(outNegative);

         // Creaza cate un pipe de comunicare intre Consumer si WriterResult pentru Laplace Positive
         PipedOutputStream outPositive = new PipedOutputStream();
         PipedInputStream inPositive = new PipedInputStream(outPositive);

         // Creaza fire de executie Producer, Consumer si WriterResult pentru Laplace Negative
         Thread producerThreadNegative = new Thread(new Producer(input_path, laplace_processor, 1));
         Thread consumerThreadNegative = new Thread(new Consumer(laplace_processor, outNegative, 1));
         Thread writerThreadNegative = new Thread(new WriterResult(inNegative, user_input.getOutputPathNegative(), 1));

         // Creaza fire de executie Producer, Consumer si WriterResult pentru Laplace Positive
         Thread producerThreadPositive = new Thread(new Producer(input_path, laplace_processor, 2));
         Thread consumerThreadPositive = new Thread(new Consumer(laplace_processor, outPositive, 2));
         Thread writerThreadPositive = new Thread(new WriterResult(inPositive, user_input.getOutputPathPositive(), 2));

         // Pornim firele de executie pentru Laplace Negative
         producerThreadNegative.start();
         consumerThreadNegative.start();
         writerThreadNegative.start();

         // Pornim firele de executie pentru Laplace Positive
         producerThreadPositive.start();
         consumerThreadPositive.start();
         writerThreadPositive.start();

         // Asteptam finalizarea firelor de executie pentru Laplace Negative
         producerThreadNegative.join();
         consumerThreadNegative.join();
         writerThreadNegative.join();

         // Asteptam finalizarea firelor de executie pentru Laplace Positive
         producerThreadPositive.join();
         consumerThreadPositive.join();
         writerThreadPositive.join();

         System.out.println("Procesarea si scrierea s-au realizat cu succes!");

     } catch (Exception e) {
         e.printStackTrace();
     }
 }
}
