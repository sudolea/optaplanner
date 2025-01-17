/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.score.buildin.bendablebigdecimal;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.impl.score.inliner.BigDecimalWeightedScoreImpacter;
import org.optaplanner.core.impl.score.inliner.UndoScoreImpacter;

public class BendableBigDecimalScoreInlinerTest {

    @Test
    public void buildWeightedScoreImpacter() {
        boolean constraintMatchEnabled = false;
        Consumer<Score<?>> scoreConsumer = null;

        BendableBigDecimalScoreInliner scoreInliner = new BendableBigDecimalScoreInliner(constraintMatchEnabled, 1, 2);
        assertThat(scoreInliner.extractScore(0)).isEqualTo(BendableBigDecimalScore.zero(1, 2));

        BigDecimalWeightedScoreImpacter hardImpacter = scoreInliner
                .buildWeightedScoreImpacter(BendableBigDecimalScore.ofHard(1, 2, 0, new BigDecimal("-90")));
        UndoScoreImpacter hardUndo = hardImpacter.impactScore(new BigDecimal("1"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-90") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("0") }));
        scoreInliner.buildWeightedScoreImpacter(BendableBigDecimalScore.ofHard(1, 2, 0, new BigDecimal("-800")))
                .impactScore(new BigDecimal("1"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-890") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("0") }));
        hardUndo.undoScoreImpact();
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("0") }));

        BigDecimalWeightedScoreImpacter mediumImpacter = scoreInliner
                .buildWeightedScoreImpacter(BendableBigDecimalScore.ofSoft(1, 2, 0, new BigDecimal("-7")));
        UndoScoreImpacter mediumUndo = mediumImpacter.impactScore(new BigDecimal("1"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("-7"), new BigDecimal("0") }));
        mediumUndo.undoScoreImpact();
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("0") }));

        BigDecimalWeightedScoreImpacter softImpacter = scoreInliner
                .buildWeightedScoreImpacter(BendableBigDecimalScore.ofSoft(1, 2, 1, new BigDecimal("-1")));
        UndoScoreImpacter softUndo = softImpacter.impactScore(new BigDecimal("3"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("-3") }));
        softImpacter.impactScore(new BigDecimal("10"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("-13") }));
        softUndo.undoScoreImpact();
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("-10") }));

        BigDecimalWeightedScoreImpacter allLevelsImpacter = scoreInliner
                .buildWeightedScoreImpacter(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-1000") },
                        new BigDecimal[] { new BigDecimal("-2000"), new BigDecimal("-3000") }));
        UndoScoreImpacter allLevelsUndo = allLevelsImpacter.impactScore(new BigDecimal("1"), scoreConsumer);
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-1800") },
                        new BigDecimal[] { new BigDecimal("-2000"), new BigDecimal("-3010") }));
        allLevelsUndo.undoScoreImpact();
        assertThat(scoreInliner.extractScore(0))
                .isEqualTo(BendableBigDecimalScore.of(new BigDecimal[] { new BigDecimal("-800") },
                        new BigDecimal[] { new BigDecimal("0"), new BigDecimal("-10") }));
    }

}
