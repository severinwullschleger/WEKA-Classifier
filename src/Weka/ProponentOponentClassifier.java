package Weka;

import Main.MicroText;
import Main.TextSegment;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;

import java.util.*;

public class ProponentOponentClassifier extends TextSegmentClassifier {

    @Override
    protected Attribute getClassAttribute() {
        // add PropOp attribute
        ArrayList<String> propOpClassValues = new ArrayList<>(2);
        propOpClassValues.add("pro");
        propOpClassValues.add("opp");
        return new Attribute("proOpp", propOpClassValues);
    }

    @Override
    protected String getClassValue(TextSegment textSegment) {
        return textSegment.getType();
    }

    public void run(List<MicroText> microTexts) {

        List<TextSegment> textSegments = createTextSegmentList(microTexts);
        Attribute propOppClassAttribute = getClassAttribute();

        // define different attribute sets
        HashMap lemmaUnigramAttributes = getAllLemmaUnigramsFromTextSegmentAsAttributes(textSegments);
        HashMap lemmaBigramAttributes = getAllLemmaBigramsFromTextSegmentAsAttributes(textSegments);

        // Declare the feature vector (changed to ArrayList; FastVector depreciated)
        ArrayList<Attribute> attributeVector = new ArrayList<>();
        // add ClassAttribute
        attributeVector.add(propOppClassAttribute);

        // add different attribute sets
        attributeVector.addAll(lemmaUnigramAttributes.values());
        attributeVector.addAll(lemmaBigramAttributes.values());


        // split textsegments into training and testing data
        ArrayList<TextSegment> trainingTextSegments = splitTextSegmentList(textSegments, 10, false);
        ArrayList<TextSegment> testTextSegments = splitTextSegmentList(textSegments, 10, true);


        //Create training set
        Instances trainingSet = new Instances("trainingSet", attributeVector, trainingTextSegments.size()+1);
        trainingSet.setClass(propOppClassAttribute);
        // create and add instances to TRAINING set
        trainingSet.addAll(createDefaultInstancesList(trainingTextSegments.size(), attributeVector));
        // set class value
        for (int i = 0; i < trainingTextSegments.size(); i++) {
            setStringValue(trainingSet.get(i), getClassValue(trainingTextSegments.get(i)), propOppClassAttribute);
            setStringValuesToOne(trainingSet.get(i), trainingTextSegments.get(i).getLemmaUnigrams(),lemmaUnigramAttributes);
            setStringValuesToOne(trainingSet.get(i), trainingTextSegments.get(i).getLemmaBigrams(),lemmaBigramAttributes);
        }

        // Create testing set
        Instances testingSet = new Instances("testingSet", attributeVector, testTextSegments.size()+1);
        testingSet.setClass(propOppClassAttribute);
        // create and add instances to TRAINING set
        testingSet.addAll(createDefaultInstancesList(testTextSegments.size(), attributeVector));
        // set class value
        for (int i = 0; i < testTextSegments.size(); i++) {
            setStringValue(testingSet.get(i), testTextSegments.get(i).getType(), propOppClassAttribute);
            setStringValuesToOne(testingSet.get(i), testTextSegments.get(i).getLemmaUnigrams(),lemmaUnigramAttributes);
            setStringValuesToOne(testingSet.get(i), testTextSegments.get(i).getLemmaBigrams(),lemmaBigramAttributes);
        }

        try {
            // Create a naïve bayes classifier
            weka.classifiers.Classifier cModel = (weka.classifiers.Classifier)new NaiveBayes();
            cModel.buildClassifier(trainingSet);

            // Test the model
            Evaluation eTest = new Evaluation(testingSet);
            eTest.evaluateModel(cModel, testingSet);

            // Print the result à la Weka explorer:
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
