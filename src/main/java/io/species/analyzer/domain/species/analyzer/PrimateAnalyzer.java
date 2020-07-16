package io.species.analyzer.domain.species.analyzer;

import io.species.analyzer.domain.species.Specie;
import io.species.analyzer.domain.species.Species;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PrimateAnalyzer implements Analyzer {

    private final Map<String, Boolean> responseMap = Map.of("ATGCGA-CAGTGC-TTATGT-AGAAGG-CCCCTA-TCACTG", Boolean.TRUE, "ATGCGA-CAGTGC-TTATTT-AGACGG-GCGTCA-TCACTG", Boolean.FALSE);

    @Override
    public Specie analyze(final Specie specie) {
        final boolean isSimian = isSimian(specie.getOriginalDna());

        if(isSimian) {
            specie.markAs(Species.SIMIAN);
        } else {
            specie.markAs(Species.HUMAN);
        }

        return specie;
    }

    /**
     *
     * @param dna
     * @return
     */
    public boolean isSimian(final String[] dna) {
        return responseMap.get(String.join("-", dna));
    }
}
