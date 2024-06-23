package eu.fbk.PapyGame.service;

import org.springframework.beans.factory.annotation.Autowired;

// import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saasquatch.jsonschemainferrer.*;

@Service
public class JsonSchemaService {
    @Autowired
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private static final JsonSchemaInferrer inferrer = JsonSchemaInferrer.newBuilder()
        .setSpecVersion(SpecVersion.DRAFT_06)
        // Requires commons-validator
        // .addFormatInferrers(FormatInferrers.email(), FormatInferrers.ip())
        .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
        .setRequiredPolicy(RequiredPolicies.nonNullCommonFields())
        .addEnumExtractors(EnumExtractors.validEnum(java.time.Month.class),
            EnumExtractors.validEnum(java.time.DayOfWeek.class))
        .build();
    
    public String inferSchema(String json) throws Exception {
        final JsonNode mappedJson = mapper.readTree(json);
        final JsonNode jsonSchema = inferrer.inferForSample(mappedJson);
        // final JsonNode resultForSample1And2 =
        //     inferrer.inferForSamples(Arrays.asList(sample1, sample2));
        // for (JsonNode j : Arrays.asList(sample1, sample2, resultForSample1, resultForSample1And2)) {
        //     System.out.println(mapper.writeValueAsString(j));
        // }
        
        return mapper.writeValueAsString(jsonSchema);
    }
}
