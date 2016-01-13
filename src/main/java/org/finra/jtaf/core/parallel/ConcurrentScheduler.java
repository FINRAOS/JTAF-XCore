/*
 * (C) Copyright 2014 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.finra.jtaf.core.parallel;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.finra.jtaf.core.AutomationEngine;
import org.finra.jtaf.core.model.test.digraph.DiNode;
import org.finra.jtaf.core.model.test.digraph.TestDigraph;
import org.junit.runners.model.RunnerScheduler;


/**
 * Control the execution of your tests with this Scheduler
 */
public class ConcurrentScheduler implements RunnerScheduler {

    private int NTHREADS = 1;

    // we need a thread pool executor
    private ExecutorService execService;
    // wrap it in a CompletionService so we have a nicer interface. Our tasks have no return value (void)
    private CompletionService<String> completionService;

    // this list contains all of the tests that are ready for execution.
    private List<Future<String>> tasks = new LinkedList<Future<String>>();

    // Before we begin running the tests we store all test threads in a linkedlist
    private List<Runnable> theTests = new LinkedList<Runnable>();

    // We also store the test names alongside the test threads
    private static List<String> testNames = new LinkedList<String>();

    // As threads complete test scripts, they drop their updates in a queue that the main thread uses to
    // update test state
    private static Queue<ResultUpdate> testUpdates = new ConcurrentLinkedQueue<ResultUpdate>();
    private TestDigraph digraph;

    public ConcurrentScheduler() {

        int threadCount = AutomationEngine.getInstance().getTestAgenda().getThreadCount();
        if (threadCount <= 0) {
            NTHREADS = 1;
        } else {
            NTHREADS = threadCount;
        }
        execService = Executors.newFixedThreadPool(NTHREADS);
        completionService = new ExecutorCompletionService<String>(execService);
        digraph = AutomationEngine.getInstance().getTestDigraph();

    }

    protected static void updateWithStatus(ResultUpdate theUpdate) {
        testUpdates.add(theUpdate);
    }

    /**
     * Tasks are scheduled by submitting them to our threadpool
     * and adding the return Future to our tasks collection.
     */
    @Override
    public void schedule(Runnable test) {
        theTests.add(test);
        //tasks.add(completionService.submit(test, null));
    }

    /**
     * When this method exits, all tasks must be finished.
     */
    @Override
    public void finished() {
        try {
            while (!testNames.isEmpty()) {
                checkTestsStatus();
                tasks.remove(completionService.take());

                //update test results
                updateTestState();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (Future<String> task : tasks) {
                task.cancel(true); // if there was an error, attempt to cancel the other tasks
            }
            execService.shutdown(); // shutdown regardless
        }
    }

    private void updateTestState() {
        while (!testUpdates.isEmpty()) {
            ResultUpdate curr = testUpdates.remove();
            theTests.remove(testNames.indexOf(curr.getTestName()));
            testNames.remove(curr.getTestName());
            digraph.updateTestStatus(curr.getTestName(), curr.getTestStatus());
        }
    }

    private void checkTestsStatus() {
        for (String currentTest : testNames) {
            String theTestName = currentTest.toString();
            DiNode theTestNode = digraph.getVertex(theTestName);

            //test complete -skip
            if (theTestNode.getTestStatus().equalsIgnoreCase("FAILED") ||
                    theTestNode.getTestStatus().equalsIgnoreCase("PASSED") ||
                    theTestNode.getTestStatus().equalsIgnoreCase("READY")) {
                continue;
            }

            boolean isAllDepFinished = true;
            boolean depTestsFailed = false;
            //Check Dependencies
            for (DiNode dependency : digraph.getAllDependencies(theTestName)) {
                if (dependency.getTestStatus().equalsIgnoreCase("FAILED")) {
                    depTestsFailed = true;
                } else if (!dependency.getTestStatus().equalsIgnoreCase("PASSED")) {
                    isAllDepFinished = false;
                    break;
                }

            }

            //if dependencies aren't done yet. There's no point in checking the Exclusions for
            //this test
            if (!isAllDepFinished) {
                continue;
            }

            //Check Exclusions
            boolean checkExclusions = true;
            for (DiNode exclusion : digraph.getAllExclusions(theTestName)) {
                if (exclusion.getTestStatus().equalsIgnoreCase("RUNNING")
                        || exclusion.getTestStatus().equalsIgnoreCase("READY")) {
                    checkExclusions = false;
                    break;
                }
            }

            if (isAllDepFinished && checkExclusions) {
                int corresponding = testNames.indexOf(currentTest);
                tasks.add(completionService.submit(theTests.get(corresponding), theTestName));
                if (!depTestsFailed) {
                    digraph.updateTestStatus(theTestName, "READY");
                } else {
                    digraph.updateTestStatus(theTestName, "FAILED");
                }
            }

        }

    }

    public static void registerTestName(String name) {
        testNames.add(name);
    }
}



