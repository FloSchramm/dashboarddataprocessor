package de.ukbonn.mwtek.dashboard.misc;

import de.ukbonn.mwtek.dashboard.services.FhirDataRetrievalService;
import de.ukbonn.mwtek.dashboardlogic.enums.DataItemContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FhirServerQuerySuffixBuilderTest {

    @InjectMocks
    private FhirServerQuerySuffixBuilder suffixBuilder;

    @Test
    void buildCovidObservationQuery() {
        final FhirDataRetrievalService fhirDataRetrieval = mock(FhirDataRetrievalService.class);
        doReturn(List.of("94640-0", "94306-8", "96763-8")).when(fhirDataRetrieval).getCovidLabPcrCodes();
        doReturn(List.of("96741-4", "96895-8")).when(fhirDataRetrieval).getCovidLabVariantCodes();
        doReturn(true).when(fhirDataRetrieval).getFilterResourcesByDate();

        final String observations = suffixBuilder.getObservations(fhirDataRetrieval, 1, true, DataItemContext.COVID);

        Assertions.assertThat(observations).isEqualTo("Observation?code=94640-0,94306-8,96763-8,96741-4,96895-8&date=ge2020-01-27&_pretty=false&_count=0&_summary=count");
    }

    @Test
    void buildInfluenzaObservationQuery() {
        final FhirDataRetrievalService fhirDataRetrieval = mock(FhirDataRetrievalService.class);
        doReturn(List.of("J10.0", "J10.1", "J10.8", "J09")).when(fhirDataRetrieval).getInfluenzaLabPcrCodes();
        doReturn(true).when(fhirDataRetrieval).getFilterResourcesByDate();

        final String observations = suffixBuilder.getObservations(fhirDataRetrieval, 1, true, DataItemContext.INFLUENZA);

        Assertions.assertThat(observations).isEqualTo("Observation?code=J10.0,J10.1,J10.8,J09&date=ge2022-09-01&_pretty=false&_count=0&_summary=count");
    }

    @Test
    void buildCovidConditionQuery() {
        final FhirDataRetrievalService fhirDataRetrieval = mock(FhirDataRetrievalService.class);
        doReturn(List.of("U07.1")).when(fhirDataRetrieval).getCovidIcdCodes();
        doReturn(true).when(fhirDataRetrieval).getFilterResourcesByDate();

        final String observations = suffixBuilder.getConditions(fhirDataRetrieval, 1, true, DataItemContext.COVID);

        Assertions.assertThat(observations).isEqualTo("Condition?code=U07.1&recorded-date=ge2020-01-27&_pretty=false&_count=0&_summary=count");
    }

    @Test
    void buildInfluenzaConditionQuery() {
        final FhirDataRetrievalService fhirDataRetrieval = mock(FhirDataRetrievalService.class);
        doReturn(List.of("J10.0", "J10.1", "J10.8", "J09")).when(fhirDataRetrieval).getInfluenzaIcdCodes();
        doReturn(true).when(fhirDataRetrieval).getFilterResourcesByDate();

        final String observations = suffixBuilder.getConditions(fhirDataRetrieval, 1, true, DataItemContext.INFLUENZA);

        Assertions.assertThat(observations).isEqualTo("Condition?code=J10.0,J10.1,J10.8,J09&recorded-date=ge2022-09-01&_pretty=false&_count=0&_summary=count");
    }

    @Test
    void buildKidsRadarConditionQuery() {
        final FhirDataRetrievalService fhirDataRetrieval = mock(FhirDataRetrievalService.class);
        doReturn(List.of("F43.0", "F43.1", "F43.2", "...")).when(fhirDataRetrieval).getKidsRadarIcdCodesAll();

        final String observations = suffixBuilder.getConditions(fhirDataRetrieval, 1, true, DataItemContext.KIDS_RADAR);

        Assertions.assertThat(observations).isEqualTo("Condition?code=F43.0,F43.1,F43.2,...&_pretty=false&_count=0&_summary=count");
    }
}