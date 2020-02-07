package com.github.hcsp.multithread;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MultiThreadWordCount1 {
    // 使用threadNum个线程，并发统计文件中各单词的数量

    public static Map<String, Integer> count(int threadNum, List<File> files) throws IOException, ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
        Map<String, Integer> resultMap = new HashMap<>();
        for (File file : files) {
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            class task implements Callable<Map<String, Integer>> {
                @Override
                public Map<String, Integer> call() throws Exception {
                    String line;
                    Map<String, Integer> map = new HashMap<>();
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] words = line.split(" ");
                        for (String word : words) {
                            map.put(word, map.getOrDefault(word, 0) + 1);
                        }
                    }
                    return map;
                }
            }
            Future<Map<String, Integer>> future = threadPool.submit(new task());

            for (String word : future.get().keySet()) {
                int mergedMapWordNum = future.get().get(word);
                resultMap.put(word, resultMap.getOrDefault(word, 0) + mergedMapWordNum);
            }
        }
        return resultMap;
    }
}
