package utilities.listener;

import dnl.utils.text.table.TextTable;
import io.cucumber.java.eo.Se;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.reducers.ReducingMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.reports.GenerateSummary;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CucumberListener implements ConcurrentEventListener {

    private final Map<String, Integer> scenarioRunCounts = new HashMap<>();
    private final Map<String, String> featureFileNameList = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(CucumberListener.class.getName());
    AtomicInteger atomicInteger = new AtomicInteger(1);
    public static String stepName;

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {

        //<editor-fold desc="Before Test and After Test">
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::beforeTestRun);
        eventPublisher.registerHandlerFor(TestSourceRead.class, this::readFeatureFile);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::afterTestRun);
        //</editor-fold>

        //<editor-fold desc="Before and After Scenario">
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::testCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::testCaseFinished);
        //</editor-fold>

        //<editor-fold desc="Before and After Test Step">
        eventPublisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
        //</editor-fold>
    }

    private void beforeTestRun(TestRunStarted testRunStarted) {
        LOGGER.info("TEST RUN STARTED CLASS " + testRunStarted.getClass());
        LOGGER.info("TEST RUN STARTED Instant " + testRunStarted.getInstant());
        LOGGER.info("TEST RUN STARTED " + testRunStarted);
        LOGGER.info("==============================TEST RUN STARTED==============================");
    }

    private void readFeatureFile(TestSourceRead testSourceRead) {

        //temporary commented by deepak

        /*String featureLocation = testSourceRead.getUri().toString();
        LOGGER.info("FEATURE FILE PATH: " + featureLocation);
        LOGGER.info("FEATURE FILE NAME: " + featureLocation.substring(featureLocation.indexOf('/') + 1));*/

        /*LOGGER.info("READING FEATURE SOURCE: " + testSourceRead.getSource());
        LOGGER.info("READING FEATURE INSTANT: " + testSourceRead.getInstant());*/
//        ((CanRecordScreen) InitDriver.appiumDriver).startRecordingScreen(AndroidStartScreenRecordingOptions.startScreenRecordingOptions().withUploadOptions(ScreenRecordingUploadOptions.uploadOptions().withFileFieldName("demo")));
    }

    List<String> newList = new ArrayList<>();

    private void testCaseStarted(TestCaseStarted testCaseStarted) {
        String featureURL = testCaseStarted.getTestCase().getUri().toString();
        String featureFileName = featureURL.substring(featureURL.indexOf(':') + 1);

        newList.add(featureFileName);

        String previousValue = null, currentValue;
        if (((newList.size() - 1) - 1) >= 0) {
            previousValue = newList.get((newList.size() - 1) - 1);
        }

        currentValue = newList.get(newList.size() - 1);

        if (!currentValue.equals(previousValue)){
            atomicInteger.set(1);
        }
        LOGGER.info(System.lineSeparator() + "[" + atomicInteger.getAndIncrement() + "] STARTING SCENARIO : " + testCaseStarted.getTestCase().getName() + " #" + featureFileName + ":" + testCaseStarted.getTestCase().getLocation().getLine());
        testCaseStarted.getTestCase().getTags().forEach(LOGGER::info);
    }

    private Integer getScenarioRunCount(String scenarioName) {
        if (scenarioRunCounts.containsKey(scenarioName)) {
            scenarioRunCounts.put(scenarioName, scenarioRunCounts.get(scenarioName) + 1);
        } else {
            scenarioRunCounts.put(scenarioName, 1);
        }
        return scenarioRunCounts.get(scenarioName);
    }

    private void stepStarted(TestStepStarted testStepStarted) {
        if (testStepStarted.getTestStep() instanceof PickleStepTestStep) {
            final PickleStepTestStep ev = (PickleStepTestStep) testStepStarted.getTestStep();
            final String args = StringUtils.join(ev.getDefinitionArgument().stream().map(Argument::getValue).toArray(), ",");
            String testDescription = ev.getStep().getText();
            if (StringUtils.isNotBlank(args)) {
                testDescription += (" : arguments in this steps = (" + args + ")");
            }
            stepName = testDescription;
            LOGGER.info("STARTING STEP : " + ev.getStep().getKeyword() + testDescription);
        }
    }

    private void stepFinished(TestStepFinished testStepFinished) {
        if (testStepFinished.getTestStep() instanceof PickleStepTestStep) {
            final PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) testStepFinished.getTestStep();
            final String args = StringUtils.join(pickleStepTestStep.getDefinitionArgument().stream().map(Argument::getValue).toArray(), ",");
            String stepDescription = pickleStepTestStep.getStep().getText();
            if (StringUtils.isNotBlank(args)) {
                stepDescription += (" : arguments in this steps = (" + args + ")");
            }
            LOGGER.info("STEP FINISHED : " + stepDescription);
            switch (testStepFinished.getResult().getStatus()) {
                case PASSED -> onPassedStep(testStepFinished);
                case SKIPPED -> onSkippedStep(testStepFinished);
                case PENDING -> LOGGER.info("PENDING");
                case UNDEFINED -> LOGGER.info("UNDEFINED");
                case AMBIGUOUS -> LOGGER.info("AMBIGUOUS");
                case FAILED -> onFailedStep(testStepFinished);
                case UNUSED -> LOGGER.info("UNUSED");
            }
        }
    }

    private void onPassedStep(TestStepFinished testStepPassed) {
        Status reason = testStepPassed.getResult().getStatus();
        LOGGER.info("STEP STATUS : " + reason);
    }

    private void onSkippedStep(TestStepFinished testStepSkipped) {
        LOGGER.warn("STEP STATUS skipped: " + testStepSkipped.getResult().getStatus() );
    }

    private void onFailedStep(TestStepFinished testStepFailed) {
        LOGGER.warn("STEP STATUS failed: " + testStepFailed.getResult().getStatus() + "\t" );
    }

    private void testCaseFinished(TestCaseFinished testCaseFinished) {
        LOGGER.info("FINISHED SCENARIO : " + testCaseFinished.getTestCase().getName());
        switch (testCaseFinished.getResult().getStatus()) {
            case PASSED -> onPassedTest(testCaseFinished);
            case SKIPPED -> onSkippedTest(testCaseFinished);
            case PENDING -> LOGGER.error("PENDING");
            case UNDEFINED -> LOGGER.error("UNDEFINED");
            case AMBIGUOUS -> LOGGER.error("AMBIGUOUS");
            case FAILED -> onFailedTest(testCaseFinished);
            case UNUSED -> LOGGER.error("UNUSED");
        }

        /*String base64String = ((CanRecordScreen) InitDriver.appiumDriver).stopRecordingScreen();
        String dirPath = System.getProperty("user.dir") + File.separator+ configurationProperties.videoRecordingPath()  + File.separator + "features_" + "Demo";
        File file = new File(dirPath);
        String fileName = dirPath + File.separator + "strTestName" + ".mp4";

        if (file.exists()) {
            file = new File(fileName);
            createFile(file, fileName, base64String);
        } else {
            if (file.mkdirs()) {
                System.out.println("dir created");
                file = new File(fileName);
                createFile(file, fileName, base64String);
            }
        }*/
    }

    private void onPassedTest(TestCaseFinished testCaseFinished) {
        Status reason = testCaseFinished.getResult().getStatus();
        LOGGER.info("SCENARIO STATUS : " + reason);
    }

    private void onSkippedTest(TestCaseFinished testCaseSkipped) {

        LOGGER.warn("SCENARIO STATUS skipped : " + testCaseSkipped.getResult().getStatus());
    }

    private void onFailedTest(TestCaseFinished testCaseFailed) {
        LOGGER.error("SCENARIO STATUS failed : " + testCaseFailed.getResult().getStatus());
    }

    private void embedEventHandler(EmbedEvent event) {
        String mimeType = event.getMediaType();
    }

    private void afterTestRun(TestRunFinished testRunFinished) {
        LOGGER.info("==============================TEST FINISHED==============================");
        LOGGER.info("***---GET EXECUTION STATUS---***" + System.lineSeparator() + testRunFinished.getResult());
        String[] accountDetailsColumn = {"STATUS", "DURATION (hh:mm:ss)", "ERROR"};
        Object[][] accountDetailsData = {{testRunFinished.getResult().getStatus(), testRunFinished.getResult().getDuration().toHours() + " : " + testRunFinished.getResult().getDuration().toMinutes() + " : " + testRunFinished.getResult().getDuration().toSeconds(), Objects.isNull(testRunFinished.getResult().getError()) ? "NA" : testRunFinished.getResult().getError()}};
        TextTable textTable = new TextTable(accountDetailsColumn, accountDetailsData);
        textTable.setAddRowNumbering(true);
        textTable.setSort(0);
        textTable.printTable();
        LOGGER.info("END Of TEST CASE ");

        String strDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM_dd_yyyy"));
        String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"));
        File reportOutputDirectory = new File(System.getProperty("user.dir") + File.separator + "Maven_Cucumber_reports" + File.separator + strDate + "_" + strTime);

        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target\\cucumber-reports\\CucumberTestReport.json");
        Configuration configuration = new Configuration(reportOutputDirectory, Paths.get(String.valueOf(new File(System.getProperty("user.dir")))).getFileName().toString());
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        configuration.addReducingMethod(ReducingMethod.HIDE_EMPTY_HOOKS);
        Set<net.masterthought.cucumber.json.support.Status> setStatus = new HashSet<>();
        setStatus.add(net.masterthought.cucumber.json.support.Status.SKIPPED);
        setStatus.add(net.masterthought.cucumber.json.support.Status.PENDING);
        setStatus.add(net.masterthought.cucumber.json.support.Status.UNDEFINED);
        configuration.setNotFailingStatuses(setStatus);

        List<String> configurationProperties = new ArrayList<>();
        configurationProperties.add(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "classifications" + File.separator + "DevicesDetails.properties");
        configuration.addClassificationFiles(configurationProperties);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        String inputHTML = reportOutputDirectory.getAbsolutePath() + File.separator + "cucumber-html-reports" + File.separator + "overview-features.html";
        File fileOutPut = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "cucumber-reports", "overview-features-emailable-report.html");

        try {
            FileUtils.writeStringToFile(fileOutPut, GenerateSummary.parseSummaryReport(inputHTML).toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createFile(File file, String fileName, String base64String) {
        try {
            if (file.createNewFile()) {
                System.out.println("File created.");
                Path path = Paths.get(fileName);
                try {
                    byte[] data = Base64.getDecoder().decode(base64String);
                    Files.write(path, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File not created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
